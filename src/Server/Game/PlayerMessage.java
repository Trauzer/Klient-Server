package Server.Game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class PlayerMessage {
    Socket playerSocket;
    String playerName;
    int playerId;

    ObjectOutputStream out;
    ObjectInputStream in;

    private PlayerMessage(Socket playerSocket) {
        this.playerSocket = playerSocket;


        while (true) {
            HashMap<String, Object> clientMessage;
            HashMap<String, Object> serverMessage;

            clientMessage = recieveMessage();
            
            serverMessage = handleData(clientMessage);

            sendMessage(serverMessage);
        }
    }

    public static void initializePlayer(Socket playerSocket) {
        new PlayerMessage(playerSocket);
    }

    private void sendMessage(HashMap<String, Object> message) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteStream);
            out.writeObject(message);
            out.flush();

            byte[] sendBuffer = byteStream.toByteArray();
            playerSocket.getOutputStream().write(sendBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Object> recieveMessage() {
        HashMap<String, Object> message;

        try {
            byte[] receiveBuffer = new byte[1024];
            int bytesRead = playerSocket.getInputStream().read(receiveBuffer);

            ByteArrayInputStream byteStream = new ByteArrayInputStream(receiveBuffer, 0, bytesRead);
            in = new ObjectInputStream(byteStream);

            message = (HashMap<String, Object>) in.readObject();
            return message;
        } catch (Exception e) {
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
                    break;
                case "guess":
                    if (playerId == ServerState.getCurrentPlayer()) {
                        
                        boolean isGuessed = ServerState.checkLetters((String) value);

                        if (!isGuessed) {
                            ServerState.goToNextPlayer();

                            returnData.put("guess", false);
                        }

                        returnData.put("guess", ServerState.getGuessedWord());
                    }

                    returnData.put("notAllowed", "Poczekaj na swoją kolej!");
                    break;
                case "tick":
                    returnData.put("tock", true);

                    returnData.put("word", ServerState.getGuessedWord());
                    returnData.put("queue", ServerState.getCurrentPlayer());
                    returnData.put("playerList", ServerState.getPlayerList());
                    
                    break;
                case "word":
                    returnData.put("word", ServerState.getGuessedWord());
                    break;
                default:
                    break;
            }
        });

        return returnData;
    }

}