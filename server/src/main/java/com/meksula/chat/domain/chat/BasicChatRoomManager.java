package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.room.ChatRoom;
import com.meksula.chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

@Service
public class BasicChatRoomManager implements ChatRoomManager {
    private ChatRoomRepository chatRoomRepository;
    private Map<Long, ChatRoom> roomMap;

    public BasicChatRoomManager() {
        this.roomMap = new HashMap<>();
    }

    @Autowired
    public void setChatRoomRepository(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public List<ChatRoom> getRoomSet() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setCreationDate(LocalDateTime.now());
        chatRoom.setPassword("");
        chatRoom.setName("default");
        chatRoom.setPeople(0);
        chatRoom.setCreatorUsername("karoladmin");

        roomMap.put(chatRoom.getId(), chatRoom);
        return new ArrayList<>(Collections.singleton(chatRoom));
        //return chatRoomRepository.findAll();
    }

}
