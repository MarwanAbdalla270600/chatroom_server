package fhtw;


import fhtw.chat.PrivateChat;
import fhtw.data.DatabaseHandler;
import fhtw.data.ValidationController;
import fhtw.user.User;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Getter
public class ClientHandler extends Thread {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private ArrayList<ClientHandler> clients;

    private Socket socket;

    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    private String username;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream((socket.getInputStream()));
            this.clients = clients;
            logger.info("ClientHandler initialized for socket: " + socket);

        } catch (IOException e) {
            logger.severe("Error initializing ClientHandler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            String message;
            while (true) {
                message = (String) reader.readObject();
                logger.info("Received message: " + message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                String header = getHeader(message);
                String body = getBody(message);
                handleMethod(header, body);
            }
            User user = DatabaseHandler.getRegisteredUsers().get(username);
            if (user != null) {
                user.setOnlineStatus(false);
            } else {
                logger.warning("User not found for username: " + username);
            }
        } catch (IOException e) {
            logger.warning("User disconnected or I/O error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.severe("Class not found: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            clients.remove(this);
            try {
                User user = DatabaseHandler.getRegisteredUsers().get(username);
                if (user != null) {
                    user.setOnlineStatus(false);
                } else {
                    logger.warning("User not found for username in finally block: " + username);
                }
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                logger.severe("Unexpected error in finally block: " + e.getMessage());
            }
        }

    }


    public void handleMethod(String header, String body) throws IOException {
        User user;
        switch (header) {
            case "register":
                user = User.fromJson(body);
                logger.info("User parsed from JSON: " + user);

                if (user == null) {
                    logger.warning("Parsed user is null after JSON conversion.");
                    this.writer.writeObject(false);
                    break;
                }

                if (user.getPrivateChats() == null) {
                    user.setPrivateChats(); // Initialize privateChats
                }

                if (ValidationController.registerNewUser(user)) {
                    DatabaseHandler.getRegisteredUsers().put(user.getUsername(), user);
                    logger.info("User successfully registered: " + user.getUsername());
                    this.writer.writeObject(true);
                } else {
                    logger.warning("Failed to register user: " + user.getUsername());
                    this.writer.writeObject(false);
                }
                break;


            case "login":
                user = User.fromJson(body);
                if (ValidationController.checkLogin(user)) {
                    User existingUser = DatabaseHandler.getRegisteredUsers().get(user.getUsername());
                    if (existingUser != null) {
                        this.username = existingUser.getUsername();
                        existingUser.setOnlineStatus(true);
                        if (existingUser.getPrivateChats() == null) {
                            existingUser.setPrivateChats(); // Initialize privateChats if null
                        }
                        this.writer.writeObject(true);
                    } else {
                        this.writer.writeObject(false);
                    }
                } else {
                    this.writer.writeObject(false);
                }
                break;

            case "addFriend":
                String friendUsername = body;

                if (User.addUser(this.username, friendUsername)) {
                    this.writer.writeObject(true);
                } else {
                    this.writer.writeObject(false);
                }

                break;
            case "initData":
                logger.info("Handling initData for user: " + this.username);

                user = DatabaseHandler.getRegisteredUsers().get(this.username);
                if (user == null) {
                    logger.warning("User not found for initData: " + this.username);
                    this.writer.writeObject(null); // Indicate failure to the client
                    break;
                }

                logger.info("User found for initData: " + user);
                List<PrivateChat> userChats = user.getPrivateChats();
                if (userChats == null) {
                    logger.warning("Private chats list is null for user: " + this.username);
                    userChats = new ArrayList<>(); // Initialize to empty list
                } else {
                    logger.info("Number of private chats for user: " + userChats.size());
                }

                String chatJson = PrivateChat.convertSetToJson(userChats);
                logger.info("Sending private chats JSON to client: " + chatJson);
                this.writer.writeObject(chatJson);
                break;


            default:
                this.writer.writeObject(true);
                break;
        }
    }

    public String getHeader(String json) {
        int semicolonIndex = json.indexOf(';');
        if (semicolonIndex != -1) {
            return json.substring(0, semicolonIndex);
        } else {
            return json;
        }

    }

    public String getBody(String json) {
        int semicolonIndex = json.indexOf(';');
        if (semicolonIndex != -1) {
            return json.substring(semicolonIndex + 1);
        } else {
            return "";
        }
    }
}
