package fhtw.data;

import fhtw.chat.PrivateChat;
import fhtw.user.User;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DatabaseHandler {
    private static Map<String, User> registeredUsers = new HashMap<>();
    private static Map<Integer, PrivateChat> privateChats = new HashMap<>();

    public static Map<String, User> getRegisteredUsers() {
        return registeredUsers;
    }

    public static Map<Integer, PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public static PrivateChat findPrivatChatbyId(int chatId) {
        return privateChats.get(chatId);
    }
}
