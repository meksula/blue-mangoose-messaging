package com.bluemangoose.client.logic.web.mailbox;

import com.bluemangoose.client.controller.MailboxController;
import com.bluemangoose.client.model.dto.Letter;
import com.bluemangoose.client.model.dto.Mail;

/**
 * @author
 * Karol Meksuła
 * 13-08-2018
 * */

public class MailboxTemporaryCache {
    private static MailboxTemporaryCache temporaryCache = new MailboxTemporaryCache();

    private MailboxTemporaryCache() {}

    public static MailboxTemporaryCache getInstance() {
        return temporaryCache;
    }

    private String currentTopic;
    private Letter lastSendLetter;
    private MailboxController currentMailboxController;

    public static void setCurrentTopic(String currentTopic) {
        MailboxTemporaryCache.temporaryCache.currentTopic = currentTopic;
    }

    public static String getCurrentTopic() {
        return MailboxTemporaryCache.temporaryCache.currentTopic;
    }

    public static void setLastSendLetter(Letter letter) {
        MailboxTemporaryCache.temporaryCache.lastSendLetter = letter;
    }

    public static Letter getLastSendLetter() {
        return MailboxTemporaryCache.temporaryCache.lastSendLetter;
    }

    public static void setCurrentMailboxController(MailboxController mailboxController) {
        MailboxTemporaryCache.temporaryCache.currentMailboxController = mailboxController;
    }

    public static MailboxController getCurrentMailboxController() {
        return MailboxTemporaryCache.temporaryCache.currentMailboxController;
    }

}
