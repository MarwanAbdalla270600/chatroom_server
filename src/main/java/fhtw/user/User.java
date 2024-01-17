package fhtw.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.chat.PrivateChat;
import fhtw.data.DatabaseHandler;
import fhtw.message.PrivateChatMessage;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
public class User implements Serializable {
    private String username;
    private String password;
    private Character gender;
    private boolean online;
    private List<PrivateChat> privateChats;

    public User(String username, String password, Character gender) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.online = false;
        this.privateChats = new ArrayList<>();
    }


    public User() {
        this.privateChats = new ArrayList<>();
    }

    public void setPrivateChats() {
        this.privateChats = new ArrayList<>();
    }

    public void setOnlineStatus(boolean online) {
        this.online = online;
    }



    public User findUser(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }
        if (DatabaseHandler.getRegisteredUsers().containsKey(username)) {
            User searchedUser = DatabaseHandler.getRegisteredUsers().get(username);
            return searchedUser;
        }
        return null;
    }



    public boolean sendMessage(String senderUsername, int chatId, String messageText) {
        User sender = findUser(senderUsername);
        PrivateChat chat = DatabaseHandler.findPrivatChatbyId(chatId);
        PrivateChatMessage newMessage = new PrivateChatMessage(senderUsername, messageText);
        //this.privateChats.add(chat.getChatId()); //adding to user
        DatabaseHandler.getPrivateChats().put(chat.getChatId(), chat);
        System.out.println("Message successfully sent to " + chatId);
        return true;
    }



    public static User fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, User.class);
    }
    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public void setOnline(boolean isOline) {
        this.online = isOnline();
    }



}
