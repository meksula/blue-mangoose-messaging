package com.bluemangoose.client.controller.cache;

import com.bluemangoose.client.model.dto.ChatAccess;
import com.bluemangoose.client.model.personal.ProfilePreferences;
import com.bluemangoose.client.model.personal.User;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

@Getter
@Setter
public class SessionCache {
    private static SessionCache sessionCache = new SessionCache();

    private SessionCache() {}

    public static SessionCache getInstance() {
        return sessionCache;
    }

    private User user;
    private ProfilePreferences profilePreferences;
    private Image profilePicture;
    private ChatAccess chatAccess;
}
