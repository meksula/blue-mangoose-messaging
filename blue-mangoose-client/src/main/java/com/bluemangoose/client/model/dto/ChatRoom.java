package com.bluemangoose.client.model.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author
 * Karol Meksuła
 * 19-07-2018
 * */

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatRoom {
    private long id;

    private String name;
    private String creatorUsername;
    private int people;
    private boolean passwordRequired;
    private String creationDate;

    @Override
    public String toString() {
        return id + ", " + name + ", " + creationDate + ", " + creatorUsername;
    }
}
