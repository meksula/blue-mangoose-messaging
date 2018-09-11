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
    private String authorUsername = "Kazik";
    private String addresseeUsername = "Adresat";
    private String content;
    private String date = String.valueOf(LocalDateTime.now());

    public Letter(String content) {
        this.content = content;
    }
}
