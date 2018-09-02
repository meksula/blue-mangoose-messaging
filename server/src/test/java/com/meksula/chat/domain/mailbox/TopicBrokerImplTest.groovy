package com.meksula.chat.domain.mailbox

import com.meksula.chat.repository.LetterRepository
import com.meksula.chat.repository.TopicRepository
import org.joda.time.LocalDateTime
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

@SpringBootTest
class TopicBrokerImplTest extends Specification {
    private TopicIndex topicIndex = Mockito.mock(TopicIndex.class)

    @Autowired
    private TopicRepository topicRepository

    @Autowired
    private LetterRepository letterRepository

    @Autowired
    private TopicBrokerImpl topicBroker

    private Letter letter
    long senderId = 345553
    String senderUsername = "zosia_samosia"
    long addresseeId = 12452
    String addresseeUsername = "admin"
    LocalDateTime sendTime = LocalDateTime.now()
    String title = "Wiadomość testowa"
    String content = "To tylko wiadomość testowa, nie martw się Zosiu."

    def setup() {
        topicBroker.setTopicIndex(topicIndex)

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

        Mockito.when(topicIndex.isTopicExist()).thenReturn(true)
    }

    def "init new topic test"() {
        when:
        def topic = topicBroker.createTopic("nowy topic", letter)

        then:
        topic.getTopicId() != null
        topic.title == "nowy topic"
        topic.getLetters().size() == 1
    }

    def "send letter to topic test"() {
        setup:
        def topic = topicBroker.createTopic("nowy topic", letter)
        def topicId = topic.getTopicId()
        final String otherTitle = "Zupełnie nowy tytuł"

        when:
        Mockito.verify(topicIndex).indexTopic(topic)
        def letterN = new Letter.LetterBuilder()
                .senderId(senderId)
                .senderUsername(senderUsername)
                .addresseeId(addresseeId)
                .addresseeUsername(addresseeUsername)
                .sendTime(sendTime)
                .unsealed(false)
                .title(otherTitle)
                .content(content)
                .build()

        topicBroker.sendLetter(letterN, topicId)

        then:
        def topicUpdated = topicRepository.findById(topicId).get()
        topicUpdated.getLetters().size() == 2

    }

    def cleanup() {
        topicRepository.deleteAll()
        letterRepository.deleteAll()
    }

}
