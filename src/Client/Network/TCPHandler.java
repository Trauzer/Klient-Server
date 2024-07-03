package Client.Network;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.net.Socket;

public class TCPHandler {
    private Socket socket;
    private MessageBufferPacker packer;
    private MessageUnpacker unpacker;

    public TCPHandler(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            packer = MessagePack.newDefaultBufferPacker();
            unpacker = MessagePack.newDefaultUnpacker(socket.getInputStream());
            System.out.println("Connected to server at " + serverAddress + ":" + port);
        } catch (Exception e) {
            System.out.println("Failed to connect to server at " + serverAddress + ":" + port);
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            packer.packString(message);
            byte[] packedMessage = packer.toByteArray();
            packer.clear();
            socket.getOutputStream().write(packedMessage);
            System.out.println("Sent message: " + message);
        } catch (Exception e) {
            System.out.println("Failed to send message to server.");
            e.printStackTrace();
        }
    }

    public String receiveMessage() {

        try {
            int counter = 0;

            while (socket.getInputStream().available() == 0) {
                Thread.sleep(100);
                counter++;

                if (counter > 10) {
                    System.out.println("No message received from server.");
                    return null;
                }
            }

            if (unpacker.hasNext()) {
                String message = unpacker.unpackString();
                System.out.println("Received message: " + message);
                return message;
            }

            return null;
        } catch (Exception e) {
            System.out.println("Failed to receive message from server.");
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            unpacker.close();
            packer.close();
            socket.close();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            System.out.println("Failed to close connection.");
            e.printStackTrace();
        }
    }
}
