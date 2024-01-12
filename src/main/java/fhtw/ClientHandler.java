package fhtw;

import com.fasterxml.jackson.core.JsonProcessingException;
import fhtw.user.User;
import lombok.Getter;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
                System.out.println(message);

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
                //make user offline
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
                System.out.println("User with Name " + user.getUsername() + " registered");
                this.writer.writeObject(true);
                break;
            case "login":
                user = User.fromJson(body);
                System.out.println("User with Name " + user.getUsername() + " logged in");
                this.username = username;
                //make him beeing online
                this.writer.writeObject(true);
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


