package com.meksula.chat.controller;

import com.meksula.chat.domain.chat.ChatAccessException;
import com.meksula.chat.domain.chat.ChatAccessValidator;
import com.meksula.chat.domain.chat.ChatRoomManager;
import com.meksula.chat.domain.chat.ChatWrapper;
import com.meksula.chat.domain.chat.dto.Message;
import com.meksula.chat.domain.chat.impl.ChatAccessValidatorImpl;
import com.meksula.chat.domain.room.ChatAccess;
import com.meksula.chat.domain.room.ChatForm;
import com.meksula.chat.domain.room.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

@RestController
@RequestMapping("/api/v1/room")
public class ChatRoomController {
    private ChatRoomManager chatRoomManager;
    private ChatAccessValidator chatAccessValidator;

    public ChatRoomController() {
        this.chatAccessValidator = new ChatAccessValidatorImpl();
    }

    @Autowired
    public void setChatRoomManager(ChatRoomManager chatRoomManager) {
        this.chatRoomManager = chatRoomManager;
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ChatRoom> chatRoomSet() {
        return chatRoomManager.getRoomSet();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ChatRoom createChatRoom(@RequestBody ChatForm chatForm) {
        return chatRoomManager.registerChatWrapper(chatForm);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Message> getAllMessagesInRoom(@RequestBody ChatAccess chatAccess) throws ChatAccessException {
        ChatWrapper chatWrapper = chatRoomManager.getChatWrapper(chatAccess.getChatName());

        if (chatAccessValidator.permit(chatAccess, chatWrapper.getChatRoom())) {
            return chatWrapper.getAllMessages();
        }

        else throw new ChatAccessException("You have no right to read messages in this room.");
    }

}
