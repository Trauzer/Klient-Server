package Client.Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPHandler(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to server at " + serverAddress + ":" + port);
        } catch (Exception e) {
            System.out.println("Failed to connect to server at " + serverAddress + ":" + port);
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (Exception e) {
            System.out.println("Failed to receive message from server.");
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            System.out.println("Failed to close connection.");
            e.printStackTrace();
        }
    }
}
