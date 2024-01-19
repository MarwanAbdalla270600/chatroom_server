package fhtw.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.chat.PrivateChat;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Creates a User object from its JSON representation.
     *
     * @param json the JSON string representing a user
     * @return a User object corresponding to the JSON string
     * @throws JsonProcessingException if there is an error in reading the JSON string
     */
    public static User fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, User.class);
    }

}
