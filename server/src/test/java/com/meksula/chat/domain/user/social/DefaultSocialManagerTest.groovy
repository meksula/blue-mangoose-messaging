package com.meksula.chat.domain.user.social

import com.meksula.chat.domain.user.ChatUser
import com.meksula.chat.domain.user.Contact
import com.meksula.chat.domain.user.ProfilePreferences
import com.meksula.chat.repository.ChatUserRepository
import com.meksula.chat.repository.ProfilePreferencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

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

    @Autowired
    ChatUserRepository chatUserRepository

    @Shared def preferences = new ProfilePreferences()
    @Shared def username = "some_test_user"
    @Shared ChatUser chatUser

    void setup() {
        preferences.setProfileUsername(username)
        preferences.setProfileId(21000)

        chatUser = new ChatUser()
        chatUser.setUsername("principal_test")
        chatUserRepository.save(chatUser)

        profilePreferencesRepository.save(preferences)
    }

    def 'notification for invite to friends should NOT be passed'() {
        when:
        defaultSocialManager.inviteToFriends(chatUser, "not_existing_user")

        then:
        thrown(NoSuchElementException.class)
    }

    def 'notification for invite to friends should be passed correctly'() {
        when:
        def profile = new ProfilePreferences()
        profile.setProfileUsername("existing_user")
        chatUser.setUsername("existing_user")
        profilePreferencesRepository.save(profile)

        def notification = defaultSocialManager.inviteToFriends(chatUser, username)

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
        chatUser.setUsername(username)

        ContactAddNotification notification = (ContactAddNotification)defaultSocialManager.inviteToFriends(chatUser, username)

        println(notification.inviterUsername)

        when: "profile has received one notification and profile has no friends yet"
        def profile = profilePreferencesRepository.findByProfileUsername(username).get()
        profile.getNotifications().size() == 1 //profile has received one notification
        profile.getContactsBook().size() == 0  //profile has no friends yet
        // accept invitation
        defaultSocialManager.invitationResponse(chatUser, profile.getNotifications().get(0).getId(), true)
        //update profile
        profile = profilePreferencesRepository.findByProfileUsername(username).get()

        then: "profile now has no notification and has one friend"
        profile.getNotifications().size() == 3
        profile.getContactsBook().size() == 2
    }

    def 'invitation response REJECT test'() {
        setup: "principal_test invite some_test_user to friends"
        def principal = "principal_test"
        def principalProfile = new ProfilePreferences()
        principalProfile.setProfileUsername(principal)
        profilePreferencesRepository.save(principalProfile)
        chatUser.setUsername(principal)

        ContactAddNotification notification = (ContactAddNotification)defaultSocialManager.inviteToFriends(chatUser, username)

        println(notification.inviterUsername)

        when: "profile has received one notification and profile has no friends"
        def profile = profilePreferencesRepository.findByProfileUsername(username).get()
        profile.getNotifications().size() == 1
        profile.getContactsBook().size() == 0
        chatUser.setUsername(username)
        defaultSocialManager.invitationResponse(chatUser, profile.getNotifications().get(0).getId(), false)
        profile = profilePreferencesRepository.findByProfileUsername(username).get()

        then:
        profile.getNotifications().size() == 1
        profile.getContactsBook().size() == 0
    }

    def 'inviteToFriends is impossible - notification exist or user has target in contact list'() {
        setup:
        def friendName = "principal_test"
        def friend = new ProfilePreferences()
        friend.setProfileUsername(friendName)
        def friendProfile = profilePreferencesRepository.save(friend)
        //send invitation
        defaultSocialManager.inviteToFriends(chatUser, friendProfile.getProfileUsername())
        //accept invitation
        def friendUser = new ChatUser()
        friendUser.setUsername(friendName)

        when:
        def updatedFriendProfile = profilePreferencesRepository.findByProfileUsername(friendName).get()
        updatedFriendProfile.getNotifications().size() == 1
        def notifId = updatedFriendProfile.getNotifications().get(0).getId()
        defaultSocialManager.invitationResponse(friendUser, notifId, true)
        def updatedFriendProfile2 = profilePreferencesRepository.findByProfileUsername(friendName).get()
        updatedFriendProfile2.getContactsBook().size() == 2

        then:
        Notification notification = defaultSocialManager.inviteToFriends(chatUser, friendProfile.getProfileUsername())
        notification.getTitle() == "Invitation exist"
    }

    void cleanup() {
        profilePreferencesRepository.deleteAll()
        chatUserRepository.delete(chatUser)
    }

}
