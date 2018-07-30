package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.room.ChatForm;
import com.meksula.chat.domain.room.ChatRoom;
import com.meksula.chat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

@Service
public class BasicChatRoomManager implements ChatRoomManager {
    private ChatRoomRepository chatRoomRepository;
    private ChatWrapperFactory chatWrapperFactory;
    private SimpMessageSendingOperations messaging;
    private Map<String, ChatWrapper> roomMap;

    public BasicChatRoomManager() {
        this.roomMap = new LinkedHashMap<>();
    }

    @Autowired
    public void setChatRoomRepository(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Autowired
    public void setChatWrapperFactory(ChatWrapperFactory chatWrapperFactory) {
        this.chatWrapperFactory = chatWrapperFactory;
    }

    @Autowired
    public void setMessaging(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    @Override
    public void receiveMessage(Message message) {
        roomMap.get(message.getRoomTarget()).captureMessage(message);
        broadcastMessage(message);
    }

    private void broadcastMessage(Message message) {
        messaging.convertAndSend("/topic/" + message.getRoomTarget(), message);
    }

    @Override
    public ChatRoom registerChatWrapper(ChatForm chatForm) {
        ChatWrapper chatWrapper = chatWrapperFactory.buildChatWrapper(chatForm);
        roomMap.put(chatWrapper.chatRoom.getName(), chatWrapper);

        return chatWrapper.chatRoom;
    }

    @Override
    public void removeChatWrapper(String name) {
        roomMap.remove(name);
    }

    @Override
    public List<ChatRoom> getRoomSet() {
        List<ChatRoom> roomList = new ArrayList<>();
        roomMap.forEach((k,v) -> roomList.add(v.chatRoom));

        return roomList;
    }

    @Override
    public Map<String, ChatWrapper> getChatMap() {
        return roomMap;
    }

}
