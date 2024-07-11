package Client;

import Client.Network.Network;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Network client = new Network("localhost", 26040);

        HashMap<String, Object> message = new HashMap<>();
        message.put("type", "message");
        message.put("content", "Hello, server!");

        client.sendMessage(message);

        System.out.println("Server response: " + client.receiveMessage());

        client.close();
    }
}
