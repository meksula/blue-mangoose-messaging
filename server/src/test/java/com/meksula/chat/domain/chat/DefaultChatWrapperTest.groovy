package com.meksula.chat.domain.chat

import com.meksula.chat.domain.chat.dto.ChatMessage
import com.meksula.chat.domain.chat.dto.Message
import com.meksula.chat.domain.chat.impl.DefaultChatWrapper
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * @Author
 * Karol Meksuła
 * 30-07-2018
 * */

class DefaultChatWrapperTest extends Specification {
    private DefaultChatWrapper wrapper
    private Message message

    void setup() {
        wrapper = new DefaultChatWrapper()
        message = new ChatMessage()

        message.setRoomTarget("Loża_szyderców")
        message.setSendTime(LocalDateTime.now() as String)
        message.setUsernmame("admin")
        message.setContent("Content of message...")
    }

    def 'wrapper should store message correctly'() {
        setup:
        wrapper.captureMessage(message)

        expect:
        wrapper.getAllMessages().size() == 1
    }

    def 'get last message should return really last'() {
        setup:
        wrapper.captureMessage(message)

        def last = new ChatMessage()
        last.setUsernmame("common_user")
        wrapper.captureMessage(last)

        expect:
        wrapper.getLastMessage().getUsernmame() == "common_user"
    }

    def 'pagination size should return correct value'() {
        setup:
        int size = 63

        for (int i = 0; i < size; i++) {
            Message message = new ChatMessage()
            message.setContent("Some message...")
            wrapper.captureMessage(message)
        }

        expect:
        wrapper.pages() == (int) (size / 10)
    }

    def 'messages pagination test - one page should has 10 messages'() {
        setup:
        int size = 32
        fakeMessages(size)

        expect:'All messages has size == 32. First 3 packages has 10 message. Last - 4th package has only 2 last messages.'
        wrapper.pages() == 3
        wrapper.getMessagesByPage(1).size() == 10
        wrapper.getMessagesByPage(2).size() == 10
        wrapper.getMessagesByPage(3).size() == 10

        wrapper.getMessagesByPage(1).get(9).getContent() == "message31"
        wrapper.getMessagesByPage(1).get(0).getContent() == "message22"
        wrapper.getMessagesByPage(1).size() == 10
        wrapper.getLastMessage().content == "message31"

        wrapper.getMessagesByPage(2).get(9).getContent() == "message21"
        wrapper.getMessagesByPage(2).get(0).getContent() == "message12"

        wrapper.getMessagesByPage(3).get(9).getContent() == "message11"
        wrapper.getMessagesByPage(3).get(0).getContent() == "message2"

        wrapper.getMessagesByPage(4).size() == 0
    }

    def 'messages pagination test - should throw last packages. If args == 2, method should return List.size() == 20'() {
        setup:
        int size = 39
        fakeMessages(size)

        expect:
        wrapper.getAllMessages().size() == size
        wrapper.getMessagesFromLastPages(1).size() == 10
        wrapper.getMessagesFromLastPages(1).get(0).content == "message29"
        wrapper.getMessagesFromLastPages(1).get(9).content == "message38"
    }

    def 'if is there less than 10 messages... test'() {
        setup:
        fakeMessages(2)

        expect:
        wrapper.getMessagesFromLastPages(1).size() == 2
    }

    void fakeMessages(int size) {
        for (int i = 0; i < size; i++) {
            ChatMessage message = new ChatMessage()
            message.setId(i)
            message.setContent("message" + i)
            wrapper.captureMessage(message)
        }
    }

    def cleanup() {
        wrapper = new DefaultChatWrapper()
    }

}
