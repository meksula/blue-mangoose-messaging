package com.meksula.chat.domain.user.social;

import org.springframework.stereotype.Service;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

@Service
public class DefaultSocialManager implements SocialManager {
    @Override
    public Notification inviteToFriends(String username) {
        return null;
    }

    @Override
    public Notification removeFriend(String friendsUsername) {
        return null;
    }

    @Override
    public Notification blockChatUser(String chatUserUsername) {
        return null;
    }
}
