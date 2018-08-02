package com.meksula.chat.domain.user

import com.meksula.chat.repository.ContactRepository
import com.meksula.chat.repository.ProfilePreferencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

    private ProfilePreferences preferences
    private final String USERNAME = "admin"

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

    void cleanup() {
        preferencesRepository.deleteAll()
        contactRepository.deleteAll()
    }
}
