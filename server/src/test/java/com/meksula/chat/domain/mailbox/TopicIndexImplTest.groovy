package com.meksula.chat.domain.mailbox

import com.meksula.chat.repository.LetterRepository
import com.meksula.chat.repository.TopicRepository
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 03-09-2018
 * */

@SpringBootTest
class TopicIndexImplTest extends Specification {

    @Autowired
    private TopicIndexImpl topicIndex

    @Autowired
    private TopicRepository topicRepository

    @Autowired
    private LetterRepository letterRepository

    Letter letter
    long senderId = 345553
    String senderUsername = "zosia_samosia"
    long addresseeId = 12452
    String addresseeUsername = "admin"
    LocalDateTime sendTime = LocalDateTime.now()
    boolean unsealed = true
    String title = "Wiadomość testowa"
    String content = "To tylko wiadomość testowa, nie martw się Zosiu."

    def setup() {
        topicIndex.createIndexes()
    }

    def "topic short info index test"() {
        when:
        saveToDatabaseEntities(5)
        topicIndex.updateIndexes()

        then:
        println(topicIndex.toString())

        cleanup:
        topicRepository.deleteAll()
        letterRepository.deleteAll()
    }

    def saveToDatabaseEntities(int amount) {
        for (int i = 0; i < amount; i++) {
            def topic = new Topic.TopicBuilder()
                    .senderId(senderId)
                    .senderUsername(senderUsername)
                    .addresseId(addresseeId)
                    .addresseeUsername(addresseeUsername)
                    .build()

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

            letterRepository.save(letter)
            topic.addLetter(letter)
            topicRepository.save(topic)
        }
    }

    def "hasNewLetters test - should has new letter"() {

    }

    def cleanup() {

    }

}
