package fhtw.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Serves as the base class for different types of chats in the chat application.
 * <p>
 * This abstract class provides the basic structure for chats and is their superclass.
 * Can be used for future scaling of the Application, features like Group Chats etc.
 * It manages a unique chat ID for each chat instance, which is automatically assigned upon creation.
 */
@Getter
@Setter
@ToString
public abstract class Chat {
    protected int chatId;
    private static int nextId = 0;

    /**
     * Constructs a new Chat instance and assigns a unique chat ID.
     * The chat ID is auto-incremented for each new instance to ensure uniqueness.
     */
    public Chat() {
        this.chatId = nextId;
        nextId++;
    }
}
