package Server.Network;

import java.net.Socket;

public class Network extends TCPHandler {

    public Network(int port) {
        super(port, 2);
    }

    //public void handleConnection(Socket clientSocket) {
        //super.handleConnection(clientSocket);
    //}
}
