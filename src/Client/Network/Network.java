package Client.Network;

public class Network extends TCPHandler {

    public Network(String serverAddress, int port) {
        super(serverAddress, port);
    }

    public void sendMessage(String message) {
        super.sendMessage(message);
    }

    public String receiveMessage() {
        return super.receiveMessage();
    }

    public void close() {
        super.close();
    }
}
