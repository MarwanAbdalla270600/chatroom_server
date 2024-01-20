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
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Represents a User in the Application
 * <p>
 * encapsulates all the user-related information like username, password, gender, and online status.
 * It also contains a list of private chats that the user is part of. This class provides methods for serializing
 * and deserializing User objects for JSON, managing private chats, and changing online status.
 */
@Getter
@ToString
public class User implements Serializable {
    private static final Logger logger = Logger.getLogger(User.class.getName());

    private String username;
    private String password;
    private Character gender;
    private boolean online;
    private List<PrivateChat> privateChats;

    /**
     * Constructs a new User with the specified username, password, and gender.
     * Initially, the user is not online and has no active private chats.
     *
     * @param username is username of the user
     * @param password is password of the user
     * @param gender   is the gender of the user
     */
    public User(String username, String password, Character gender) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.online = false;
        this.privateChats = new ArrayList<>();
        logger.info("Creating new user with initialized privateChats: " + username);
    }

    /**
     * Constructs a new User with no username, password, or gender.
     * Is used for JSON deserialization.
     */
    public User() {
        this.privateChats = new ArrayList<>();
    }

    /**
     * Initializes the list of private chats for this user.
     */
    public void setPrivateChats() {
        this.privateChats = new ArrayList<>();
    }

    /**
     * Sets the Online Status for a registered User in the System
     * @param online is of type boolean
     */
    public void setOnlineStatus(boolean online) {
        this.online = online;
    }



/*public Set<PrivateChat> getUserPrivateChats() {
        Set<PrivateChat> privateChats = new HashSet<>();
        for (Integer chatId : userPrivateChatIDs) {
            PrivateChat chat = DatabaseHandler.getPrivateChats().get(chatId);
            if (chat != null) {
                privateChats.add(chat);
            }
        }
        return privateChats;
    }*/

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
        logger.info("Adding user: Sender = " + sender + ", Receiver = " + receiver);
        User senderUser = DatabaseHandler.getRegisteredUsers().get(sender);
        User receiverUser = DatabaseHandler.getRegisteredUsers().get(receiver);
        System.out.println(sender);
        System.out.println(receiver);
        if (senderUser == null || receiverUser == null) {
            logger.info("SENDER "+senderUser);
            logger.info("Receiver "+receiverUser);
            return false;
        }
        PrivateChat chat = new PrivateChat(sender, receiver);
        DatabaseHandler.getPrivateChats().put(chat.getChatId(), chat); //adding privatchatroom to UserService HashMap
        logger.info("HashMAPS ");
        System.out.println(DatabaseHandler.getPrivateChats());
        senderUser.getPrivateChats().add(chat);
        receiverUser.getPrivateChats().add(chat);
        return true;
    }



    public boolean sendMessage(String senderUsername, int chatId, String messageText) {
        User sender = findUser(senderUsername);
        PrivateChat chat = DatabaseHandler.findPrivatChatbyId(chatId);
        PrivateChatMessage newMessage = new PrivateChatMessage(senderUsername, messageText);
        if (sender == null || chat == null) {
            logger.warning("Either sender or chat is null, message sending failed.");
            return false;
        }
        //this.privateChats.add(chat.getChatId()); //adding to user
        DatabaseHandler.getPrivateChats().put(chat.getChatId(), chat);
        logger.info("Message successfully sent to " + chatId);
        return true;
    }

    /**
     * Creates a User object from its JSON representation.
     *
     * @param json the JSON string representing a user
     * @return a User object corresponding to the JSON string
     * @throws JsonProcessingException if there is an error in reading the JSON string
     */
    public static User fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(json, User.class);
        if (user.getPrivateChats() == null) {
            user.setPrivateChats();
        }
        return user;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

}
