package stream.klient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;

public class klient {


    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 29090);
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String msg = in.readLine();
        JOptionPane.showMessageDialog(null, msg);
        //System.out.println(msg);
        s.close();
        System.exit(0);
    }


}