package com.meksula.chat.controller;

import com.meksula.chat.domain.chat.dto.ChatMessage;
import com.meksula.chat.domain.chat.ChatRoomManager;
import com.meksula.chat.domain.chat.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

@Controller
public class ChatController {
    private ChatRoomManager chatRoomManager;

    @Autowired
    public void setChatRoomManager(ChatRoomManager chatRoomManager) {
        this.chatRoomManager = chatRoomManager;
    }

    @MessageMapping("/main")
    public Message defaultChat(@Payload ChatMessage chatMessage) {
        chatRoomManager.receiveMessage(chatMessage);
        return chatMessage;
    }

}
