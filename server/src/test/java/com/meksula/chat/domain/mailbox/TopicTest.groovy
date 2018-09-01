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
 * 01-09-2018
 * */

@SpringBootTest
class TopicTest extends Specification {
    private Topic topic
    private Letter[] letters

    String topicId

    long senderId = 345553
    String senderUsername = "zosia_samosia"
    long addresseeId = 12452
    String addresseeUsername = "admin"
    LocalDateTime sendTime = LocalDateTime.now()
    boolean unsealed = true
    String title = "Wiadomość testowa"
    String content = "To tylko wiadomość testowa, nie martw się Zosiu."

    @Autowired
    private TopicRepository topicRepository

    @Autowired
    private LetterRepository letterRepository

    def setup() {
        topic = new Topic()
        topic.topicId = "2modm2d3f"
        topic.letters = new LinkedList<>()
        topic = topicRepository.save(topic)
        letters = new Letter[5]

        for (int i = 0; i < 5; i++) {
            def letter = new Letter.LetterBuilder()
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

            letterRepository.save(letter)
            topic.addLetter(letter)
        }

        topic.getTopicId()
    }

    def "save topic entity test"() {
        setup:
        topicId = topic.getTopicId()

        expect:
        println(topicId)

        Topic jsonTopic = topicRepository.findById(topicId).get()
        println(new ObjectMapper().writeValueAsString(jsonTopic))
    }

    def "topic builder test"() {
        setup:
        def topic = new Topic.TopicBuilder()
                .senderId(senderId)
                .senderUsername(senderUsername)
                .addresseId(addresseeId)
                .addresseeUsername(addresseeUsername)
                .build()

        topicRepository.save(topic)

        expect:
        def savedTopic = topicRepository.findById(topic.getTopicId()).get()
        savedTopic.getTopicId().length() == 10
        savedTopic.getAddresseeUsername() == addresseeUsername
        savedTopic.getSenderUsername() == senderUsername
        savedTopic.getAddresseeId() == addresseeId
        savedTopic.getAddresseeUsername() == addresseeUsername
    }

    def "send letter in new topic"() {
        setup:
        final String TITLE = "Nowa wiadomość!"
        def newLetter = new Letter.LetterBuilder()
                .senderId(senderId)
                .senderUsername(senderUsername)
                .addresseeId(addresseeId)
                .addresseeUsername(addresseeUsername)
                .sendTime(LocalDateTime.now())
                .unsealed(false)
                .title(TITLE)
                .content("To jest nowa wiadomość testowa.")
                .topic(topic)
                .build()

        topic.getLetters().size() == 5
        topic.addLetter(newLetter)
        topicRepository.save(topic)

        expect:
        def topic = topicRepository.findById(topic.getTopicId()).get()
        topic.getLetters().size() == 6
    }

    def cleanup() {
        topicRepository.deleteAll()
    }

}
