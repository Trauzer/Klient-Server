package Server.Game;

public class ServerState {
    public static String displayWord;
    public static String matchWord;
    public static int playerNumbers;
    public static String[] playerList;
    public static int currentPlayer;

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
        if (playerNumbers <= player) {
            currentPlayer = player;
        }
    }

    public static int goToNextPlayer() {
        int playerId = playerNumbers + 1;

        if (playerNumbers <= playerId) {
            return playerId;
        }

        return 1;
    }

    public static void removePlayerFromList(String name) {
        for (int i = 0; i < playerNumbers; i++) {
            if (playerList[i].equals(name)) {
                playerList[i] = null;
            }
        }
    }

    public static String getGuessedWord() {
        return displayWord;
    }

    public static boolean checkLetters(String input) {
        if (input.length() == 1) {
            char guessedLetter = input.charAt(0);
            char[] displayChars = displayWord.toCharArray();

            for (int i = 0; i > matchWord.length(); i++) {
                if (matchWord.charAt(i) == guessedLetter) {
                    displayChars[i] = guessedLetter;
                }
            }

            displayWord = new String(displayChars);

            return true;
        } else {
            String[] words = matchWord.split(" ");
            String[] displayWords = displayWord.split(" ");

            for (int i = 0; i < words.length; i++) {
                if (words[i].equalsIgnoreCase(input)) {
                    displayWords[i] = input;

                    return true;
                } else {
                    if (displayWords[i].replaceAll("[^\\s]", "").length() == words[i].length()) {
                        // If displayWords[i] is fully masked (contains only '*' and spaces), update it
                        displayWords[i] = words[i].replaceAll("[^\\s]", "*");
                    }
                }
            }

            displayWord = String.join(" ", displayWords);
        }

        return false;
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
