package Server.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

class TCPHandler {
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
        String message = receiveMessage(clientSocket);
        System.out.println("Received message: " + message);

        sendMessage(clientSocket, "Hello, client!");
    }

    private String receiveMessage(Socket clientSocket) {
        try {
            // Read message from serialization package MessagePack
            MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(clientSocket.getInputStream());
            if (unpacker.hasNext()) {
                String message = unpacker.unpackString();
                return message;
            }
            return null;
        } catch (IOException e) {
            System.out.println("Failed to receive message from client.");
            e.printStackTrace();
            return null;
        }
    }

    private void sendMessage(Socket clientSocket, String message) {
        if (clientSocket == null || clientSocket.isClosed()) {
            System.out.println("Client socket is closed.");
            return;
        }

        try {
            // Send message using serialization package MessagePack
            byte[] packedMessage;
            try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
                packer.packString(message);
                packedMessage = packer.toByteArray();
            }
            clientSocket.getOutputStream().write(packedMessage);
            System.out.println("Sent message: " + message);
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
