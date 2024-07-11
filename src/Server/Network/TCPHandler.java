package Server.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

public abstract class TCPHandler {
    private ServerSocket serverSocket;
    private Socket clientSockets[];

    protected TCPHandler(int port, int maxClients) {
        try {
            System.out.println("Starting server on port " + port);
            serverSocket = new ServerSocket(port);
            clientSockets = new Socket[maxClients];
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

    public void listen() {
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    for (int i = 0; i < clientSockets.length; i++) {
                        if (clientSockets[i] == null || clientSockets[i].isClosed()) {
                            clientSockets[i] = serverSocket.accept();
                            int finalI = i;
                            new Thread(() -> handleConnection(clientSockets[finalI])).start();
                            break;
                        }
                    }

                    //Socket clientSocket = serverSocket.accept();
                    //new Thread(() -> handleConnection(clientSocket)).start();
                } catch (IOException e) {
                    System.out.println("Failed to accept client connection.");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void handleConnection(Socket clientSocket) {
        System.out.println("Handling connection from " + clientSocket.getInetAddress());

        // Get message from client
        HashMap<String, Object> nessage = receiveMessage(clientSocket);
        System.out.println("Received message: " + nessage);


        HashMap<String, Object> response = new HashMap<>();
        response.put("type", "response");
        response.put("content", "Hello, client!");

        sendMessage(clientSocket, response);
    }

    public abstract HashMap<String, Object> receiveMessage(Socket clientSocket);

    public abstract void sendMessage(Socket clientSocket, HashMap<String, Object> message);

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
