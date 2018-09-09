import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientConnectionThread
{
    // GUI items
    JButton sendButton;
    JButton connectButton;
    JTextField machineInfo;
    JTextField portInfo;

    JTextField message;
    JTextArea history;

    // Network Items
    boolean connected;
    Socket echoSocket;
    PrintWriter out;
    BufferedReader in;
    int id;
    ClientGUI gui;

    // set up GUI
    public ClientConnectionThread(ClientGUI g)
    {
        gui = g;
        machineInfo = g.servIP;
        portInfo = g.servPort;
        message = g.msg;
        history = g.chatbox;
        connected = g.connected;
    } 
    
    //this function is called when a messege is being sent
    public void doSendMessage()
    {
        try
        {
            out.println("m " +gui.ClientID + " "+ gui.clientsIDS.get(gui.recipient)  + " " +message.getText());
        }
        catch (Exception e)
        {
            history.insert ("Error in processing message ", 0);
        }
    }
    
    //tell the server to remove it from online users
    public void leave(){
        try
        {
            out.println("l " + gui.ClientID );
        }
        catch (Exception e)
        {
            history.insert ("Error in processing message ", 0);
        }
    }

    public void doManageConnection()
    {
        if (connected == false)
        {
            String machineName = null;
            int portNum = -1;
            try {
                machineName = machineInfo.getText();
                portNum = Integer.parseInt(portInfo.getText());
                echoSocket = new Socket(machineName, portNum );
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

                // start a new thread to read from the socket
                new CommunicationReadThread (in, this);

                gui.connected = true;
                gui.connectBtn.setText("Disconnect from Server");
            } 
            catch (NumberFormatException e) {
                gui.chatbox.insert ( "Server Port must be an integer\n", 0);
            } 
            catch (UnknownHostException e) {
                gui.chatbox.insert("Don't know about host: " + machineName , 0);
            } 
            catch (IOException e) {
                gui.chatbox.insert ("Couldn't get I/O for "
                        + "the connection to: " + machineName , 0);
            }
        }
        else{
            try{
                out.close();
                in.close();
                echoSocket.close();
                sendButton.setEnabled(false);
                connected = false;
                connectButton.setText("Connect to Server");
            }
            catch (IOException e){
                history.insert ("Error in closing down Socket ", 0);
            }
        }
    }
}
