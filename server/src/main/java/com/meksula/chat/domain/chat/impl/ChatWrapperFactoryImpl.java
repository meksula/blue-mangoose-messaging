package com.meksula.chat.domain.chat.impl;

import com.meksula.chat.domain.chat.ChatWrapper;
import com.meksula.chat.domain.chat.ChatWrapperFactory;
import com.meksula.chat.domain.room.ChatForm;
import com.meksula.chat.domain.room.ChatRoom;
import com.meksula.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author
 * Karol Meksuła
 * 30-07-2018
 * */

@Service
public class ChatWrapperFactoryImpl implements ChatWrapperFactory {
    private ChatRoomRepository chatRoomRepository;
    private ChatForm chatForm;

    public ChatWrapperFactoryImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public ChatWrapper buildChatWrapper(ChatForm chatForm) {
        this.chatForm = chatForm;

        ChatRoom chatRoom = resurrect();

        return new DefaultChatWrapper(chatRoom);
    }

    private ChatRoom resurrect() {
        return chatRoomRepository.findByName(chatForm.getName()).orElse(buildFreshChat());
    }

    private ChatRoom buildFreshChat() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setCreatorUsername(chatForm.getCreatorUsername());
        chatRoom.setPeople(1);
        chatRoom.setName(chatForm.getName());
        chatRoom.setPasswordRequired(chatForm.isPasswordRequired());
        chatRoom.setPassword(chatForm.getPassword());
        chatRoom.setCreationDate(LocalDateTime.now());
        return chatRoom;
    }

}
