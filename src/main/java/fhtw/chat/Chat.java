package fhtw.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString


public abstract class Chat {    //was ursprünglich für GroupChats etc. gedacht
    protected int chatId;
    private static int nextId = 0;

    public Chat() {
        this.chatId = nextId;
        nextId++;
    }
}
