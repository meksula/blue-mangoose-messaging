package com.bluemangoose.client.model.logic;

import com.bluemangoose.client.model.dto.Room;

import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 19-07-2018
 * */

public interface RoomSearcher {
    Set<Room> searchRooms(int amount);
}
