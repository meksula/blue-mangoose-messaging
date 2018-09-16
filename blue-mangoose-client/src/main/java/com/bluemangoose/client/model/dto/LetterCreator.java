package com.bluemangoose.client.model.dto;

import com.bluemangoose.client.controller.cache.SessionCache;

/**
 * @author
 * Karol Meksuła
 * 16-09-2018
 * */

public class LetterCreator {

    public Letter letterBuild(String content, String title, String addresseUsername) {
        Letter letter = buildDefault(content, addresseUsername);
        letter.setTitle(title);
        return letter;
    }

    public Letter letterBuild(String content, String addresseUsername) {
        return buildDefault(content, addresseUsername);
    }

    private Letter buildDefault(String content, String addresseUsername) {
        Letter letter = new Letter();
        letter.setContent(content);
        letter.setSenderUsername(SessionCache.getInstance().getProfilePreferences().getProfileUsername());
        letter.setAddresseeUsername(addresseUsername);
        letter.setUnsealed(false);
        return letter;
    }

}
