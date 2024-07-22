package Server.Network;

import java.io.IOException;
import java.net.ServerSocket;


public abstract class TCPHandler {
    protected ServerSocket serverSocket;

    protected TCPHandler(int port) {
        try {
            System.out.println("Starting server on port " + port);
            serverSocket = new ServerSocket(port);
            System.out.println("Server started!");
        } catch (IOException e) {
            System.out.println("Failed to start server on port " + port);
            e.printStackTrace();
        } finally {
            if (serverSocket == null) {
                System.exit(1);
            }
        }
    }

    public void close() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server socket closed.");
            }
        } catch (IOException e) {
            System.out.println("Failed to close server socket.");
            e.printStackTrace();
        }
    }



}
