package com.meksula.chat.domain.user.registry

import com.meksula.chat.domain.user.ChatUser
import com.meksula.chat.domain.user.Contact
import com.meksula.chat.domain.user.ProfilePreferences
import com.meksula.chat.repository.ProfilePreferencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 18-08-2018
 * */

@SpringBootTest
class DefaultStatusRegistryTest extends Specification {

    @Autowired
    StatusRegistry statusRegistry

    @Autowired
    ProfilePreferencesRepository profilePreferencesRepository

    def 'loginUser, add user to registry test' () {
        setup:
        def chatUser = new ChatUser()
        chatUser.setUsername("test_username")

        when:
        statusRegistry.loginUser(chatUser)

        then:
        statusRegistry.getStatusSet().size() == 1
    }

    def 'logout test' () {
        setup:
        def chatUser = new ChatUser()
        chatUser.setUsername("test_username")

        when:
        statusRegistry.loginUser(chatUser)
        statusRegistry.getStatusSet().size() == 1

        then:
        statusRegistry.logoutUser(chatUser)
        statusRegistry.getStatusSet().size() == 0
    }

    def 'isLoggedNow test' () {
        setup:
        def chatUser = new ChatUser()
        chatUser.setUsername("test_username")

        def loggedOut = new ChatUser()
        loggedOut.setUsername("offlineUser")

        when:
        statusRegistry.loginUser(chatUser)

        then:
        !statusRegistry.isLoggedNow(loggedOut.getUsername())
        statusRegistry.isLoggedNow(chatUser.getUsername())
    }

    @Shared ProfilePreferences profile

    def 'getContactsStatus test'() {
        setup:
        profile = new ProfilePreferences()
        profile.setProfileUsername("test_user2")

        def contact = new Contact()
        contact.setUsername("contact_1")
        contact.setProfilePreferences(profile)
        def contact2 = new Contact()
        contact2.setUsername("contact_2")
        contact2.setProfilePreferences(profile)
        def contact3 = new Contact()
        contact3.setUsername("contact_3")
        contact3.setProfilePreferences(profile)
        def contact4 = new Contact()
        contact4.setUsername("offline_user")
        contact4.setProfilePreferences(profile)

        def user = new ChatUser()
        user.setUsername("contact_1")
        def user2 = new ChatUser()
        user2.setUsername("contact_2")
        def user3 = new ChatUser()
        user3.setUsername("contact_3")

        statusRegistry.loginUser(user)
        statusRegistry.loginUser(user2)
        statusRegistry.loginUser(user3)
        statusRegistry.getStatusSet().size() == 3

        profile.setContactsBook(new HashSet<Contact>())

        profile.addContact(contact)
        profile.addContact(contact2)
        profile.addContact(contact3)
        profile.addContact(contact4)
        profile = profilePreferencesRepository.save(profile)

        def statusMap = statusRegistry.getContactStatus(profile.getProfileUsername())

        expect:
        statusMap.size() == 4

        statusMap.get(username).booleanValue() == status

        where:
        username       | status
        "contact_1"    | true
        "contact_2"    | true
        "contact_3"    | true
        "offline_user" | false
    }

    def cleanup() {
        if (profile == null) {
            return
        }
        profilePreferencesRepository.delete(profile)
    }

}
