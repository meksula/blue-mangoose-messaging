package com.bluemangoose.client.logic.web.impl;

import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.ChatRoomManager;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnector;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.logic.web.socket.ChatClient;
import com.bluemangoose.client.logic.web.socket.ChatMessage;
import com.bluemangoose.client.logic.web.socket.MyStompSessionHandler;
import com.bluemangoose.client.model.dto.ChatRoom;
import com.bluemangoose.client.model.personal.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public class DefaultRoomManager implements ChatRoomManager {
    private static DefaultRoomManager singletonInstance = new DefaultRoomManager();
    private HttpServerConnector<String> connector;
    private ChatClient chatClient;
    private MyStompSessionHandler sessionHandler;
    private String roomTarget;
    private boolean isConnected;
    private static User user;

    private DefaultRoomManager() {
        this.connector = new HttpServerConnectorImpl<>(String.class);
    }

    public static DefaultRoomManager getInstance(User user) {
        DefaultRoomManager.user = user;
        return singletonInstance;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public List<ChatRoom> fetchChatRoomList() {
        String string = connector.get(ApiPath.CHAT_ROOM_LIST);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ChatRoom> chatRooms = null;
        try {
            chatRooms = objectMapper.readValue(string, new TypeReference<List<ChatRoom>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chatRooms;
    }

    @Override
    public boolean joinRoom(String roomTarget) {
        this.roomTarget = roomTarget;
        this.chatClient = new ChatClient(roomTarget);
        this.sessionHandler = chatClient.connect();
        this.isConnected = true;
        return true;
    }

    @Override
    public void postMessage(String text) throws IllegalAccessException {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomTarget(roomTarget);
        chatMessage.setContent(text);
        chatMessage.setUsernmame(user.getUsername());
        chatMessage.setSendTime(String.valueOf(LocalDateTime.now()));

        if (isConnected) {
            this.sessionHandler.postMessage(chatMessage);
        }

        throw new IllegalAccessException("Cannot post message because websocket is disconnected.");
    }

}
