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
        def notification = defaultSocialManager.inviteToFriends("existing_user", username)

        then:
        notification != null
        profilePreferencesRepository.findByProfileUsername(username).get().getNotifications().size() == 1
    }

    void cleanup() {
        profilePreferencesRepository.deleteAll()
    }

}
