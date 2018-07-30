package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.room.ChatRoom;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 30-07-2018
 * */

public class DefaultChatWrapper extends ChatWrapper {
    private final int PAGE_SIZE = 10;
    private List<Message> messages = new LinkedList<>();

    public DefaultChatWrapper(ChatRoom chatRoom) {
        super(chatRoom);
    }

    @Override
    public void captureMessage(Message message) {
        messages.add(message);
    }

    @Override
    public List<Message> getMessagesByPage(int page) {
        if (page <= 0) {
            return Collections.emptyList();
        }

        int firstIndex = messages.size() - (PAGE_SIZE * page);
        int lastIndex = messages.size() - ((page * PAGE_SIZE) - PAGE_SIZE);

        try {
            return messages.subList(firstIndex, lastIndex);
        } catch (IndexOutOfBoundsException e) {
            return Collections.emptyList();
        }

    }

    @Override
    public List<Message> getMessagesFromLastPages(int pages) {
        if (pages <= 0) {
            return Collections.emptyList();
        }

        int lastIndex = messages.size();
        int firstIndex = lastIndex - (pages * PAGE_SIZE);

        try {
            return messages.subList(firstIndex, lastIndex);
        } catch (IndexOutOfBoundsException e) {
            return Collections.emptyList();
        }

    }

    @Override
    public Message getLastMessage() {
        return messages.get(messages.size() - 1);
    }

    @Override
    public List<Message> getAllMessages() {
        return messages;
    }

    @Override
    public int pages() {
        return messages.size() / PAGE_SIZE;
    }

}
