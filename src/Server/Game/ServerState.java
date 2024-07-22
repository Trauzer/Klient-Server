package Server.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ServerState {
    public static String displayWord;
    public static String matchWord;
    public static int playerNumbers = 0;
    public static String[] playerList = new String[100];
    public static int currentPlayer = 1;

    public ServerState() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Utwórz wyraz do zgadnięcia");
            String input = reader.readLine();
            System.out.println("Utworzyłeś nowe hasło: " + input);

            ServerState.setMatchWord(input);

        } catch (IOException e) {

        }
    }

    public static void addPlayerNumbers() {
        playerNumbers++;
    }

    public static void decreasePlayerNumbers() {
        playerNumbers--;
    }

    public static int getPlayerNumbers() {
        return playerNumbers;
    }

    public static String[] getPlayerList() {
        return playerList;
    }

    public static void addPlayerToList(String name) {
        playerList[playerNumbers] = name;
    }

    public static int getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(int player) {
        if (playerNumbers >= player) {
            currentPlayer = player;
        }
    }

    public static int goToNextPlayer() {
        int playerId = currentPlayer + 1;

        if (playerNumbers >= playerId) {
            setCurrentPlayer(playerId);
            return playerId;
        }

        setCurrentPlayer(1);
        return 1;
    }

    public static void removePlayerFromList(String name) {
        System.out.println("Removing player: " + name);
        for (int i = 0; i < playerNumbers; i++) {
            if (playerList[i] == null) {
                continue;
            }

            if (playerList[i].equals(name)) {
                playerList[i] = null;
            }
        }
    }

    public static String getGuessedWord() {
        return displayWord;
    }

    public static boolean checkLetters(String input) {
        boolean isGuessed = false;

        if (input.length() == 1) {
            for (int i = 0; i < matchWord.length(); i++) {
                if (matchWord.charAt(i) == input.charAt(0)) {
                    displayWord = displayWord.substring(0, i) + input + displayWord.substring(i + 1);
                    isGuessed = true;
                }
            }
        } else {
            if (matchWord.equals(input)) {
                displayWord = input;
                isGuessed = true;
            }
        }

        return isGuessed;
    }

    public static boolean isGameDone() {
        return matchWord.equals(displayWord);
    }

    public static void setMatchWord(String word) {
        matchWord = word;
        displayWord = "";
        
        for (int i = 0; i < matchWord.length(); i++) {
            if (matchWord.charAt(i) == ' ') {
                displayWord += " ";
            } else {
                displayWord += "*";
            }
        }
    }
}
