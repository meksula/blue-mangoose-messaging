package com.bluemangoose.client.controller.cache;

import com.bluemangoose.client.model.personal.ProfilePreferences;
import com.bluemangoose.client.model.personal.User;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

@Getter
@Setter
public class SessionCache {
    private static SessionCache sessionCache = new SessionCache();

    private SessionCache() {
    }

    public static SessionCache getInstance() {
        return sessionCache;
    }

    private String typedUsername;
    private String typedPassword;
    private User user;
    private ProfilePreferences profilePreferences;
    private Image profilePicture;
    private Map<String, Boolean> contactsStatus;

    public synchronized void updateContactStatus(Map<String, Boolean> status) {
        this.contactsStatus = Collections.synchronizedMap(status);
    }

}
