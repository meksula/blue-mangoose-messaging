package com.meksula.chat.domain.chat.impl

import com.meksula.chat.domain.chat.ChatWrapper
import com.meksula.chat.domain.room.ChatForm
import com.meksula.chat.domain.room.ChatRoom
import com.meksula.chat.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 31-07-2018
 * */

@SpringBootTest
class ChatWrapperFactoryImplTest extends Specification {

    @Autowired
    private ChatWrapperFactoryImpl chatWrapperFactory

    @Autowired
    private ChatRoomRepository repository

    private ChatRoom chatRoom
    private ChatForm chatForm
    private String name = "room_name"
    private String username = "admin"

    void setup() {
        chatRoom = new ChatRoom()
        chatRoom.setName(name)
        chatRoom.setCreatorUsername(username)

        repository.save(chatRoom)

        chatForm = new ChatForm(name, username, false, "")
    }

    def 'initialization of new Wrapper instance test'() {
        expect: 'initialize new, fresh instance'
        ChatWrapper wrapper = chatWrapperFactory.buildChatWrapper(chatForm)
        wrapper.getChatRoom() != null
        wrapper.getChatRoom().getName() == name

        repository.findAll().size() == 1
    }

    def 'initialization of inactive old Wrapper instance test'() {
        setup:
        ChatForm form = new ChatForm("not_exist", "other", false, "")

        expect:
        ChatWrapper wrapper = chatWrapperFactory.buildChatWrapper(form)
        wrapper.getChatRoom() != null
    }

    void cleanup() {
        repository.deleteAll()
    }
}
