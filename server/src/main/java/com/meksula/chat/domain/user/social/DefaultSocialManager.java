package com.meksula.chat.domain.user.social;

import com.meksula.chat.domain.user.ProfilePreferences;
import com.meksula.chat.repository.ProfilePreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

@Service
public class DefaultSocialManager implements SocialManager {
    private ProfilePreferencesRepository profilePreferencesRepository;

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    @Override
    public Notification inviteToFriends(Object principal, String friendsUsername) {
        ProfilePreferences invitationTarget = profilePreferencesRepository.findByProfileUsername(friendsUsername)
                .orElseThrow(() -> new UsernameNotFoundException(friendsUsername));

        Notification notification = buildNotification((String) principal, invitationTarget);
        profilePreferencesRepository.save(invitationTarget);

        return notification;
    }

    private ContactAddNotification buildNotification(String principal, ProfilePreferences invitationTarget) {
        final String INV_TITLE = "Zaproszenie do znajomych";
        final String INV_MESSAGE = "Otrzymałeś zaproszenie do znajomych od ";

        ContactAddNotification notification = new ContactAddNotification(INV_TITLE, INV_MESSAGE + principal);
        notification.setProfilePreferences(invitationTarget);
        invitationTarget.getNotifications().add(notification);
        return notification;
    }

    @Override
    public Notification removeFriend(Object principal, String friendsUsername) {
        return null;
    }

    @Override
    public Notification blockChatUser(Object principal, String chatUserUsername) {
        return null;
    }

    @Override
    public void invitationRespond(Object principal, Long invitationId, boolean respond) {

    }

}
