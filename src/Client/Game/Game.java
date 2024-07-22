package Client.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

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

    BufferedReader reader;

    public Game() {
        System.out.println("Initializating game");

        String ip = "localhost";
        int port = 26040;

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Podaj adres IP: ");
            String input = reader.readLine();
            String[] inputAddress = input.split(":");

            if (inputAddress.length == 2) {
                ip = inputAddress[0];
                port = Integer.parseInt(inputAddress[1]);
            } else if (!input.isEmpty() && inputAddress.length == 1) {
                ip = input;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        client = new Network(ip, port);

        initializeGame();

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
                    word = newWord;
                    render();
                }

                if ((boolean) message.get("done")) {
                    System.out.println("Koniec gry!");
                    isInitialized = false;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Disconnecting from game...");
        client.close();

        System.exit(0);
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

                    String inputMessage = (String) message.get("guess");

                    if (!inputMessage.equals("false")) {
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

    private static Scanner scanner = new Scanner(System.in);

    public static void initializeGame() {
        HashMap<String, Object> message = new HashMap<>();
        message.put("init", true);
        client.sendMessage(message);

        message = client.receiveMessage();
        playerId = (int) message.get("init");

        if (!message.isEmpty()) {

            System.out.println("Podaj swoje imię: ");
            String name = scanner.nextLine();
            player = new Player(name);

            message = new HashMap<>();
            message.put("name", name);
            client.sendMessage(message);

            message = client.receiveMessage();

            if (message.get("name").equals(player.getName())) {
                isInitialized = true;
            }
        }
    }

}
