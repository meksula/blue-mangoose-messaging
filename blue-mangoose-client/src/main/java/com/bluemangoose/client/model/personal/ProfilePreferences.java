package com.bluemangoose.client.model.personal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProfilePreferences {
    private long profileId;

    private long userId;
    private String profileUsername;

    private Set<Contact> contactsBook;
    private List<ContactAddNotification> notifications;
}
