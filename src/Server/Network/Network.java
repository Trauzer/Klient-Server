package Server.Network;

import java.io.IOException;
import java.net.Socket;
import Server.Game.ServerState;

public class Network extends TCPHandler {

    public Network(int port) {
        super(port);

        new ServerState();
    }

    public void listen() {
        System.out.println("Listening for connections...");
        while (true) {
            try {

                Socket clientSocket = serverSocket.accept();

                new Thread(new Runnable() {
                    
                    public void run() {
                        System.out.println("Connected with client: " + clientSocket.getInetAddress());

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
