package Server.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class TCPHandler {
    private ServerSocket serverSocket;

    protected TCPHandler(int port) {
        try {
            System.out.println("Starting server on port " + port);
            serverSocket = new ServerSocket(port);
            System.out.println("Server started!");
        } catch (IOException e) {
            System.out.println("Failed to start server on port " + port);
            e.printStackTrace();
        }
    }

    public void listen() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    // Listen for new connections
                    System.out.println("Listening for new connections...");
                    Socket clientSocket = serverSocket.accept();
                    handleConnection(clientSocket);
                }
            } catch (IOException e) {
                System.out.println("Failed to accept connection");
                e.printStackTrace();
            }
        }).start();
    }

    protected void handleConnection(Socket clientSocket) {
        System.out.println("Handling connection from " + clientSocket.getInetAddress());

        // Get message from client
        String message = receiveMessage(clientSocket);
        System.out.println("Received message: " + message);

        // Send response to client
        sendMessage(clientSocket, "Hello, client!");
    }

    private String receiveMessage(Socket clientSocket) {
        try {
            return new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).readLine();
        } catch (IOException e) {
            System.out.println("Failed to receive message from client.");
            e.printStackTrace();
            return null;
        }
    }

    private void sendMessage(Socket clientSocket, String message) {
        try {
            new PrintWriter(clientSocket.getOutputStream(), true).println(message);
        } catch (IOException e) {
            System.out.println("Failed to send message to client.");
            e.printStackTrace();
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
