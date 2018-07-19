package com.meksula.chat.client.model.logic;

import com.meksula.chat.client.model.dto.Room;

import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 19-07-2018
 * */

public interface RoomSearcher {
    Set<Room> searchRooms(int amount);
}
