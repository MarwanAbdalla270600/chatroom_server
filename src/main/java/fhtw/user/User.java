package fhtw.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@ToString
public class User implements Serializable {
    private  String username;
    private  String password;
    private  Character gender;
    private boolean online = false;

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
