package com.meksula.chat.domain.mailbox

import com.meksula.chat.repository.LetterRepository
import com.meksula.chat.repository.TopicRepository
import groovy.util.logging.Slf4j
import org.joda.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 03-09-2018
 * */

@Slf4j
@SpringBootTest
class TopicIndexImplTest extends Specification {

    @Autowired
    private TopicIndexImpl topicIndex

    @Autowired
    private TopicBrokerImpl topicBroker

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
        letter = new Letter.LetterBuilder()
                .senderId(senderId)
                .senderUsername(senderUsername)
                .addresseeId(addresseeId)
                .addresseeUsername(addresseeUsername)
                .sendTime(sendTime)
                .unsealed(unsealed)
                .title(title)
                .content(content)
                .build()

        def topic = topicBroker.createTopic("topic_title", letter)
        topicIndex.updateIndexes()

        then:
        topicRepository.findById(topic.getTopicId())
        def map = topicIndex.indexReport()
        map.get(TopicIndexImpl.TOPIC_SHORT_SET) == 1
        map.get(TopicIndexImpl.LETTER_INDEX) == 1
        map.get(TopicIndexImpl.FRESH_LETTERS_BY_USERNAME) == 1
    }

    def "get Topic List by username"() {
        when:
        for (int i = 0; i < 10; i++) {
            String addresse
            if (i % 2 == 0) {
                addresse = "First_user"
            } else {
                addresse = "Second_user"
            }
            letter = new Letter.LetterBuilder()
                    .senderId(senderId)
                    .senderUsername(senderUsername)
                    .addresseeId(addresseeId)
                    .addresseeUsername(addresse)
                    .sendTime(sendTime)
                    .unsealed(unsealed)
                    .title(title)
                    .content(content)
                    .build()
            topicBroker.createTopic("topic_title", letter)
        }
        topicIndex.updateIndexes()

        then:
        topicIndex.getTopicListByUsername("First_user").size() == 5
        topicIndex.getTopicListByUsername("Second_user").size() == 5
    }

    def "is topic exist test"() {
        when:
        String topicId = ""
        for (int i = 0; i < 10; i++) {
            letter = new Letter.LetterBuilder()
                    .senderId(senderId)
                    .senderUsername(senderUsername)
                    .addresseeId(addresseeId)
                    .addresseeUsername(addresseeUsername)
                    .sendTime(sendTime)
                    .unsealed(unsealed)
                    .title(title)
                    .content(content)
                    .build()
            def topic = topicBroker.createTopic("topic_title", letter)

            if (i == 7) {
                topicId = topic.getTopicId()
            }
        }
        topicIndex.updateIndexes()

        then:
        topicIndex.isTopicExist(topicId)
        !topicIndex.isTopicExist(topicId + "bf")
    }

    def "hasNewLetters test - should has new letter"() {

    }

    def "delete from index test" () {
        setup:
        def id = "2m9d3293m"
        def topic = new Topic()
        topic.topicId = id
        topicIndex.indexTopic(topic)
        def topicShorten = new TopicShortInfo(id)

        expect:
        topicShorten == new TopicShortInfo(topicShorten.getTopicId())
    }

    def cleanup() {
        topicRepository.deleteAll()
        letterRepository.deleteAll()
    }

}
