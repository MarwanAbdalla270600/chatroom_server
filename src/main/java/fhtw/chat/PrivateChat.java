package fhtw.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.message.PrivateChatMessage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fhtw.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrivateChat implements Serializable {
    private int chatId;
    private String firstMember;
    private String secondMember;
    private List<PrivateChatMessage> chatMessages;
    private static int nextId = 0;

    public PrivateChat(String firstMember, String secondMember) {
        this.chatId = nextId;
        nextId++;
        this.firstMember = firstMember;
        this.secondMember = secondMember;
        this.chatMessages = new LinkedList<>();
        //TODO LocalDateTime timeStamp;
    }

    public static String convertSetToJson(List<PrivateChat> privateChats) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(privateChats);
    }

    public static PrivateChat fromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, PrivateChat.class);
    }

    public void addMsg(PrivateChatMessage message) {
        this.chatMessages.add(message);
    }

}//end
