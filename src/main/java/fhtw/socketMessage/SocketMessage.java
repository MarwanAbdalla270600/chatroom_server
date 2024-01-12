package fhtw.socketMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@ToString
public class SocketMessage implements Serializable {
    private String method;

    private Object object;

    public SocketMessage(String method, Object object) {
        this.method = method;
        this.object = object;
    }

    public SocketMessage() {

    }

    public static SocketMessage fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, SocketMessage.class);
    }
    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
