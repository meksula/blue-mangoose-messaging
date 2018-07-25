package com.bluemangoose.client.model.dto;

/**
 * @Author
 * Karol Meksuła
 * 19-07-2018
 * */

public class Room {
    private String roomId;
    private String author;
    private int people;
    private boolean passwordRequired;

    public int getPeople() {
        return people;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isPasswordRequired() {
        return passwordRequired;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPasswordRequired(boolean passwordRequired) {
        this.passwordRequired = passwordRequired;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

}
