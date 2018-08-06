package com.bluemangoose.client.logic.web.socket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * @Author
 * Karol Meksuła
 * 30-07-2018
 * */

@Getter
public class ChatForm {
    private String name;
    private String creatorUsername;
    private boolean passwordRequired;
    private String password;
    private int capacity;

    @JsonCreator
    public ChatForm(@JsonProperty("name") String name, @JsonProperty("creatorUsername") String creatorUsername,
                    @JsonProperty("passwordRequired") boolean passwordRequired, @JsonProperty("password") String password) {
        this.name = name;
        this.creatorUsername = creatorUsername;
        this.passwordRequired = passwordRequired;
        this.password = password;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return name + ", " + creatorUsername;
    }

}

