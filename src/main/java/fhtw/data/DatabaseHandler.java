package fhtw.data;

import fhtw.chat.PrivateChat;
import fhtw.user.User;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Handles the database operations for the chat application.
 * <p>
 * This class holds all registered users and private chats.
 * It provides methods to access and manage these entities.
 */
@Getter
@Setter
@ToString
public class DatabaseHandler {
    private static Map<String, User> registeredUsers = new HashMap<>();
    private static Map<Integer, PrivateChat> privateChats = new HashMap<>();

    /**
     * Retrieves the map of registered users.
     * The map's key is the username, and the value is the corresponding User object.
     *
     * @return a map of registered users
     */
    public static Map<String, User> getRegisteredUsers() {
        return registeredUsers;
    }

    /**
     * Retrieves the map of private chats.
     * The map's key is the chat ID, and the value is the corresponding PrivateChat object.
     *
     * @return a map of private chats
     */
    public static Map<Integer, PrivateChat> getPrivateChats() {
        return privateChats;
    }

    /**
     * Finds a private chat by its unique chat ID.
     *
     * @param chatId the unique ID of the chat
     * @return the PrivateChat object if found, or null if no chat with the given ID exists
     */
    public static PrivateChat findPrivatChatbyId(int chatId) {
        return privateChats.get(chatId);
    }
}
