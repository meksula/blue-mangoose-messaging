package com.bluemangoose.client.logic.web.impl;

import com.bluemangoose.client.logic.web.ChatRoomManager;
import com.bluemangoose.client.model.dto.ChatRoom;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public class DefaultRoomManagerTest {
    private ChatRoomManager chatRoomManager;

    @Test
    public void chatListFetchTest() {
        chatRoomManager = new DefaultRoomManager();
        List<ChatRoom> roomList = chatRoomManager.fetchChatRoomList();

        assertNotNull(roomList);
        assertEquals(1, roomList.size());
    }

}