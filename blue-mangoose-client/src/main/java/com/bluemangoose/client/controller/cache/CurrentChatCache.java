package com.bluemangoose.client.controller.cache;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

public class CurrentChatCache {
    private static CurrentChatCache currentChatCache;

    public CurrentChatCache() {}

    public static CurrentChatCache getInstance() {
        return currentChatCache;
    }

}
