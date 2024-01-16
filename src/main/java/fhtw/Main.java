package fhtw;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.chat.PrivateChat;
import fhtw.data.DatabaseHandler;
import fhtw.message.PrivateChatMessage;
import fhtw.user.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper(); // Just a test instantiation
        System.out.println("ObjectMapper test: " + mapper);
        //Starting server:
        User testUser = new User("Max", "Test123", 'f');
        User testUser2 = new User("Flo", "Test123", 'f');
        User testUser3 = new User("Tom", "Test123", 'f');
        User testUser4 = new User("Marwan", "Test123", 'f');

        DatabaseHandler.getRegisteredUsers().put("Max", testUser);
        DatabaseHandler.getRegisteredUsers().put("Flo", testUser2);
        DatabaseHandler.getRegisteredUsers().put("Tom", testUser3);
        DatabaseHandler.getRegisteredUsers().put("Marwan", testUser4);

        User.addUser(testUser.getUsername(), testUser2.getUsername());
        User.addUser(testUser.getUsername(), testUser3.getUsername());

        PrivateChatMessage message = new PrivateChatMessage("Max", "Das ist eine Nachricht");
        PrivateChatMessage message2 = new PrivateChatMessage("Max", "22Das ist eine Nachricht 222");
        PrivateChatMessage message3 = new PrivateChatMessage("Tom", "33Das ist eine Nachricht3333");
        PrivateChatMessage message4 = new PrivateChatMessage("Max", "Hey Tom");
        PrivateChatMessage message5 = new PrivateChatMessage("Tom", "Hey Max");

        DatabaseHandler.findPrivatChatbyId(0).addMsg(message);
        DatabaseHandler.findPrivatChatbyId(0).addMsg(message2);
        DatabaseHandler.findPrivatChatbyId(0).addMsg(message3);


        DatabaseHandler.findPrivatChatbyId(1).addMsg(message);
        DatabaseHandler.findPrivatChatbyId(1).addMsg(message2);
        DatabaseHandler.findPrivatChatbyId(1).addMsg(message3);
        DatabaseHandler.findPrivatChatbyId(1).addMsg(message4);
        DatabaseHandler.findPrivatChatbyId(1).addMsg(message5);


        ServerSocket serverSocket;

        Socket socket;

        try {
            serverSocket = new ServerSocket(80);
            while (true) {
                System.out.println("Waiting for clients...");
                socket = serverSocket.accept();
                System.out.println("Connected Successfully...");

                /** passed socket and client Array to ClientHandler Class and Start Thread...*/
                ClientHandler clientThread = new ClientHandler(socket, clients);
                clients.add(clientThread);
                clientThread.start();
                System.out.println(clients);
                if(socket.isClosed()) {
                    clients.remove(clientThread);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}