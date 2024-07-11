package Client.Network;


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Network extends TCPHandler {

    public Network(String serverAddress, int port) {
        super(serverAddress, port);
    }

    public void sendMessage(HashMap<String, Object> message) {
        try {
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(message);
            objectOut.flush();
        } catch (Exception e) {
            System.out.println("Failed to send message to client.");
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> receiveMessage() {
        HashMap<String, Object> message = new HashMap<>();
        try {
            InputStream in = socket.getInputStream();
            ObjectInputStream objectIn = new ObjectInputStream(in);
            message = (HashMap<String, Object>) objectIn.readObject();
        } catch (Exception e) {
            System.out.println("Failed to receive message from client.");
            e.printStackTrace();
        }

        return message;
    }

    public void close() {
        super.close();
    }
}
