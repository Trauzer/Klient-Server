package minigame.numberGenerator.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class server {
    public static void main(String[] args) throws IOException {
        int port = 29090;

        ServerSocket serverSocket = new ServerSocket(port);
        Random random = new Random();
        int numberToGuess = random.nextInt(100); // generates a random number between 0 and 99

        System.out.println("Server is running on port " + port);
        System.out.println("The number to guess is: " + numberToGuess);

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    System.out.println("Client connected: " + socket.getInetAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    while (true) {
                        String guess = in.readLine();
                        int guessedNumber = Integer.parseInt(guess);

                        if (guessedNumber == numberToGuess) {
                            out.println("Congratulations! You've guessed the number.");
                            break;
                        } else {
                            out.println("Sorry, that's not correct. Try again.");
                        }
                    }
                } finally {
                    socket.close();
                    System.out.println("Client disconnected.");
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}