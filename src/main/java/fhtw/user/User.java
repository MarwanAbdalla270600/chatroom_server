package fhtw.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.chat.PrivateChat;
import fhtw.data.DatabaseHandler;
import fhtw.message.PrivateChatMessage;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
public class User implements Serializable {
    private String username;
    private String password;
    private Character gender;
    private boolean online = false;
    private Set<Integer> userPrivateChatIDs = new HashSet<>();

    public User(String username, String password, Character gender) {
        this.username = username;
        this.password = password;
        this.gender = gender;
    }

    public User(String username, String password, Character gender, boolean online) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.online = online;
    }

    public User() {
    }

    public Set<PrivateChat> getUserPrivateChats() {
        Set<PrivateChat> privateChats = new HashSet<>();
        for (Integer chatId : userPrivateChatIDs) {
            PrivateChat chat = DatabaseHandler.getPrivateChats().get(chatId);
            if (chat != null) {
                privateChats.add(chat);
            }
        }
        return privateChats;
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

    public static boolean addUser(String sender, String receiver) {
        User senderUser = DatabaseHandler.registeredUsers.get(sender);
        User receiverUser = DatabaseHandler.registeredUsers.get(receiver);
        System.out.println(sender);
        System.out.println(receiver);
        if (senderUser == null || receiverUser == null) {
            return false;
        }
        PrivateChat chat = new PrivateChat(senderUser, receiverUser);
        DatabaseHandler.privateChats.put(chat.getChatId(), chat); //adding privatchatroom to UserService HashMap
        senderUser.userPrivateChatIDs.add(chat.getChatId());
        receiverUser.userPrivateChatIDs.add(chat.getChatId());
        return true;
    }



    public boolean sendMessage(String senderUsername, int chatId, String messageText) {
        User sender = findUser(senderUsername);
        PrivateChat chat = DatabaseHandler.findPrivatChatbyId(chatId);
        PrivateChatMessage newMessage = new PrivateChatMessage(senderUsername, messageText);
        userPrivateChatIDs.add(chat.getChatId()); //adding to user
        DatabaseHandler.getPrivateChats().put(chat.getChatId(), chat);
        System.out.println("Message successfully sent to " + chatId);
        return true;
    }

    public void removeChat(Integer chatId) {
        userPrivateChatIDs.remove(chatId);  //from user
        DatabaseHandler.getPrivateChats().remove(chatId); //from system

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
