package com.meksula.chat.domain.user.social;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

public interface SocialManager {
    Notification inviteToFriends(Object principal, String friendsUsername);

    Notification removeFriend(Object principal, String friendsUsername);

    Notification blockChatUser(Object principal, String chatUserUsername);

    Notification invitationResponse(Object principal, long invitationId, boolean response);
}
