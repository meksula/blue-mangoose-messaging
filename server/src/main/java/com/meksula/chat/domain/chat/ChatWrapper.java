package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.room.ChatRoom;

import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 29-07-2018
 * */

public abstract class ChatWrapper {
    protected ChatRoom chatRoom;

    public ChatWrapper(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public abstract void captureMessage(Message message);

    public abstract List<Message> getMessagesByPage(int page);

    public abstract List<Message> getMessagesFromLastPages(int pages);

    public abstract Message getLastMessage();

    public abstract List<Message> getAllMessages();

    public abstract int pages();
}
