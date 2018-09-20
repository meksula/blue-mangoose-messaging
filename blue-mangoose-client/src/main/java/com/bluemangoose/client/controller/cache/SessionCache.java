package com.bluemangoose.client.controller.cache;

import com.bluemangoose.client.model.dto.Mail;
import com.bluemangoose.client.model.personal.ProfilePreferences;
import com.bluemangoose.client.model.personal.User;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private List<Mail> mailboxList;
    private AtomicBoolean lettersUnsealed = new AtomicBoolean(false);

    public synchronized void updateContactStatus(Map<String, Boolean> status) {
        sessionCache.contactsStatus = Collections.synchronizedMap(status);
    }

    public synchronized void setLettersUnsealed(boolean state) {
        sessionCache.lettersUnsealed.set(state);
    }

}
