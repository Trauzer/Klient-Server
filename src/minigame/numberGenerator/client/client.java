package minigame.numberGenerator.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 29090);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                System.out.println("Enter a number to guess: ");
                String guess = userInput.readLine();
                out.println(guess);

                String response = in.readLine();
                System.out.println("Server response: " + response);

                if (response.contains("Congratulations")) {
                    break;
                }
            }
        } finally {
            socket.close();
        }
    }
}