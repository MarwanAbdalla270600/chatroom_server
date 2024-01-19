package fhtw.message;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Represents a message in a private chat within the chat application.
 * <p>
 * This class encapsulates the details of a chat message, including the senders username,
 * the text of the message, and the Chat ID of the chat to which the message will be directed.
 * It provides methods for serializing and deserializing PrivateChatMessage objects to and from JSON.
 */
@Getter
@Setter
@ToString
public class PrivateChatMessage implements Serializable {
    private String senderUsername;
    private String messageText;
    private int chatId;

    /**
     * Constructs a new PrivateChatMessage with the specified senders username and message text.
     *
     * @param senderUsername the username of the sender of the message
     * @param messageText    the text content of the message
     */
    public PrivateChatMessage(String senderUsername, String messageText) {
        this.senderUsername = senderUsername;
        this.messageText = messageText;
    }

    /**
     * Default constructor. Used primarily for JSON deserialization.
     */
    public PrivateChatMessage() {
    }

    /**
     * Creates a PrivateChatMessage object from its JSON representation.
     *
     * @param json the JSON string representing a private chat message
     * @return a PrivateChatMessage object corresponding to the JSON string
     * @throws JsonProcessingException if there is an error in reading the JSON string
     */
    public static PrivateChatMessage fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, PrivateChatMessage.class);
    }

}
