package chatP2P;

/**
 * Created by MONIK RAJ on 10/10/2016.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

public class userInterface implements Runnable {

    public Thread t = new Thread();

    public socketConnection socket = new socketConnection();

    private int serStatus;
    private int clStatus;
    private int role = 1;//1 for Server and 0 for client
    private String messageString="";


    JFrame mainWindow = new JFrame("ChatBuddy");
    JPanel panel100 = new JPanel();
    JPanel panel200 = new JPanel();
    JPanel panel201 = new JPanel();
    JPanel panel202 = new JPanel();
    JPanel panel203 = new JPanel();

    JButton serStartBut = new JButton();
    JButton serStopBut = new JButton();

    JTextField address = new JTextField();
    JLabel instr = new JLabel();
    JButton connectBut = new JButton();

    JTextPane messageMonitor = new JTextPane();
    JScrollPane msgScroll = new JScrollPane(messageMonitor);
    JTextField writeBox = new JTextField();
    JButton sendBut = new JButton();
    JButton disconnectBut = new JButton();

    public void userInterfaceMain(){

        connectBut.setText("CONNECT");
        address.setMaximumSize(new Dimension(200,25));
        instr.setText("Enter Friend's IP Address");
        serStartBut.setText("Start Listening");
        serStopBut.setText("Stop Listening");
        serStopBut.setEnabled(false);
        panel100.add(instr);
        panel100.add(Box.createRigidArea(new Dimension(20,0)));
        panel100.add(address);
        panel100.add(Box.createRigidArea(new Dimension(0,20)));
        panel100.add(connectBut);
        panel100.add(Box.createRigidArea(new Dimension(0,20)));
        panel100.add(serStartBut);
        panel100.add(Box.createRigidArea(new Dimension(0,20)));
        panel100.add(serStopBut);


        serStartBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    serStartBut.setEnabled(false);
                    serStopBut.setEnabled(true);
                    connectBut.setEnabled(false);
                    address.setEditable(false);
                    Thread y = new Thread(){
                        public void run(){
                            try {
                                serStatus = socket.startServer();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            try {
                                updateWindow(serStatus,1);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    };
                    y.start();
                }

        });

        serStopBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serStopBut.setEnabled(false);
                serStartBut.setEnabled(true);
                connectBut.setEnabled(true);
                address.setEditable(true);
                Thread h = new Thread(){
                  public void run(){
                      try {
                          socket.stopServer();
                      } catch (IOException e1) {
                          e1.printStackTrace();
                      }
                  }
                };
                h.start();
            }
        });

        connectBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serStopBut.setEnabled(false);
                serStartBut.setEnabled(false);
                address.setEditable(false);
                try {
                    socket.stopServer();
                    clStatus = socket.startClient(address.getText());
                    updateWindow(clStatus,0);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                serStopBut.setEnabled(true);
                serStartBut.setEnabled(true);
                address.setEditable(true);
            }
        });
        panel100.setLayout(new BoxLayout(panel100,BoxLayout.X_AXIS));

        //messageMonitor.setMaximumSize(new Dimension(550,650));
        messageMonitor.setEditable(false);
        messageMonitor.setText("");
        msgScroll.setPreferredSize(new Dimension(450,550));
        msgScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //panel201.add(messageMonitor);
        panel201.add(msgScroll);
        panel201.setLayout(new BoxLayout(panel201,BoxLayout.LINE_AXIS));

        writeBox.setMaximumSize(new Dimension(380,25));
        writeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strW = "";
                try {
                    strW = socket.sendData(writeBox.getText());
                    messageString = "YOU : "+ strW + "\n" + messageString ;
                    messageMonitor.setVisible(false);
                    messageMonitor.setText(messageString);
                    messageMonitor.setVisible(true);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                writeBox.setText("");
                writeBox.setVisible(false);
                writeBox.setVisible(true);
            }
        });
        sendBut.setText("SEND");
        sendBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strW = "";
                try {
                    strW = socket.sendData(writeBox.getText());
                    messageString = "YOU : "+ strW + "\n" + messageString;
                    messageMonitor.setVisible(false);
                    messageMonitor.setText(messageString);
                    messageMonitor.setVisible(true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                writeBox.setText("");
                writeBox.setVisible(false);
                writeBox.setVisible(true);
            }
        });
        panel202.add(writeBox);
        panel202.add(Box.createRigidArea(new Dimension(20,0)));
        panel202.add(sendBut);
        panel202.add(Box.createRigidArea(new Dimension(20,0)));
        panel202.add(sendBut);
        panel202.setLayout(new BoxLayout(panel202,BoxLayout.X_AXIS));

        disconnectBut.setText("Disconnect");
        disconnectBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(role==1){
                    try {
                        String h = socket.sendData("COMMUNICATION ENDED BY THE FRIEND");
                        socket.stopServerListener();
                        updateWindowOld();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if(role==0){
                    try {
                        String h = socket.sendData("COMMUNICATION ENDED BY THE FRIEND");
                        socket.stopClient();
                        updateWindowOld();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        panel203.add(disconnectBut);
        panel203.setLayout(new BoxLayout(panel203,BoxLayout.LINE_AXIS));

        panel200.add(panel201);
        panel200.add(Box.createRigidArea(new Dimension(0,30)));
        panel200.add(panel202);
        panel200.add(Box.createRigidArea(new Dimension(0,30)));
        panel200.add(panel203);
        panel200.setLayout(new BoxLayout(panel200,BoxLayout.Y_AXIS));

        mainWindow.setResizable(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(600,700);
        mainWindow.add(Box.createRigidArea(new Dimension(0,200)));
        mainWindow.add(panel100);
        mainWindow.setLayout(new BoxLayout(mainWindow.getContentPane(),BoxLayout.Y_AXIS));
        mainWindow.setVisible(true);
        System.out.println("User Interface created..");


    }

    @Override
    public void run() {
        userInterfaceMain();
    }

    public void start(){
        System.out.println("User Interface started..");
        if(t==null){
            t = new Thread(this);
            t.start();
        }
        else{
            t.start();
        }
    }

    public void updateWindow(int u, int d) throws InterruptedException {
        if(u==200){
            mainWindow.setVisible(false);
            mainWindow.remove(panel100);
            mainWindow.add(panel200);
            try {
                socket.sendData("HI. I AM YOUR FRIEND "+InetAddress.getLocalHost().getHostName()+" AND MY IP ADDRESS IS "+ InetAddress.getLocalHost().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainWindow.setVisible(true);
            role = d;
            startReadingMessage();
        }

    }

    public void updateWindowOld(){
        mainWindow.setVisible(false);
        mainWindow.remove(panel200);
        mainWindow.add(panel100);
        connectBut.setEnabled(true);
        address.setEnabled(true);
        serStartBut.setEnabled(true);
        serStopBut.setEnabled(false);
        mainWindow.setVisible(true);

    }

    public void startReadingMessage(){
        Thread g = new Thread(){
          public void run(){
              while(socket.readingOK==1){
                  try {
                      socket.receiveData();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  if(socket.readDone==0){
                      messageString = "FRIEND : " + socket.strRead + "\n" + messageString;
                      socket.readDone = 1;
                      messageMonitor.setVisible(false);
                      messageMonitor.setText(messageString);
                      messageMonitor.setVisible(true);
                  }
              }
          }
        };
        g.start();
    }
}
