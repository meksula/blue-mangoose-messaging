package com.meksula.chat.repository

import com.meksula.chat.domain.user.ChatUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ChatUserRepositoryTest extends Specification {

    @Autowired
    private ChatUserRepository chatUserRepository

    private ChatUser chatUser1
    private ChatUser chatUser2

    void setup() {
        chatUser1 = new ChatUser()
        chatUser1.password = "i23dm23mid"
        chatUser1.email = "karol.mail@gmail.com"

        chatUser2 = new ChatUser()
        chatUser1.password = "i23dm23mid"
        chatUser1.email = "karol.mail@gmail.com"

        chatUserRepository.save(chatUser1)
        chatUserRepository.save(chatUser2)
    }

    def 'entities should be saved'() {
        expect:
        chatUserRepository.findAll().size() == 2
    }

    def cleanup() {
        chatUserRepository.deleteAll()
    }

}
