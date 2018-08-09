package com.meksula.chat.domain.user

import com.meksula.chat.domain.user.social.ContactAddNotification
import com.meksula.chat.repository.ContactAddNotificationRepository
import com.meksula.chat.repository.ContactRepository
import com.meksula.chat.repository.ProfilePreferencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

@SpringBootTest
class ProfilePreferencesTest extends Specification {

    @Autowired
    private ProfilePreferencesRepository preferencesRepository

    @Autowired
    private ContactRepository contactRepository

    @Autowired
    private ContactAddNotificationRepository contactAddNotificationRepository

    private ProfilePreferences preferences
    private final String USERNAME = "admin"

    @Shared def invited = new ProfilePreferences()
    @Shared def inviting = new ProfilePreferences()

    void setup() {
        preferences = new ProfilePreferences()
        preferences.setUserId(23)
        preferences.setProfileUsername(USERNAME)
    }

    def 'save and find persisted entity test'() {
        setup:
        preferencesRepository.save(preferences)

        expect:
        ProfilePreferences preferences = preferencesRepository.findByProfileUsername(USERNAME).get()
        preferences != null
        preferences.getContactsBook().size() == 0

        Contact contact = new Contact()
        contact.setUsername("some_user")
        contact.setIdOfContactUser(141)
        contact.setProfilePreferences(preferences)

        preferences.getContactsBook().add(contact)
        preferences.getContactsBook().size() == 1
        preferencesRepository.save(preferences)

        ProfilePreferences updated = preferencesRepository.findByProfileUsername(USERNAME).get()
        updated.getContactsBook().size() == 1
    }

    def 'notification deliver test'() {
        setup: "situation simulation: Mock objects(Notification.class) adding to one object ProfilePreferences"
        invited.setProfileUsername("invitedUser")
        inviting.setProfileUsername("invitingUser")

        preferencesRepository.saveAll([inviting, invited])
        def notification = new ContactAddNotification("New friend!",
                "Some user invited you to friend list!") //some new notification

        def updated = preferencesRepository.findByProfileUsername("invitedUser").get()

        List<ContactAddNotification> notificationList = new ArrayList<>()
        notification.setProfilePreferences(updated)
        notificationList.add(notification)
        updated.setNotifications(notificationList)
        updated = preferencesRepository.save(updated)

        expect:
        updated.getNotifications().size() == 1
    }

    void cleanup() {
        preferencesRepository.deleteAll()
        contactRepository.deleteAll()
        contactAddNotificationRepository.deleteAll()
    }

}
