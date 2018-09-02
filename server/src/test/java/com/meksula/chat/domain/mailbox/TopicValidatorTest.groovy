package com.meksula.chat.domain.mailbox

import com.meksula.chat.domain.user.ProfilePreferences
import com.meksula.chat.repository.ProfilePreferencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

@SpringBootTest
class TopicValidatorTest extends Specification {

    @Autowired
    private TopicValidator topicValidator

    @Autowired
    private ProfilePreferencesRepository preferencesRepository

    def senderUsername = "Gargamel55"
    def addresseeUsername = "Smerfetka69"

    def sender = new ProfilePreferences()
    def addressee = new ProfilePreferences()

    Letter letter

    def setup() {
        sender.setProfileUsername(senderUsername)
        addressee.setProfileUsername(addresseeUsername)
        preferencesRepository.saveAll([sender, addressee])

        letter = new Letter.LetterBuilder()
                .senderUsername(senderUsername)
                .addresseeUsername(addresseeUsername)
                .build()
    }

    def "topicValidator should return true"() {
        expect:
        topicValidator.validate(senderUsername, letter)
    }

    def "test should not pass - error in addressee name"() {
        setup:
        letter.addresseeUsername = addresseeUsername + "df"

        expect:
        !topicValidator.validate(senderUsername, letter)
    }

    def cleanup() {
        preferencesRepository.deleteAll([sender, addressee])
    }

}
