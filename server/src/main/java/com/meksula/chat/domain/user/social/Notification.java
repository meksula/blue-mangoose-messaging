package com.meksula.chat.domain.user.social;

import lombok.Getter;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

@Getter
public abstract class Notification {
    private String title;
    private String message;

    public Notification(String title, String message) {
        this.title = title;
        this.message = message;
    }

}
