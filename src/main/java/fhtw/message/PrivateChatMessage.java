package fhtw.message;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrivateChatMessage implements Serializable {
    private String senderUsername;
    private String messageText;

    public PrivateChatMessage(String senderUsername, String messageText) {
        this.senderUsername = senderUsername;
        this.messageText = messageText;
    }


    public static PrivateChatMessage fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, PrivateChatMessage.class);
    }
}//end
