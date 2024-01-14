package fhtw.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.data.DatabaseHandler;
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
    private boolean isOnline;

    public PrivateChat(String firstMember, String secondMember) {
        this.chatId = nextId;
        nextId++;
        this.firstMember = firstMember;
        this.firstMember += DatabaseHandler.getRegisteredUsers().get(firstMember).getGender();  //for gender
        this.secondMember = secondMember;
        this.secondMember += DatabaseHandler.getRegisteredUsers().get(secondMember).getGender();//for gender
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


    public static void setOnlineForList(List<PrivateChat> list) {
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
            }
        }
    }
}
