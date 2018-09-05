package com.meksula.chat.domain.mailbox

import com.meksula.chat.repository.LetterRepository
import com.meksula.chat.repository.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.method.P
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 05-09-2018
 * */

@SpringBootTest
class MessagingFlowTest extends Specification {
    @Autowired private TopicBrokerImpl topicBroker
    @Autowired private TopicIndexImpl topicIndex

    @Autowired private LetterRepository letterRepository
    @Autowired private TopicRepository topicRepository

    long senderId = 3453
    long addresseeId = 35545
    String senderUsername = "krolewna_sniezka"
    String addresseeUsername = "krasnoludek"
    String title = "default_title1"
    String content = "some content. Default message."

    def "example, basic flow test"() {
        setup:
        Topic topic = topicBroker.createTopic("Flow topic", buildLetter())

        when:
        topicIndex.indexReport().get(TopicIndexImpl.LETTER_INDEX) == 1
        topicIndex.isTopicExist(topic.getTopicId())
        topicIndex.getTopicListByUsername(addresseeUsername).size() == 1
        topicIndex.getTopicListByUsername(senderUsername).size() == 1

        then:
        topicBroker.getNewestInTopic(topic.getTopicId(), 0).size() == 1
        def letter = new Letter.LetterBuilder()
                .senderId(addresseeId)
                .addresseeId(senderId)
                .senderUsername(addresseeUsername)
                .addresseeUsername(senderUsername)
                .title("Fresh title")
                .content(content)
                .build()

        def topicUpdated = topicBroker.sendLetter(letter, topic.getTopicId())
        topicUpdated.getLetters().size() == 2
        topicIndex.indexReport().get(TopicIndexImpl.LETTER_INDEX) == 2
        topicIndex.isTopicExist(topic.getTopicId())
        topicIndex.getTopicListByUsername(addresseeUsername).size() == 2
        topicIndex.getTopicListByUsername(senderUsername).size() == 2

        topicIndex.hasNewLetters(addresseeUsername)
        topicBroker.getNewestInTopic(topicUpdated.getTopicId(), 0).size() == 1
        topicBroker.sendLetter(buildLetter(this.title), topicUpdated.getTopicId()).getLetters().size() == 3
        sleep(1000)
        def newestTopic = topicBroker.sendLetter(buildLetter("Newest"), topicUpdated.getTopicId())
        newestTopic.getLetters().size() == 4

        def list = topicBroker.getNewestInTopic(newestTopic.getTopicId(), 0)
        list.size() == 3
        //list.get(list.size() - 1).getTitle() == "Newest"
        for (Letter letter1 : newestTopic.getLetters()) {
            println(letter1)
        }
    }

    Letter buildLetter(String title) {
        return new Letter.LetterBuilder()
                .senderId(senderId)
                .addresseeId(addresseeId)
                .senderUsername(senderUsername)
                .addresseeUsername(addresseeUsername)
                .title(title)
                .content(content)
                .build()
    }

    def cleanup() {
        topicRepository.deleteAll()
        letterRepository.deleteAll()
    }

}
