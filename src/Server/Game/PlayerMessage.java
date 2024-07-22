package Server.Game;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class PlayerMessage {
    Socket playerSocket;
    String playerName;
    int playerId;

    OutputStream out;
    InputStream in;

    boolean loop = true;

    private PlayerMessage(Socket playerSocket) {
        this.playerSocket = playerSocket;


        while (loop) {
            HashMap<String, Object> clientMessage;
            HashMap<String, Object> serverMessage;

            clientMessage = recieveMessage();
            
            serverMessage = handleData(clientMessage);

            sendMessage(serverMessage);
        }

        try {
            playerSocket.close();
            System.out.println("Connection closed with client: " + playerSocket.getInetAddress());
        } catch (Exception e) {
            System.out.println("Failed to close connection with client: " + playerSocket.getInetAddress());
            e.printStackTrace();
        }
    }

    public static void initializePlayer(Socket playerSocket) {
        new PlayerMessage(playerSocket);
    }

    private void sendMessage(HashMap<String, Object> message) {
        try {
            out = playerSocket.getOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(message);
            objectOut.flush();

            System.out.println("Sent message to client: " + playerSocket.getInetAddress());
        } catch (Exception e) {
            System.out.println("Failed to send message to client: " + playerSocket.getInetAddress());
            e.printStackTrace();
        }
    }

    private HashMap<String, Object> recieveMessage() {
        HashMap<String, Object> message;

        try {
            in = playerSocket.getInputStream();
            ObjectInputStream objectIn = new ObjectInputStream(in);

            message = (HashMap<String, Object>) objectIn.readObject();

            System.out.println("Recieve message from client: " + playerSocket.getInetAddress());
            return message;
        } catch (Exception e) {
            System.out.println("Failed to receive message from client " + playerSocket.getInetAddress());
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, Object> handleData(HashMap<String, Object> data) {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        
        data.forEach((key, value) -> {
            switch (key) {
                case "init":
                    ServerState.addPlayerNumbers();
                    playerId = ServerState.getPlayerNumbers();

                    returnData.put("init", playerId);
                    break;
                case "name":
                    playerName = (String) value;
                    ServerState.addPlayerToList(playerName);

                    returnData.put("name", playerName);
                    break;
                case "disconnect":
                    ServerState.decreasePlayerNumbers();
                    ServerState.removePlayerFromList(playerName);

                    returnData.put("disconnect", "true");

                    loop = false;
                    break;
                case "guess":
                    if (playerId == ServerState.getCurrentPlayer()) {
                        
                        boolean isGuessed = ServerState.checkLetters((String) value);

                        if (!isGuessed) {
                            ServerState.goToNextPlayer();

                            returnData.put("guess", "false");
                        } else {
                            returnData.put("guess", ServerState.getGuessedWord());
                        }

                        System.out.println(ServerState.getCurrentPlayer());
                    } else {
                        returnData.put("notAllowed", "Poczekaj na swoją kolej!");
                    }
                    break;
                case "tick":
                    returnData.put("tock", true);

                    returnData.put("word", ServerState.getGuessedWord());
                    returnData.put("queue", ServerState.getCurrentPlayer());
                    returnData.put("playerList", ServerState.getPlayerList());
                    returnData.put("done", ServerState.isGameDone());
                    
                    break;
                case "word":
                    returnData.put("word", ServerState.getGuessedWord());
                    break;
                default:
                    break;
            }
        });

        //if (!true) {
            System.out.println(returnData);
        //}

        return returnData;
    }

}