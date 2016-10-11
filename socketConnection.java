package chatP2P;

/**
 * Created by MONIK RAJ on 10/10/2016.
 */
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;

public class socketConnection{
    private Thread t = new Thread();
    private ServerSocket server;
    private Socket client;
    private Socket serverListen;
    private int connectionNumber = 0;
    private int check = 0;
    private int role = -1;//1 for Server and 0 for Client -1 for dead state
    public String strRead="";
    public int readDone = 1;
    public int readingOK = 0;

    private DataOutputStream serverOP;
    private DataOutputStream clientOP;
    private DataInputStream serverIP;
    private DataInputStream clientIP;

    public int startServer() throws IOException{
        check=0;
        server = new ServerSocket(7500);
        int utrr = 0;
        while(connectionNumber==0 && check==0) {
            System.out.println("StartServer Listening at 7500");
            try {
                serverListen = server.accept();
                connectionNumber=1;
                utrr = 200;
                serverIP = new DataInputStream(serverListen.getInputStream());
                serverOP = new DataOutputStream(serverListen.getOutputStream());
                role=1;
                readingOK=1;
            } catch (IOException e2) {
                System.out.println(e2);
                utrr = 100;
            }
        }
       return utrr;
    }

    public void stopServer() throws IOException {
        check=1;
        connectionNumber=0;
        System.out.println("StopServer");
        try{
            server.close();
            role=-1;
            readingOK=0;
        }
        catch (IOException e3){
            System.out.println(e3);
        }
    }

    public void stopServerListener() throws IOException{
        connectionNumber=0;
        System.out.println("Stopping Listener at Server Side");
        try{
            serverListen.close();
            role=-1;
            serverIP.close();
            serverOP.close();
            server.close();//Latest change made
            readingOK=0;
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public int startClient(String address) throws IOException{
        connectionNumber=0;
        int dree = 0;
        try{
            client = new Socket(address,7500);
            dree = 200;
            connectionNumber=1;
            clientIP = new DataInputStream(client.getInputStream());
            clientOP = new DataOutputStream(client.getOutputStream());
            role=0;
            readingOK=1;
        }
        catch (IOException e){
            System.out.println(e);
            dree = 100;
            check=0;
        }
        return dree;
    }

    public void stopClient() throws IOException{
        connectionNumber=0;
        try{
            client.close();
            role=-1;
            clientIP.close();
            clientOP.close();
            readingOK=0;
        }
        catch (IOException e1){
            System.out.println(e1);
        }
    }

    public String sendData(String s) throws IOException {
        int sucW = 0;
        if(role==0){
            try{
                clientOP.writeUTF(s);
                sucW=1;
            }
            catch (IOException e){
                System.out.println(e);
            }
        }
        if(role==1){
            try{
                serverOP.writeUTF(s);
                sucW=1;
            }
            catch (IOException e){
                System.out.println(e);
            }
        }
        if(sucW==1){
            return s;
        }
        else
        {
            return "";
        }
    }

    public void receiveData() throws IOException{

        strRead="";
        if(role==0){
            try{
                        if (clientIP.available()>0) {
                        strRead=clientIP.readUTF();
                        readDone = 0;
                    }

            }
            catch (IOException e){
                System.out.println(e);
            }
        }
        if(role==1){
            try{

                    if (serverIP.available()>0) {
                        strRead=serverIP.readUTF();
                        readDone = 0;
                    }

            }
            catch (IOException e){
                System.out.println(e);
            }
        }
    }

}
