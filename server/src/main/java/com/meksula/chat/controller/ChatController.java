package com.meksula.chat.controller;

import com.meksula.chat.domain.chat.ChatMessage;
import com.meksula.chat.domain.chat.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

@Controller
public class ChatController {

    @Autowired
    SimpMessageSendingOperations messaging;

    private Message lastMessage;

    @MessageMapping("/main")
    public Message defaultChat(@Payload ChatMessage chatMessage) {
        lastMessage = chatMessage;

        System.out.println("Nowa wiadomość: " + chatMessage.toString());

        messaging.convertAndSend("/topic/" + chatMessage.getRoomTarget(), lastMessage);

        return chatMessage;
    }

}
