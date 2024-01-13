package fhtw.chat;

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
public class PrivateChat implements Serializable {
    private int chatId;
    private User firstMember;
    private User secondMember;
    private List<PrivateChatMessage> chatMessages;
    private static int nextId = 0;

    public PrivateChat(User firstMember, User secondMember) {
        this.chatId = nextId;
        nextId++;
        this.firstMember = firstMember;
        this.secondMember = secondMember;
        this.chatMessages = new LinkedList<>();
        //TODO LocalDateTime timeStamp;
    }
}//end
