package com.meksula.chat.domain.user.social;

import com.meksula.chat.domain.user.ApplicationUser;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.domain.user.Contact;
import com.meksula.chat.domain.user.ProfilePreferences;
import com.meksula.chat.repository.ProfilePreferencesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

@Slf4j
@Service
public class DefaultSocialManager implements SocialManager {
    private ProfilePreferencesRepository profilePreferencesRepository;
    private boolean decision;

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    @Override
    public Notification inviteToFriends(Object principal, String friendsUsername) {
        ChatUser chatUser = (ChatUser) principal;

        if (contactExist(chatUser, friendsUsername) || notificationExist(chatUser, friendsUsername)) {
            log.info("Invitation just exist! Cannot invite user twice!");
            return new Notification("Invitation exist", "Cannot invite user again!");
        }

        ProfilePreferences invitationTarget = profilePreferencesRepository.findByProfileUsername(friendsUsername)
                .orElseThrow(() -> new UsernameNotFoundException(friendsUsername));

        ProfilePreferences currentUser = profilePreferencesRepository.findByProfileUsername(chatUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(chatUser.getUsername()));

        Notification notification = buildNotification(currentUser, invitationTarget);
        profilePreferencesRepository.save(invitationTarget);

        log.info("Invitation send correctly.");
        return notification;
    }

    /**
     * User cannot invite another user if has its in current contacts
     * */
    private boolean contactExist(ApplicationUser inviter, String whom) {
        return profilePreferencesRepository.findByProfileUsername(inviter.getUsername())
                .get()
                .getContactsBook()
                .stream()
                .anyMatch(contact -> contact.getUsername().equals(whom));
    }

    private boolean notificationExist(ApplicationUser inviter, String whom) {
        return profilePreferencesRepository.findByProfileUsername(whom)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot invite user, because not exist!"))
                .getNotifications()
                .stream()
                .anyMatch(notification -> notification.getInviterUsername().equals(inviter.getUsername()));
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
        ChatUser target = (ChatUser) principal;

        ProfilePreferences user = profilePreferencesRepository.findByProfileUsername(target.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: " + target.getUsername()));

        ContactAddNotification notification = findNotificationById(user, invitationId);

        ProfilePreferences inviter = profilePreferencesRepository.findByProfileUsername(notification.getInviterUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with username: " + notification.getInviterUsername()));

        if (!respond) {
            return invitationReject(user, inviter);
        }

        Notification notif = prepareNotifications(user, inviter);
        addContactsEachOtherAndSave(user, inviter);

        cleanOldNotification(inviter.getProfileUsername(), target.getUsername(), invitationId);
        return notif;
    }

    private void cleanOldNotification(String inviterUsername, String targetUsername, long invitationId) {
        removeNotification(inviterUsername, invitationId);
        removeNotification(targetUsername, invitationId);
    }

    @Override
    public void removeNotification(ChatUser chatUser, long notificationId) {
        ProfilePreferences profilePreferences = profilePreferencesRepository.findByProfileUsername(chatUser.getUsername())
                .orElseThrow(() -> new AccessDeniedException("You have no access to remove notifications."));

        profilePreferences.getNotifications()
                .remove(profilePreferences.getNotifications()
                        .stream()
                        .filter(notification -> notification.getId() == notificationId)
                        .findFirst()
                        .orElse(null));

        log.info("Notification with id: " + notificationId + " has just removed.");
        profilePreferencesRepository.save(profilePreferences);
    }

    private void removeNotification(String username, long notificationId) {
        ProfilePreferences profilePreferences = profilePreferencesRepository.findByProfileUsername(username)
                .orElseThrow(() -> new AccessDeniedException("You have no access to remove notifications."));

        profilePreferences.getNotifications()
                .remove(profilePreferences.getNotifications()
                        .stream()
                        .filter(notification -> notification.getId() == notificationId)
                        .findFirst()
                        .orElse(null));

        profilePreferencesRepository.save(profilePreferences);
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
