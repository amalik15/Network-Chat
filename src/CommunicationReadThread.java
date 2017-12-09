import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

// Class to handle socket reads
//   THis class is NOT written as a nested class, but perhaps it should
class CommunicationReadThread extends Thread
{
    //private Socket clientSocket;
    private ClientConnectionThread gui;
    private BufferedReader in;
    private BufferedReader out;


    public CommunicationReadThread (BufferedReader inparam, ClientConnectionThread ec3)
    {
        in = inparam;
        gui = ec3;
        start();
        gui.gui.chatbox.insert ("Communicating with Port\n", 0);
    }

    public void run()
    {
        System.out.println ("New Communication Thread Started");

        try {
            String inputLine;

            //get id from server


            while ((inputLine = in.readLine()) != null)
            {
                if(inputLine.charAt(0) == 'i'){
                    //System.out.println(inputLine);
                    String[] splitStr = inputLine.split("\\s+");
                    gui.gui.ClientID = Integer.parseInt(splitStr[1]);


                    if(gui.gui.username.getText() != ""){
                        gui.out.println ("u " + gui.gui.ClientID + " "+ gui.gui.username.getText());
                        gui.gui.sendBtn.setEnabled(true);
                    }
                    else{
                        JOptionPane.showMessageDialog(
                                null,
                                "Please create a display name",
                                "No Name", JOptionPane.PLAIN_MESSAGE );
                    }
                }

                else if(inputLine.charAt(0) == 'o'){
                    //System.out.println(inputLine);
                    String[] splitStr = inputLine.split("\\s+");
                    String users[] =  new String[splitStr.length];

                    for (int i = 0; i < gui.gui.clientsNames.size(); i++ ){
                        gui.gui.clientsIDS.remove(gui.gui.clientsNames.get(i));
                        gui.gui.clientsNames.remove(i);
                    }

                    gui.gui.sendTo.removeAllItems();
                    for (int i = 1; i < splitStr.length; i++ ){
                        //System.out.println(splitStr[i]);
                        String[] subStringArr = splitStr[i].split(",");
                        gui.gui.clientsIDS.put(subStringArr[1],Integer.parseInt(subStringArr[0]));
                        gui.gui.clientsNames.put(Integer.parseInt(subStringArr[0]), subStringArr[1]);
                        users[i-1] = subStringArr[1];
                        //System.out.println(subStringArr[1]);
                        gui.gui.sendTo.addItem(subStringArr[1]);

                        if(Integer.parseInt(subStringArr[0]) == gui.gui.ClientID){
                            gui.gui.username.setText(subStringArr[1]);
                        }
                    }
                }
                //history.insert ("From Server: " + in.readLine() + "\n" , 0);

                else if(inputLine.charAt(0) == 'm'){
                    //System.out.println(inputLine);
                    String[] splitStr = inputLine.split("\\s+");
                    int key = Integer.parseInt(splitStr[1]);

                    String sender = gui.gui.clientsNames.get(key);

                    String message = " ";
                    for (int i = 3; i < splitStr.length; i++){
                        message =" "+ splitStr[i];
                    }

                    gui.gui.chatbox.insert ("From " + sender + ":" + message + "\n", 0);
                }


                if (inputLine.equals("Bye."))
                    break;

            }

            in.close();
            //clientSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("Problem with Client Read");
            //System.exit(1);
        }
    }
}
