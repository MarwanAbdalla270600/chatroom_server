package fhtw.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fhtw.data.DatabaseHandler;
import fhtw.message.PrivateChatMessage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import fhtw.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrivateChat extends Chat implements Serializable {

    private String firstMember;
    private String secondMember;
    private List<PrivateChatMessage> chatMessages;
    private boolean isOnline;

    public PrivateChat(String firstMember, String secondMember) {
        super();
        this.firstMember = firstMember;
        this.firstMember += DatabaseHandler.getRegisteredUsers().get(firstMember).getGender();  //for gender
        this.secondMember = secondMember;
        this.secondMember += DatabaseHandler.getRegisteredUsers().get(secondMember).getGender();//for gender
        this.chatMessages = new LinkedList<>();
    }
    public PrivateChat () {
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

    public static boolean sendMessage(PrivateChatMessage message) {
        PrivateChat chat = DatabaseHandler.findPrivatChatbyId(message.getChatId());
        chat.addMsg(message);
        return true;
    }
}
