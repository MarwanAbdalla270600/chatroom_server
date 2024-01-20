package fhtw.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.data.DatabaseHandler;
import fhtw.message.PrivateChatMessage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import fhtw.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a private chat between two users in the chat application.
 * <p>
 * This class encapsulates the details of a private chat, including the members, the chat messages, and the online status.
 * It provides methods to manage the chat messages, convert chat data to JSON, and manage online status.
 */
@Getter
@Setter
@ToString
public class PrivateChat implements Serializable {
    private static final Logger logger = Logger.getLogger(PrivateChat.class.getName());

    private int chatId;
    private String firstMember;
    private String secondMember;
    private List<PrivateChatMessage> chatMessages;
    private boolean isOnline;
    private static int nextId = 0; // This should be static if you want to increment it across all instances

    /**
     * Constructs a new PrivateChat with the specified members.
     * Initializes the list of chat messages and appends the gender of each member to their username.
     *
     * @param firstMember  the username of the first member of the chat
     * @param secondMember the username of the second member of the chat
     */
    public PrivateChat(String firstMember, String secondMember) {
        super();
        this.chatId = nextId++;
        this.firstMember = firstMember;
        this.firstMember += DatabaseHandler.getRegisteredUsers().get(firstMember).getGender();  //for gender
        this.secondMember = secondMember;
        this.secondMember += DatabaseHandler.getRegisteredUsers().get(secondMember).getGender();//for gender
        this.chatMessages = new LinkedList<>();
    }

    /**
     * Default constructor. Used primarily for JSON deserialization.
     */
    public PrivateChat () {
    }

    /**
     * Converts a list of PrivateChat objects to its JSON representation.
     *
     * @param privateChats the list of PrivateChat objects
     * @return a JSON string representing the list of private chats
     * @throws JsonProcessingException if there is an error in writing the JSON string
     */
    public static String convertSetToJson(List<PrivateChat> privateChats) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(privateChats);
    }

    /**
     * Adds a message to this private chat.
     *
     * @param message the PrivateChatMessage to be added to the chat
     */
    public void addMsg(PrivateChatMessage message) {
        this.chatMessages.add(message);
    }

    /**
     * Sets the online status for a list of private chats based on the online status of their members.
     *
     * @param list the list of PrivateChat objects whose online status needs to be updated
     */
    public static void setOnlineForList(List<PrivateChat> list) {
        if (list == null) {
            logger.warning("List of PrivateChats is null.");
            return; // Exit the method to prevent NullPointerException
        }
        for(PrivateChat chat: list) {
            String firstString = DatabaseHandler.getPrivateChats().get(chat.chatId).getFirstMember();
            String secondString = DatabaseHandler.getPrivateChats().get(chat.chatId).getSecondMember();
            firstString = firstString.substring(0, firstString.length()-1);
            secondString = secondString.substring(0, secondString.length()-1);

            User first = DatabaseHandler.getRegisteredUsers().get(firstString);
            User second = DatabaseHandler.getRegisteredUsers().get(secondString);

            if(first != null && second != null) {
                System.out.println("first: " + first.isOnline());
                System.out.println("second: " + second.isOnline());
                chat.setOnline(first.isOnline() && second.isOnline());
            } else {
                logger.warning("One or both users in a chat are null. Chat ID: " + chat.getChatId());
            }
        }
    }

    /**
     * Adds a user to the private chat between the sender and receiver.
     * Creates a new private chat if one does not already exist between the two users.
     *
     * @param sender   the username of the sender
     * @param receiver the username of the receiver
     * @return true if the user is successfully added or the chat is successfully created, false otherwise
     */
    public static boolean addUser(String sender, String receiver) {
        User senderUser = DatabaseHandler.getRegisteredUsers().get(sender);
        User receiverUser = DatabaseHandler.getRegisteredUsers().get(receiver);
        System.out.println(sender);
        System.out.println(receiver);
        if (senderUser == null || receiverUser == null) {
            System.out.println("SENDER " + senderUser + "is NULL");
            System.out.println("Receiver " + receiverUser + "is NULL");
            return false;
        }
        PrivateChat chat = new PrivateChat(sender, receiver);
        DatabaseHandler.getPrivateChats().put(chat.getChatId(), chat); //adding privatchatroom to UserService HashMap
        senderUser.getPrivateChats().add(chat);
        receiverUser.getPrivateChats().add(chat);
        return true;
    }

    /**
     * Sends a message in a private chat.
     *
     * @param message the PrivateChatMessage to be sent
     * @return true if the message is successfully sent, false otherwise
     */
    public static boolean sendMessage(PrivateChatMessage message) {
        PrivateChat chat = DatabaseHandler.findPrivatChatbyId(message.getChatId());
        chat.addMsg(message);
        return true;
    }
}
