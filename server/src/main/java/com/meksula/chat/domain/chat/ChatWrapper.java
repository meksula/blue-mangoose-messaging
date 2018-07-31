package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.chat.dto.Message;
import com.meksula.chat.domain.room.ChatRoom;
import org.joda.time.LocalDateTime;

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

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public abstract void captureMessage(Message message);

    public abstract List<Message> getMessagesByPage(int page);

    public abstract List<Message> getMessagesFromLastPages(int pages);

    public abstract Message getLastMessage();

    public abstract List<Message> getAllMessages();

    public abstract void setAllMessages(List<Message> shortenedList);

    public abstract int pages();

    public abstract LocalDateTime getInitTimestamp();

    public abstract void setInitTimestamp(LocalDateTime timestamp);
}
