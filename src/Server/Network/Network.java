package Server.Network;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Network extends TCPHandler {

    public Network(int port) {
        super(port, 2);
    }

    public HashMap<String, Object> receiveMessage(Socket clientSocket) {
        HashMap<String, Object> message = new HashMap<>();
        try {
            InputStream in = clientSocket.getInputStream();
            ObjectInputStream objectIn = new ObjectInputStream(in);
            message = (HashMap<String, Object>) objectIn.readObject();
        } catch (Exception e) {
            System.out.println("Failed to receive message from client.");
            e.printStackTrace();
        }

        return message;
    }

    public void sendMessage(Socket clientSocket, HashMap<String, Object> message) {
        try {
            OutputStream out = clientSocket.getOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(message);
            objectOut.flush();
        } catch (Exception e) {
            System.out.println("Failed to send message to client.");
            e.printStackTrace();
        }
    }
}
