package com.meksula.chat.repository

import com.meksula.chat.domain.user.ProfilePreferences
import com.meksula.chat.domain.user.social.ContactAddNotification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 17-08-2018
 * */

@SpringBootTest
class ContactAddNotificationRepositoryTest extends Specification {

    @Autowired
    ContactAddNotificationRepository repository

    @Autowired
    ProfilePreferencesRepository profilePreferencesRepository

    @Shared ProfilePreferences profile
    @Shared long id

    def setup() {
        profile = new ProfilePreferences()
        profile = profilePreferencesRepository.save(profile)
    }

    def 'notification delete test'() {
        setup:
        def notification = new ContactAddNotification()
        profile.setNotifications([notification])
        notification.setProfilePreferences(profile)
        id = repository.save(notification).getId()

        when:
        profile.getNotifications().size() == 1
        profile.getNotifications().remove(notification)
        profile.getNotifications().size() == 0
        profilePreferencesRepository.save(profile)

        then:
        !repository.findById(id).isPresent()

        cleanup:
        profilePreferencesRepository.delete(profile)
    }

}
