package com.meksula.chat.domain.user.social;

import com.meksula.chat.domain.user.Contact;
import com.meksula.chat.domain.user.ProfilePreferences;
import com.meksula.chat.repository.ContactAddNotificationRepository;
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
    private ContactAddNotificationRepository contactAddNotificationRepository;
    private boolean decision;

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    @Autowired
    public void setContactAddNotificationRepository(ContactAddNotificationRepository contactAddNotificationRepository) {
        this.contactAddNotificationRepository = contactAddNotificationRepository;
    }

    @Override
    public Notification inviteToFriends(Object principal, String friendsUsername) {
        ProfilePreferences invitationTarget = profilePreferencesRepository.findByProfileUsername(friendsUsername)
                .orElseThrow(() -> new UsernameNotFoundException(friendsUsername));

        ProfilePreferences currentUser = profilePreferencesRepository.findByProfileUsername(principal.toString())
                .orElseThrow(() -> new UsernameNotFoundException(principal.toString()));

        Notification notification = buildNotification(currentUser, invitationTarget);
        profilePreferencesRepository.save(invitationTarget);

        return notification;
    }

    private ContactAddNotification buildNotification(ProfilePreferences currentUser, ProfilePreferences invitationTarget) {
        final String INV_TITLE = "Zaproszenie do znajomych";
        final String INV_MESSAGE = "Otrzymałeś zaproszenie do znajomych od ";

        ContactAddNotification notification = new ContactAddNotification(INV_TITLE, INV_MESSAGE + currentUser.getProfileUsername());
        notification.setProfilePreferences(invitationTarget);
        notification.setInviterUserId(currentUser.getUserId());
        notification.setInviterUsername(currentUser.getProfileUsername());
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
    public Notification invitationResponse(Object principal, long invitationId, boolean respond) {
        this.decision = respond;

        ProfilePreferences user = profilePreferencesRepository.findByProfileUsername(principal.toString())
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: " + principal));

        ContactAddNotification notification = findNotificationById(user, invitationId);

        ProfilePreferences inviter = profilePreferencesRepository.findByProfileUsername(notification.getInviterUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: " + notification.getInviterUsername()));

        if (!respond) {
            return invitationReject(user, inviter);
        }

        Notification notif = prepareNotifications(user, inviter);
        addContactsEachOtherAndSave(user, inviter);
        return notif;
    }

    private ContactAddNotification invitationReject(ProfilePreferences user, ProfilePreferences inviter) {
        return prepareNotifications(user, inviter);
    }

    private ContactAddNotification findNotificationById(ProfilePreferences user, long invitationId) {
        return user.getNotifications()
                .stream()
                .filter(contactAddNotification -> contactAddNotification.getId() == invitationId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No notification with id: " + invitationId));
    }

    private void addContactsEachOtherAndSave(ProfilePreferences user, ProfilePreferences inviter) {
        user.addContact(createContact(user, inviter.getUserId(), inviter.getProfileUsername()));
        inviter.addContact(createContact(inviter, user.getUserId(), user.getProfileUsername()));

        profilePreferencesRepository.save(user);
        profilePreferencesRepository.save(inviter);
    }

    private ContactAddNotification prepareNotifications(ProfilePreferences user, ProfilePreferences inviter) {
        ContactAddNotification added = createNotification(inviter.getProfileUsername(), user);

        user.addContactAddNotification(added);
        inviter.addContactAddNotification(createNotification(user.getProfileUsername(), inviter));

        return added;
    }

    private Contact createContact(ProfilePreferences profilePreferences, long contactUsersId, String usernameToDisplay) {
        Contact contact = new Contact();
        contact.setIdOfContactUser(contactUsersId);
        contact.setProfilePreferences(profilePreferences);
        contact.setUsername(usernameToDisplay);
        return contact;
    }

    private ContactAddNotification createNotification(String contactUsername, ProfilePreferences profilePreferences) {
        final String INV_TITLE = "Użytkownik odpowiedział";
        final String INV_MESSAGE_ACCEPTED = "Teraz masz nowy kontakt" + contactUsername + ". Życzymy miłej i częstej konwersacji.";
        final String INV_MESSAGE_REFUSED = "Niestety, " + contactUsername + " odrzucił Twoje zaproszenie.";

        ContactAddNotification notification;

        if (decision) {
            notification = new ContactAddNotification(INV_TITLE, INV_MESSAGE_ACCEPTED);
        }

        else {
            notification = new ContactAddNotification(INV_TITLE, INV_MESSAGE_REFUSED);
        }

        notification.setProfilePreferences(profilePreferences);
        return notification;
    }

}
