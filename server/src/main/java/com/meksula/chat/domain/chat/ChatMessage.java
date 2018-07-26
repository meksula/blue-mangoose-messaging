package com.meksula.chat.domain.chat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatMessage extends Message {
    private String roomTarget;

    @Override
    public String toString() {
        return "[RoomTarget:] " + roomTarget + ", [author:] " + super.getUsernmame() + ", [content:] " + super.getContent();
    }
}
