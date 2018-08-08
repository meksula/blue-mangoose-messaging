package com.meksula.chat.domain.user.social;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

public interface SocialManager {
    Notification inviteToFriends(String username);

    Notification removeFriend(String friendsUsername);

    Notification blockChatUser(String chatUserUsername);

    void invitationRespond(Long invitationId, boolean respond);
}
