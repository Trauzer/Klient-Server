package Server.Network;

import java.io.IOException;
import java.net.Socket;

public class Network extends TCPHandler {

    public Network(int port) {
        super(port);
        listen();
    }

    private void listen() {
        System.out.println("Listening for connections...");
        while (true) {
            try {

                Socket clientSocket = serverSocket.accept();

                new Thread(new Runnable() {
                    
                    public void run() {
                        Server.Game.PlayerMessage.initializePlayer(clientSocket);
                    }
                    
                }).start();

            } catch (IOException e) {
                System.out.println("Failed to accept connection.");
                e.printStackTrace();
            }
        }
    }
}
