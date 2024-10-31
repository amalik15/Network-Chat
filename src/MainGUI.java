import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

public class MainGUI extends JFrame{
    JButton serverBtn;
    JButton clientBtn;
    Container menu;
    JPanel btnArea;
    String username;
    String IP;
    String Port;
    ClientGUI cGUI;

    public MainGUI() {
        super ( "RSA Encrypted Chat (Choose your destiny)" );
        menu = getContentPane();
        menu.setLayout(new BorderLayout());

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        btnArea = new JPanel(new GridLayout (1,2));
        serverBtn = new JButton("Server");
        clientBtn = new JButton("Client");
        btnArea.add(serverBtn);
        btnArea.add(clientBtn);
        menu.add(btnArea, BorderLayout.CENTER);
        //ActionListeners
        serverBtn.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new EchoServer();
                }
            }
        );

        clientBtn.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new ClientGUI();
                }
            }//end of new actionlistener()
        );

        //Menu bar
        JMenuItem About = new JMenuItem("About");
        About.setMnemonic('A');
        About.addActionListener(
            new ActionListener() {
                public void actionPerformed( ActionEvent e )
                {
                    JOptionPane.showMessageDialog(
                            MainGUI.this,"This program was designed by:\namalik15\n"
                                        + "dqiao4",
                                "About Us", JOptionPane.PLAIN_MESSAGE );
                    }
                }//end of actionListener
        );

        JMenu Help = new JMenu("Help");
        Help.setMnemonic( 'H' );

        JMenuItem servHelp = new JMenuItem("Server Help");
        servHelp.setMnemonic( 'S' );
        Help.add( servHelp);
        servHelp.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(
                            MainGUI.this,
                            "Choose to be Server or Client\n\n" +
                                    "Server will host the chat, allowing Clients to connect.\n" +
                                    "(Note: Server is required to run FIRST, then Clients will\n" +
                                    "be able to connect!)",
                            "Server-side Aid", JOptionPane.PLAIN_MESSAGE );
                }
            }//end of actionListener
        );

        JMenuItem clientHelp = new JMenuItem("Client Help");
        clientHelp.setMnemonic( 'C' );
        Help.add(clientHelp);
        clientHelp.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(
                            MainGUI.this,
                            "Choose to be Server or Client\n\n" +
                                    "Client will need to enter username, and Server's IP and Port\n" +
                                    "before being connected. As Client, choose which recipient to\n" +
                                    "send messages to, then type away.",
                            "Client-side Aid", JOptionPane.PLAIN_MESSAGE );
                }
            }//end of actionListener
        );

        WindowListener exit = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };

        JMenuBar bar = new JMenuBar();
        setJMenuBar( bar );
        bar.add( Help );
        bar.add( About );

        setSize(420,400);
        setVisible(true);
    }

    public static void main(String[] jargs) {
        MainGUI app = new MainGUI();
    }
}
