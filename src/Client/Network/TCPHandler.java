package Client.Network;

import java.net.Socket;
import java.util.HashMap;

import Client.Game.Game;


public abstract class TCPHandler {
    protected Socket socket;

    public TCPHandler(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);

            System.out.println("Connected to server at " + serverAddress + ":" + port);
        } catch (Exception e) {
            System.out.println("Failed to connect to server at " + serverAddress + ":" + port);
            e.printStackTrace();
        } finally {
            if (socket == null) {
                System.exit(1);
            }
        }
    }

    public void close() {
        try {
            socket.close();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            System.out.println("Failed to close connection.");
            e.printStackTrace();
        }
    }
}
