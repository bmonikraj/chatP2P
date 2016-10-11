package chatP2P;

/**
 * Created by MONIK RAJ on 10/10/2016.
 */
import java.net.*;
public class chatMain {
    public static void main(String args[]) throws InterruptedException, UnknownHostException {
        System.out.println("Chat Application Started");
        userInterface userInterfaceObj = new userInterface();
        Thread t = new Thread(userInterfaceObj);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
