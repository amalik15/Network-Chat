/*David Qiao - dqiao4
 *Abdulaziz Malik - amalik11
 *
 * CS 342 Fall 2017
 * Professor Troy
 * Project 5 - Network Chat with RSA Encryption
 */


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.*;

public class ClientGUI extends JFrame implements ActionListener{

    JTextField servIP;
    JTextField servPort;
    JTextField username;
    JTextField pnum1, pnum2;
    JButton checkBtn, randomBtn;

    JTextArea chatbox;

    JComboBox<String> sendTo;
    JTextField msg;
    JButton connectBtn;
    JButton sendBtn;
    //*****COPIED******
    //EDIT BELOW CODE, COPIED DIRECTLY FROM TROY (in future marked by ***COPIED***)
    boolean connected;
    Socket echoSocket;
    PrintWriter out;
    BufferedReader in;
    //****END COPIED*****

    JFrame body;
    ClientConnectionThread connectionThread;
    int ClientID;
    String recipient;
    String namelist[] = {" "};
    Hashtable<Integer,String> clientsNames;
    Hashtable<String,Integer> clientsIDS;
    String un;

    public ClientGUI() {
        super( "RSA Encrypted Chat (Client)" );

        body = new JFrame();

        clientsNames = new Hashtable<Integer,String>();
        clientsIDS = new Hashtable<String, Integer>();

        //
        //body.setSize(700, 700);
        //body.setLayout(new BorderLayout());

        JPanel ntwkPanel = new JPanel();
        ntwkPanel.setLayout(new GridLayout(5,1));


        //***COPIED***
        connected = false;
        //***END COPIED***

        //ntwkPanel stuff
        ntwkPanel.add(new JLabel ("Server IP: ", JLabel.RIGHT));
        servIP = new JTextField("");
        ntwkPanel.add(servIP);

        ntwkPanel.add(new JLabel ("Port: ", JLabel.RIGHT));
        servPort = new JTextField("");
        ntwkPanel.add(servPort);

        ntwkPanel.add(new JLabel ("Username: ", JLabel.RIGHT));
        username = new JTextField("");
        ntwkPanel.add(username);

        JPanel LPanel = new JPanel();
        LPanel.setLayout(new GridLayout(1,1));
        JPanel RPanel = new JPanel();
        RPanel.setLayout(new GridLayout(1,1));
        ntwkPanel.add( LPanel, BorderLayout.WEST);
        ntwkPanel.add( RPanel, BorderLayout.EAST);

        LPanel.add(new JLabel ("Prime Numer #1: ", JLabel.LEFT));
        pnum1 = new JTextField("");
        LPanel.add(pnum1);

        RPanel.add(new JLabel ("Prime Numer #2: ", JLabel.LEFT));
        pnum2 = new JTextField("");
        RPanel.add(pnum2);

        checkBtn = new JButton("Verify");
        ntwkPanel.add(checkBtn);
        checkBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed( ActionEvent e )
					{
						checkPrimes(pnum1.getText(), pnum2.getText());
					}
				}//end of actionListener
		);
        

        randomBtn = new JButton("Randomize");
        ntwkPanel.add(randomBtn);
        randomBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed( ActionEvent e )
					{
						
						randomizePrimes();
					}
				}//end of actionListener
		);
        

        body.getContentPane().add(ntwkPanel, BorderLayout.NORTH);
//  JPanel connectPanel = new JPanel();
//  connectPanel.setLayout(new BorderLayout());
//  ntwkPanel.add(connectPanel, BorderLayout.SOUTH);



        //end of ntwkPanel stuff

        //end of ntwkPanel stuff

        chatbox = new JTextArea ( 10, 50 );
        chatbox.setEditable(false);
        body.getContentPane().add( new JScrollPane(chatbox), BorderLayout.CENTER);

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new GridLayout(3,1));
        body.getContentPane().add(chatPanel, BorderLayout.SOUTH);


        //chatPanel stuff
        //String namelist[] = {" ", "UglyBear", "Your Mother-in-law", "Her Majesty, The Queen of England", "Pam from the Office", "0", "1", "2"};
        //***remove this list once username list is created!!!!***

        chatPanel.add(new JLabel("Choose recipient:", JLabel.CENTER));
        sendTo = new JComboBox<>(namelist);
        sendTo.setSelectedIndex(4);
        sendTo.setEnabled(false);
        chatPanel.add(sendTo);

        chatPanel.add(new JLabel("Message:", JLabel.CENTER));
        msg = new JTextField("");
        chatPanel.add(msg);

        connectBtn = new JButton("Connect to Server");
        connectBtn.setEnabled(true);
        chatPanel.add(connectBtn);
        connectBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed( ActionEvent e )
					{
						if(numCheck == true && nameCheck == true) {
							doConnect();
							
						}
						
						else if (numCheck == false) {
							JOptionPane.showMessageDialog(
									ClientGUI.this,
									"",
									"Status of the Game", JOptionPane.PLAIN_MESSAGE );
						}
						
						else {
							
						}
					}
				}//end of actionListener
		);
        
        
        sendBtn = new JButton("Send");
        sendBtn.addActionListener(this);
        sendBtn.setEnabled(false);
        chatPanel.add(sendBtn);

        WindowListener exit1 = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                connectionThread.leave();
                System.exit(0);
            }
        };
        body.addWindowListener(exit1);

        connectionThread = new ClientConnectionThread(this);


        body.setSize(new Dimension(700,700));
        body.setLocationRelativeTo(null);
        body.setVisible(true);
        body.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    /*
    public static void main( String args[] )
    {
        ClientGUI app = new ClientGUI();
        app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
    */
    public void checkPrimes(String x, String y) {
		int a = Integer.parseInt(x);
		int b = Integer.parseInt(y);
		int c = a * b;
		if ( c > 268435456) {
			connectBtn.setEnabled(true);
		}
		else {
			connectBtn.setEnabled(false);
		}
	}
	
	public void randomizePrimes(){
		Scanner sc = new Scanner(new File("numfile.txt"));
		List<String> lines = new ArrayList<String>();
		while (sc.hasNextLine()) {
		  lines.add(sc.nextLine());
		}

		String[] arr = lines.toArray(new String[0]);
		
	}
    
    public void actionPerformed ( ActionEvent e) {


        if ( connected && (e.getSource() == sendBtn || e.getSource() == msg ))
        {
            connectionThread.doSendMessage();
        }
        else if (e.getSource() == connectBtn)
        {
            connectionThread.doManageConnection();
        }
        else if(e.getSource() == username){
            un = username.getText();
        }
        else if(e.getSource() == sendTo){
            recipient = (String)sendTo.getSelectedItem();
        }

    }
}