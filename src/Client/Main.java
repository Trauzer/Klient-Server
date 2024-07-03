package Client;

import Client.Network.Network;

public class Main {

    public static void main(String[] args) {
        Network client = new Network("localhost", 26040);

        client.sendMessage("Hello, server!");

        System.out.println("Server response: " + client.receiveMessage());

        client.close();
    }
}
