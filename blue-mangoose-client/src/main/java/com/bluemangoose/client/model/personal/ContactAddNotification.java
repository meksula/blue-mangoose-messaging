package com.bluemangoose.client.model.personal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author
 * Karol Meksuła
 * 12-08-2018
 * */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactAddNotification {
    private Long id;
    private String title;
    private String message;
    private String initDate;

    private String inviterUsername;
    private Long inviterUserId;

    @Override
    public String toString() {
        return id + ", " + title + ", " + message + ", " + initDate + ", " + inviterUsername + ", " + inviterUserId;
    }
}
