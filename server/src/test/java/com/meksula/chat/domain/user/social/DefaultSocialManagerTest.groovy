package com.meksula.chat.domain.user.social

import com.meksula.chat.domain.user.ProfilePreferences
import com.meksula.chat.repository.ProfilePreferencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Shared
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 09-08-2018
 * */

@SpringBootTest
class DefaultSocialManagerTest extends Specification {

    @Autowired
    SocialManager defaultSocialManager

    @Autowired
    ProfilePreferencesRepository profilePreferencesRepository

    @Shared def preferences = new ProfilePreferences()
    @Shared def username = "some_test_user"

    void setup() {
        preferences.setProfileUsername(username)
        preferences.setProfileId(21000)

        profilePreferencesRepository.save(preferences)
    }

    def 'notification for invite to friends should NOT be passed'() {
        when:
        defaultSocialManager.inviteToFriends("some_user", "not_existing_user")

        then:
        thrown(UsernameNotFoundException.class)
    }

    def 'notification for invite to friends should be passed correctly'() {
        when:
        def profile = new ProfilePreferences()
        profile.setProfileUsername("existing_user")
        profilePreferencesRepository.save(profile)

        def notification = defaultSocialManager.inviteToFriends(profile.getProfileUsername(), username)

        then:
        notification != null
        profilePreferencesRepository.findByProfileUsername(username).get().getNotifications().size() == 1
    }

    def 'invitation response ACCEPT test'() {
        setup: "principal_test invite some_test_user to friends"
        def principal = "principal_test"
        def principalProfile = new ProfilePreferences()
        principalProfile.setProfileUsername(principal)
        profilePreferencesRepository.save(principalProfile)

        ContactAddNotification notification = (ContactAddNotification)defaultSocialManager.inviteToFriends(principal, username)

        println(notification.inviterUsername)

        when: "profile has received one notification and profile has no friends yet"
        def profile = profilePreferencesRepository.findByProfileUsername(username).get()
        profile.getNotifications().size() == 1 //profile has received one notification
        profile.getContactsBook().size() == 0  //profile has no friends yet
        // accept invitation
        defaultSocialManager.invitationResponse(username, profile.getNotifications().get(0).getId(), true)
        //update profile
        profile = profilePreferencesRepository.findByProfileUsername(username).get()

        then: "profile now has no notification and has one friend"
        profile.getNotifications().size() == 2 //one because one was removed and next was saved
        profile.getContactsBook().size() == 1
    }

    def 'invitation response REJECT test'() {
        setup: "principal_test invite some_test_user to friends"
        def principal = "principal_test"
        def principalProfile = new ProfilePreferences()
        principalProfile.setProfileUsername(principal)
        profilePreferencesRepository.save(principalProfile)

        ContactAddNotification notification = (ContactAddNotification)defaultSocialManager.inviteToFriends(principal, username)

        println(notification.inviterUsername)

        when: "profile has received one notification and profile has no friends"
        def profile = profilePreferencesRepository.findByProfileUsername(username).get()
        profile.getNotifications().size() == 1
        profile.getContactsBook().size() == 0
        defaultSocialManager.invitationResponse(username, profile.getNotifications().get(0).getId(), false)
        profile = profilePreferencesRepository.findByProfileUsername(username).get()

        then:
        profile.getNotifications().size() == 1
        profile.getContactsBook().size() == 0
    }

    void cleanup() {
        profilePreferencesRepository.deleteAll()
    }

}
