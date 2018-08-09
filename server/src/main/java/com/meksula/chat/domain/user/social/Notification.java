package com.meksula.chat.domain.user.social;

import lombok.Getter;
import org.joda.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

@Getter
@MappedSuperclass
public abstract class Notification {
    private String title;
    private String message;
    private String initDate;

    public Notification(String title, String message) {
        this.title = title;
        this.message = message;
        this.initDate = LocalDateTime.now().toString();
    }

    public Notification() {}

}
