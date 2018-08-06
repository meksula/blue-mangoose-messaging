package com.meksula.chat.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * @Author
 * Karol Meksuła
 * 06-08-2018
 * */

@Getter
public class ContactFind {
    private long userId;
    private String username;

    @JsonCreator
    public ContactFind(@JsonProperty("userId") long userId, @JsonProperty("username") String username) {
        this.userId = userId;
        this.username = username;
    }

}
