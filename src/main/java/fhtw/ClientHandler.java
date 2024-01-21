package fhtw;


import com.fasterxml.jackson.core.JsonProcessingException;
import fhtw.chat.PrivateChat;
import fhtw.data.DatabaseHandler;
import fhtw.data.ValidationController;
import fhtw.message.PrivateChatMessage;
import fhtw.user.User;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles client interactions for the chat application.
 * <p>
 * This class manages communication with individual clients connected to the server.
 * It processes incoming messages, handles user registration and login, message sending,
 * and other user actions, maintaining the connection and state for each client.
 */
@Getter
public class ClientHandler extends Thread {

    private ArrayList<ClientHandler> clients;
    private Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private String username;

    /**
     * Constructs a new ClientHandler for handling communication with a client.
     *
     * @param socket  the socket representing the connection to the client
     * @param clients the list of currently connected clients
     */
    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream((socket.getInputStream()));
            this.clients = clients;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listens for messages from the client and processes them.
     * Handles different types of requests based on the message header.
     */
    @Override
    public void run() {

        try {
            String message;
            while (true) {
                message = (String) reader.readObject();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                String header = getHeader(message);
                String body = getBody(message);
                handleMethod(header, body);
            }
            {

            }
        } catch (IOException e) {
            System.out.println("User disconnected");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            clients.remove(this);
            try {
                DatabaseHandler.getRegisteredUsers().get(username).setOnlineStatus(false);
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Processes the request from the client based on the header.
     * Performs actions such as user registration, login, user search, message sending, and initializing data.
     *
     * @param header the header indicating the type of request
     * @param body   the content of the request
     * @throws IOException if an I/O error occurs while handling the request
     */
    public void handleMethod(String header, String body) throws IOException {
        User user;

        switch (header) {
            case "register":
                user = User.fromJson(body);
                user.setPrivateChats(); //GANZ WICHTIG, SONST CRASHED ALLES
                if (ValidationController.registerNewUser(user)) {
                    DatabaseHandler.getRegisteredUsers().put(user.getUsername(), user);
                    System.out.println("User with Name " + user.getUsername() + " registered");
                    this.writer.writeObject(true);
                } else {
                    this.writer.writeObject(false);
                }
                System.out.println(DatabaseHandler.getRegisteredUsers());
                break;

            case "login":
                user = User.fromJson(body);
                System.out.println(user);
                if (ValidationController.checkLogin(user)) {
                    System.out.println("User with Name " + user.getUsername() + " logged in");
                    this.username = user.getUsername();
                    //System.out.println(this.username); //for testing
                    DatabaseHandler.getRegisteredUsers().get(username).setOnlineStatus(true);
                    this.writer.writeObject(true);
                } else {
                    this.writer.writeObject(false);
                }
                break;

            case "addFriend":
                String friendUsername = body;
                if (PrivateChat.addUser(this.username, friendUsername)) {
                    this.writer.writeObject(true);
                } else {
                    this.writer.writeObject(false);
                }
                break;

            case "sendMessage":
                try {
                    if (PrivateChat.sendMessage(PrivateChatMessage.fromJson(body))) {
                        this.writer.writeObject(true);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;

            case "initData":
                //System.out.println(this.username); //for testing
                List<PrivateChat> userChats = DatabaseHandler.getRegisteredUsers().get(this.username).getPrivateChats();
                PrivateChat.setOnlineForList(userChats);
                //System.out.println(DatabaseHandler.getPrivateChats());    //for testing
                this.writer.writeObject(PrivateChat.convertSetToJson(userChats));
                break;

            default:
                this.writer.writeObject(true);
                break;
        }
    }

    /**
     * Extracts the header from a JSON string.
     *
     * @param json the JSON string containing the header and body
     * @return the header part of the JSON string
     */
    public String getHeader(String json) {
        int semicolonIndex = json.indexOf(';');
        if (semicolonIndex != -1) {
            return json.substring(0, semicolonIndex);
        } else {
            return json;
        }
    }

    /**
     * Extracts the body from a JSON string.
     *
     * @param json the JSON string containing the header and body
     * @return the body part of the JSON string
     */
    public String getBody(String json) {
        int semicolonIndex = json.indexOf(';');
        if (semicolonIndex != -1) {
            return json.substring(semicolonIndex + 1);
        } else {
            return "";
        }
    }
}


