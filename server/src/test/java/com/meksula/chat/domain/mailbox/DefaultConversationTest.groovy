package com.meksula.chat.domain.mailbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.meksula.chat.domain.user.ProfilePreferences
import com.meksula.chat.repository.LetterRepository
import com.meksula.chat.repository.ProfilePreferencesRepository
import com.meksula.chat.repository.TopicRepository
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

@SpringBootTest
class DefaultConversationTest extends Specification {

    @Autowired
    private Conversation conversation

    @Autowired
    private TopicRepository topicRepository

    @Autowired
    private LetterRepository letterRepository

    @Autowired
    private ProfilePreferencesRepository profilePreferencesRepository

    long senderId = 345553
    String senderUsername = "zosia_samosia"
    long addresseeId = 12452
    String addresseeUsername = "admin"
    LocalDateTime sendTime = LocalDateTime.now()
    String title = "Wiadomość testowa"
    String content = "To tylko wiadomość testowa, nie martw się Zosiu."

    Letter letter

    def profileSender = new ProfilePreferences()
    def profileAddresse = new ProfilePreferences()

    def setup() {
        profileSender.setProfileUsername(senderUsername)
        profileAddresse.setProfileUsername(addresseeUsername)

        letter = new Letter.LetterBuilder()
                .senderId(senderId)
                .senderUsername(senderUsername)
                .addresseeId(addresseeId)
                .addresseeUsername(addresseeUsername)
                .sendTime(sendTime)
                .unsealed(false)
                .title(title)
                .content(content)
                .build()
    }

    def "init new conversation test"() {
        setup:
        profilePreferencesRepository.saveAll([profileSender, profileAddresse])

        def topicInit = conversation.initTopic(letter, "Topic testowy")
        println(new ObjectMapper().writeValueAsString(topicInit))
        Thread.sleep(500)

        expect:
        def topicUpdated = topicRepository.findById(topicInit.getTopicId()).get()
        topicUpdated.getLetters().size() == 1
        topicUpdated.getAddresseeId() > 0
        topicUpdated.getSenderId() > 0

        cleanup:
        profilePreferencesRepository.deleteAll([profileSender, profileAddresse])
    }

    def cleanup() {
        topicRepository.deleteAll()
        letterRepository.deleteAll()
    }

}
