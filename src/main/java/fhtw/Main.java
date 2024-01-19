package fhtw;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import fhtw.chat.PrivateChat;
import fhtw.data.DatabaseHandler;
import fhtw.message.PrivateChatMessage;
import fhtw.user.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The entry point of the chat application server.
 * <p>
 * This class initializes test data, sets up the server socket, and continuously listens for incoming client connections.
 * Upon accepting a connection, it creates a new ClientHandler to manage the communication with the connected client.
 */
public class Main {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    /**
     * The main method where the server is initialized and started.
     * Test data is created and the server listens for client connections.
     *
     * @param args command-line arguments (not used)
     * @throws InterruptedException if the thread is interrupted while waiting
     * @throws IOException          if an I/O error occurs while creating the server socket or accepting a client connection
     * @throws ClassNotFoundException if the class for a serialized object cannot be found
     */
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        // Data Testing:
        User testUser = new User("Max", "Test123", 'm');
        User testUser2 = new User("Flo", "Test123", 'm');
        User testUser3 = new User("Tom", "Test123", 'm');
        User testUser4 = new User("Marwan", "Test123", 'm');
        User testUser5 = new User("Mira", "Test123", 'f');

        DatabaseHandler.getRegisteredUsers().put("Max", testUser);
        DatabaseHandler.getRegisteredUsers().put("Flo", testUser2);
        DatabaseHandler.getRegisteredUsers().put("Tom", testUser3);
        DatabaseHandler.getRegisteredUsers().put("Marwan", testUser4);
        DatabaseHandler.getRegisteredUsers().put("Mira", testUser5);


        PrivateChat.addUser(testUser.getUsername(), testUser2.getUsername());
        PrivateChat.addUser(testUser.getUsername(), testUser3.getUsername());
        PrivateChat.addUser(testUser2.getUsername(), testUser3.getUsername());
        PrivateChat.addUser(testUser5.getUsername(), testUser4.getUsername());
        PrivateChat.addUser(testUser4.getUsername(), testUser2.getUsername());

        PrivateChatMessage message = new PrivateChatMessage("Max", "Das ist eine Nachricht");
        PrivateChatMessage message2 = new PrivateChatMessage("Max", "22Das ist eine Nachricht 222");
        PrivateChatMessage message3 = new PrivateChatMessage("Tom", "33Das ist eine Nachricht3333");
        PrivateChatMessage message4 = new PrivateChatMessage("Max", "Hey Tom");
        PrivateChatMessage message5 = new PrivateChatMessage("Tom", "FLO/MARWAN");

        DatabaseHandler.findPrivatChatbyId(0).addMsg(message);
        DatabaseHandler.findPrivatChatbyId(1).addMsg(message2);
        DatabaseHandler.findPrivatChatbyId(2).addMsg(message3);
        DatabaseHandler.findPrivatChatbyId(3).addMsg(message4);
        DatabaseHandler.findPrivatChatbyId(4).addMsg(message5);

        System.out.println("Printing PrivateChat Infos: ");
        System.out.println(DatabaseHandler.getPrivateChats());



        //Starting server:
        ServerSocket serverSocket;
        Socket socket;

        try {
            serverSocket = new ServerSocket(12345);
            while (true) {
                System.out.println("Waiting for clients.....");
                socket = serverSocket.accept();
                System.out.println("Connected Successfully.");

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