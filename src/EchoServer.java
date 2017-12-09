import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EchoServer extends JFrame {

    // GUI items
    JButton ssButton;
    JLabel machineInfo;
    JLabel portInfo;
    JTextArea history;
    private boolean running;

    // Network Items
    boolean serverContinue;
    ServerSocket serverSocket;
    Vector <PrintWriter> outStreamList;
    int clientNum;
    final Lock lock;
    Hashtable<Integer,PrintWriter> clients;
    Hashtable<String,String> clientsNames;

    // set up GUI
    public EchoServer()
    {
        super( "Echo Server" );

        lock = new ReentrantLock(true);
        clientNum = 0;
        clients = new Hashtable<Integer,PrintWriter>();
        clientsNames = new Hashtable<>();

        // set up the shared outStreamList
        outStreamList = new Vector<PrintWriter>();

        // get content pane and set its layout
        JFrame container = new JFrame();

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(new GridLayout(1,3));
        //container.setSize(700, 700);
        //container.setLayout( new FlowLayout() );

        // create buttons
        running = false;
        ssButton = new JButton( "Start Listening" );
        ssButton.addActionListener( e -> doButton (e) );
        panel1.add( ssButton );

        String machineAddress = null;
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            machineAddress = addr.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            machineAddress = "127.0.0.1";
        }
        machineInfo = new JLabel (machineAddress);
        panel1.add(machineInfo );
        portInfo = new JLabel (" Not Listening ");
        panel1.add( portInfo );

        history = new JTextArea ( 10, 40 );
        history.setEditable(false);
        panel2.add( new JScrollPane(history));

        WindowListener exit1 = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        container.addWindowListener(exit1);

        container.getContentPane().add(panel1, BorderLayout.NORTH);
        container.getContentPane().add(panel2, BorderLayout.SOUTH);
        container.setSize(new Dimension(500,250));
        container.setLocationRelativeTo(null);
        container.setVisible(true);
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } // end CountDown constructor


    // handle button event
    public void doButton( ActionEvent event )
    {
        if (running == false)
        {
            new ConnectionThread (this);
        }
        else
        {
            serverContinue = false;
            ssButton.setText ("Start Listening");
            portInfo.setText (" Not Listening ");
        }
    }


} // end class EchoServer4













