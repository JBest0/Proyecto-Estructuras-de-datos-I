import java.io.*;
import java.net.*;

public class RemoteMatchProbe {
    public static void main(String[] args) {
        String host = "177.93.11.126";
        int port = 5001;
        long ts = System.currentTimeMillis();
        String userA = "pA" + (ts % 100000);

        System.out.println("Probing LOGIN " + host + ":" + port);

        try {
            Socket s1 = new Socket(host, port);
            s1.setSoTimeout(10000);
            DataOutputStream out1 = new DataOutputStream(s1.getOutputStream());
            DataInputStream in1 = new DataInputStream(s1.getInputStream());
            
            System.out.println("Connected. Sending INICIO...");
            out1.writeUTF("INICIO");
            out1.writeUTF(userA);
            out1.writeUTF("1234");
            out1.flush();
            
            String resp1 = in1.readUTF();
            System.out.println("RESP: " + resp1);
            s1.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
