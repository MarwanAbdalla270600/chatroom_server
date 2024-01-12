package fhtw;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        //Starting server:


        ServerSocket serverSocket;

        Socket socket;

        try {
            serverSocket = new ServerSocket(12345);
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