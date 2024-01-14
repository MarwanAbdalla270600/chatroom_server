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

@Getter
public class ClientHandler extends Thread {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                // DatabaseHandler.getRegisteredUsers().get(username).setOnlineStatus(false);
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                /*e.printStackTrace();*/
            }
        }
    }


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
                    System.out.println("nana so nit, try again");
                }
                System.out.println(DatabaseHandler.getRegisteredUsers());

                break;
            case "login":
                user = User.fromJson(body);
                System.out.println(user);
                if (ValidationController.checkLogin(user)) {
                    System.out.println("User with Name " + user.getUsername() + " logged in");
                    this.username = user.getUsername();
                    System.out.println(this.username);
                    DatabaseHandler.getRegisteredUsers().get(username).setOnlineStatus(true);
                    this.writer.writeObject(true);
                } else {
                    this.writer.writeObject(false);
                    System.out.println("nana so nit, try again");
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
                System.out.println("initdata");
                User tmp = DatabaseHandler.getRegisteredUsers().get(this.username);
                System.out.println("TEST: " + tmp);
                //System.out.println("IN initData");
                System.out.println(this.username);
                List<PrivateChat> userChats = DatabaseHandler.getRegisteredUsers().get(this.username).getPrivateChats();
                //System.out.println(userChats);
                //System.out.println("JSON:" + PrivateChat.convertSetToJson(userChats));
                this.writer.writeObject(PrivateChat.convertSetToJson(userChats));


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


