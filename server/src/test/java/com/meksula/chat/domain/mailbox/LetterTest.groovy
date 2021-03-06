package com.meksula.chat.domain.mailbox

import com.fasterxml.jackson.databind.ObjectMapper
import com.meksula.chat.repository.LetterRepository
import com.meksula.chat.repository.TopicRepository
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

@SpringBootTest
class LetterTest extends Specification {

    @Autowired
    LetterRepository letterRepository

    @Autowired
    TopicRepository topicRepository

    String id = "d9821n3923xd2"
    long senderId = 345553
    String senderUsername = "zosia_samosia"
    long addresseeId = 12452
    String addresseeUsername = "admin"
    LocalDateTime sendTime = LocalDateTime.now()
    boolean unsealed = true
    String title = "Wiadomość testowa"
    String content = "To tylko wiadomość testowa, nie martw się Zosiu."

    Letter letter
    Topic topic

    def setup() {
        topic = new Topic()
        topicRepository.save(topic)

        letter = new Letter.LetterBuilder()
                .senderId(senderId)
                .senderUsername(senderUsername)
                .addresseeId(addresseeId)
                .addresseeUsername(addresseeUsername)
                .sendTime(sendTime)
                .unsealed(unsealed)
                .title(title)
                .content(content)
                .topic(topic)
                .build()

        letter.id = id
    }

    def "init fields test"() {
        expect:
        letter.getSenderId() == senderId
        letter.getSenderUsername() == senderUsername
        letter.getAddresseeId() == addresseeId
        letter.getAddresseeUsername() == addresseeUsername
        letter.getSendTime() == sendTime
        letter.isUnsealed() == unsealed
        letter.getTitle() == title
        letter.getContent() == content
        println(new ObjectMapper().writeValueAsString(letter))
    }

    def "save to database test"() {
        when:
        def letter = letterRepository.save(letter)

        then:
        letter != null
        letter.getId() == id
    }

    def cleanup() {
        letterRepository.delete(letter)
        topicRepository.deleteAll()
    }

}
