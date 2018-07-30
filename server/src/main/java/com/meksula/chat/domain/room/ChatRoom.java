package com.meksula.chat.domain.room;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meksula.chat.domain.user.ApplicationUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

@Getter
@Setter
@Table(name = "chat_room")
@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String creatorUsername;
    private int people;
    private boolean passwordRequired;
    private LocalDateTime creationDate;
    private String password;

    public ChatRoom(String name, ApplicationUser user, boolean passwordRequired) {
        this.name = name;
        this.creatorUsername = user.getUsername();
        this.passwordRequired = passwordRequired;
    }

    public ChatRoom() {}

}
