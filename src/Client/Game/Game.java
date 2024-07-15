package Client.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import Client.Network.Network;

public class Game {
    static Player player;
    static int playerId;
    static Network client;
    static boolean isInitialized = false;

    String[] playerList;
    String word;

    boolean canWrite = false;
    int guesser = 0;

    public Game() {
        System.out.println("Initializating game");

        client = new Network("localhost", 26040);

        while (!isInitialized) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Game initialized");

        //Main game loop
        while (isInitialized) {
            HashMap<String, Object> message = new HashMap<>();
            message.put("tick", true);

            client.sendMessage(message);
            message = client.receiveMessage();

            if ( (boolean) message.get("tock")) {
                playerList = (String[]) message.get("playerList");
    
                guesser = (int) message.get("queue");

                if (guesser == playerId) {
                    canWrite = true;
                } else {
                    canWrite = false;
                }

                String newWord = (String) message.get("word");

                if (newWord != word) {
                    render();
                }

                word = newWord;
            }
            
        }
    }

    private void render() {
        try {
            //clearing console
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            System.out.println("Sesja gry w zgaduj wyraz!");
            System.out.println("Zgadywane słowo: " + word);
            System.out.println("Teraz zgaduje: " + playerList[guesser]);

            if (canWrite) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Twoja kolej! Podaj swój typ: ");

                try {
                    String userInput = reader.readLine();

                    System.out.println("Wytypowałeś: " + userInput);

                    HashMap<String, Object> message = new HashMap<>();
                    message.put("guess", userInput);
                    client.sendMessage(message);
                    
                    message = client.receiveMessage();

                    if (!(boolean) message.get("guess")) {
                        System.out.println("Wytypowano poprawnie: " + userInput);
                    } else {
                        System.out.println("Niestety, brak pasujących znaków dla wejścia: " + userInput);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void initializeGame() {
        HashMap<String, Object> message = new HashMap<>();
        message.put("init", true);
        client.sendMessage(message);

        message = client.receiveMessage();
        playerId = (int) message.get("init");

        if (!message.isEmpty()) {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Enter your name: ");
                String name = reader.readLine();
                player = new Player(name);

                message = new HashMap<>();
                message.put("name", name);
                client.sendMessage(message);

                message = client.receiveMessage();

                if (message.get("name") == player.getName()) {
                    isInitialized = true;
                }
            } catch (Exception e) {
                System.out.println("Failed to initialize game.");
                e.printStackTrace();
            }
        }
    }

}
