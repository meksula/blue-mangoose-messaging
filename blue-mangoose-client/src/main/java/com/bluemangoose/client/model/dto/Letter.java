package com.bluemangoose.client.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

/**
 * @author
 * Karol Meksuła
 * 10-09-2018
 * */

@Getter
@Setter
public class Letter {
    private String id;
    private String title;
    private String content;
    private long senderId;
    private String senderUsername;
    private long addresseeId;
    private String addresseeUsername;
    private String sendTimestamp;

    @JsonIgnore
    private LocalDateTime parsedTimestamp;

    private boolean unsealed;

    public Letter(String content) {
        this.content = content;
    }

    public Letter() {}

    @Override
    public String toString() {
        return "Title: " + title + "; Content: " + content + "; Send time: " + sendTimestamp;
    }
}
