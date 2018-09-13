package com.bluemangoose.client.logic.web.mailbox;

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

    public static void setCurrentTopic(String currentTopic) {
        MailboxTemporaryCache.temporaryCache.currentTopic = currentTopic;
    }

    public static String getCurrentTopic() {
        return MailboxTemporaryCache.temporaryCache.currentTopic;
    }

    private String currentTopic;

}
