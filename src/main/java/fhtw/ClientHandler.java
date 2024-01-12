package fhtw;

import fhtw.socketMessage.SocketMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
                System.out.println(message);
                SocketMessage socketmessage = SocketMessage.fromJson(message);

                handleMethod(socketmessage.getMethod(), socketmessage.getObject());
                //System.out.println(socketmessage.getMethod());
                //writer.writeObject("register");

               // SocketMessage socketMessage = SocketMessage.fromJson(message);

               // System.out.println(socketMessage.getMethod());
            }
            {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            clients.remove(this);
            try {
                //reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                /*e.printStackTrace();*/
            }
        }
    }



    public void handleMethod(String method, Object object) {
        SocketMessage response;
        switch (method) {
            case "register":
                response = new SocketMessage("response", true);
                try {
                    this.writer.writeObject(response.toJson());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "login":
                response = new SocketMessage("response", true);
                try {
                    this.writer.writeObject(response.toJson());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                response = new SocketMessage("response", false);
                try {
                    this.writer.writeObject(response.toJson());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}

