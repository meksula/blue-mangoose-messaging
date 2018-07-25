package com.bluemangoose.client.model.logic.impl;

import com.bluemangoose.client.model.dto.Room;
import com.bluemangoose.client.model.logic.RoomSearcher;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 19-07-2018
 * */

public class RoomSearcherImpl implements RoomSearcher {

    @Override
    public Set<Room> searchRooms(int amount) {
        Set<Room> roomSet = new HashSet<>();

        for (int i = 0; i < amount; i++) {
            Room room = new Room();
            room.setAuthor("Karol Meksuła");
            room.setPasswordRequired(true);
            room.setPeople(5);

            roomSet.add(room);
        }

        return roomSet;
    }

}
