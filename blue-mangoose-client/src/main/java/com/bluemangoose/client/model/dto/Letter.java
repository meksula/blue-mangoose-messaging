package com.bluemangoose.client.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author
 * Karol Meksuła
 * 10-09-2018
 * */

@Getter
@Setter
public class Letter {
    private String date;
    private String id;
    private String title;
    private String content;
    private long senderId;
    private String senderUsername;
    private long addresseeId;
    private String addresseeUsername;
    private LocalDateTime sendTime;
    private boolean unsealed;

    public Letter(String content) {
        this.content = content;
    }

    public Letter() {}

}
