package com.meksula.chat.domain.chat.impl

import com.meksula.chat.domain.chat.ChatRoomManager
import com.meksula.chat.domain.chat.ChatWrapper
import com.meksula.chat.domain.chat.dto.ChatMessage
import com.meksula.chat.domain.chat.dto.Message
import com.meksula.chat.domain.room.ChatForm
import com.meksula.chat.domain.room.ChatRoom
import org.joda.time.LocalDateTime
import org.joda.time.Minutes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 31-07-2018
 * */

@SpringBootTest
class DefaultResourcesCollectorTest extends Specification {

    @Autowired
    private DefaultResourcesCollector collector

    @Autowired
    private ChatRoomManager manager

    void setup() {
    }

    def 'injection test'() {
        expect:
        collector != null
        manager != null
    }

    def 'properties inject'() {
        expect:
        collector.deadMinutes > 0
    }

    def 'memory measure test'() {
        expect:
        long memory = Runtime.getRuntime().freeMemory()
        int memoryTaken = (int) ((memory / 1024) / 1024)
        println(memoryTaken)

        long allocatedMemory = Runtime.getRuntime().totalMemory()
        println("ALOCATED MEMORY [MB] : " + (allocatedMemory / 1024) / 1024)

        long maxMemory = Runtime.getRuntime().maxMemory()
        println(maxMemory)
        println("MAX JVM MEMORY [MB] : " + (maxMemory / 1024) / 1024)
    }

    def 'joda time test'() {
        expect:
        int time = 50
        LocalDateTime first = LocalDateTime.now()
        LocalDateTime updated = first.plusMinutes(time)

        Minutes miuntes = Minutes.minutesBetween(LocalDateTime.now(), updated)
        miuntes.getMinutes() == time - 1
    }

    def 'detect inactive ChatRoom test'() {
        setup:
        ChatForm chatForm = new ChatForm("default", "admin", false, "")
        ChatForm chatForm2 = new ChatForm("default2", "admin2", false, "")
        manager.registerChatWrapper(chatForm)
        manager.registerChatWrapper(chatForm2)
        manager.getChatMap().size() == 2

        LocalDateTime ts = manager.getChatMap().get("default").getInitTimestamp()
        LocalDateTime updatedTs = ts.plusMinutes(190) // set minutes over 'dead' parameter
        manager.getChatMap().get("default").setInitTimestamp(updatedTs)

        when:
        collector.updateResources()
        collector.detectInactiveRooms()

        then: 'one of setted ChatRoooms should be kicked off'
        manager.getChatMap().size() == 1
    }

    def 'messages overflow detector test'() {
        setup: 'in this step I create 150 messages and throw them to ROOM'
        def ROOM = "some_room"
        ChatForm chatForm = new ChatForm(ROOM, "admin", false, "")
        manager.registerChatWrapper(chatForm)

        collector.messagesLimit == 100

        for (int i = 0; i < 150; i++) {
            Message message = new ChatMessage()
            message.setRoomTarget(ROOM)
            message.setContent("message" + i)
            manager.receiveMessage(message)
        }

        expect: 'collector should left only 100 newest messages'
        ChatWrapper wrapper = manager.getChatMap().get(ROOM)
        wrapper != null
        wrapper.getChatRoom() != null

        collector.detectMessagesOverflow()
        wrapper.getAllMessages().size() == 100
        wrapper.getAllMessages().get(0).getContent() == "message50"
        wrapper.getAllMessages().get(99).getContent() == "message149"
    }

}
