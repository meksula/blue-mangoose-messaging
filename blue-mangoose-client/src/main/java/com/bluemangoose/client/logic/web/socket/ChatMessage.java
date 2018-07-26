package com.bluemangoose.client.logic.web.socket;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatMessage {
    private String id;
    private String usernmame;
    private String content;
    private String sendTime;
    private String roomTarget;
}

