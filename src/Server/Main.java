package Server;

import Server.Network.Network;

public class Main {
    public static void main(String[] args) {
        Network server = new Network(26040);

        server.listen();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.close();
            System.out.println("Server shut down successfully.");
        }));
    }
}
