import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Vector;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.Set;

class CommunicationThread extends Thread
{
    //private boolean serverContinue = true;
    private Socket clientSocket;
    private EchoServer gui;
    private Vector<PrintWriter> outStreamList;

    public CommunicationThread (Socket clientSoc, EchoServer ec3, Vector<PrintWriter> oSL)
    {
        clientSocket = clientSoc;
        gui = ec3;
        outStreamList = oSL;
        gui.history.insert ("Comminucating with Port" + clientSocket.getLocalPort()+"\n", 0);
        start();
    }

    public void run()
    {
        System.out.println ("New Communication Thread Started");

        try {
            gui.lock.lock();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            outStreamList.add(out);

            BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
            String inputLine;


            out.println("i " +Integer.toString( gui.clientNum));
            gui.clients.put(gui.clientNum, out);
            gui.clientsNames.put(Integer.toString(gui.clientNum), " ");
            gui.clientNum++;

            gui.lock.unlock();
            while ((inputLine = in.readLine()) != null )
            {
                gui.history.insert (inputLine+"\n", 0);

                if(inputLine.charAt(0) == 'm'){
                    System.out.println(inputLine);
                    String[] splitStr = inputLine.split("\\s+");
                    int key = Integer.parseInt(splitStr[2]);
                    PrintWriter sendTo = gui.clients.get(key);
                    sendTo.println(inputLine);
                }

                if(inputLine.charAt(0) == 'u'){
                    String[] splitStr = inputLine.split("\\s+");
                    String key = splitStr[1];

                    checkAndAddName(key, splitStr[2]);
                    String newOnlineList = createNewOnlineList();

                    for ( PrintWriter out1: outStreamList )
                    {
                        out1.println (newOnlineList);
                    }
                }

                if(inputLine.charAt(0) == 'l'){
                    String[] splitStr = inputLine.split("\\s+");
                    String key = splitStr[1];
                    gui.clientsNames.remove(key);
                    gui.clients.remove(Integer.parseInt(key));


                    String newOnlineList = createNewOnlineList();
                    for ( PrintWriter out1: outStreamList )
                    {
                        out1.println (newOnlineList);
                    }
                }

                if (inputLine.equals("Bye."))
                    break;

                if (inputLine.equals("End Server."))
                    gui.serverContinue = false;
            }

            outStreamList.remove(out);
            gui.portInfo.setText("Not Listening");
            out.close();
            in.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("Problem with Communication Server");
        }
    }

    public void checkAndAddName(String key, String name){
        if(gui.clientsNames.get(key) == " "){
            int i = 0;
            while(true) {
                int match = 0;
                for (String n : gui.clientsNames.values()) {
                    if (name.equals(n)) {
                        match = 1;
                        break;
                    }
                }
                if(match == 0){
                    gui.clientsNames.replace(key, name);
                    break;
                }
                else{
                    name = name+Integer.toString(i);
                    i++;
                }
            }
        }
    }

    public String createNewOnlineList(){
        String newList = "o";

        for( String key: gui.clientsNames.keySet()){
            newList = newList + " " + key + "," + gui.clientsNames.get(key);
        }
        return newList;
    }
}