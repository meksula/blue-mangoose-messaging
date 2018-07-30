package com.meksula.chat.domain.chat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

@Getter
@Setter
@Entity
@Table(name = "messages")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatMessage extends Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public String toString() {
        return "[RoomTarget:] " + super.getRoomTarget() + ", [author:] " + super.getUsernmame() + ", [content:] " + super.getContent();
    }
}
