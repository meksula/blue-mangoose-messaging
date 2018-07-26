package com.meksula.chat.controller;

import com.meksula.chat.domain.chat.ChatRoomManager;
import com.meksula.chat.domain.room.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public void setChatRoomManager(ChatRoomManager chatRoomManager) {
        this.chatRoomManager = chatRoomManager;
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ChatRoom> chatRoomSet() {
        return chatRoomManager.getRoomSet();
    }

}
