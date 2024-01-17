package fhtw.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.chat.PrivateChat;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


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
