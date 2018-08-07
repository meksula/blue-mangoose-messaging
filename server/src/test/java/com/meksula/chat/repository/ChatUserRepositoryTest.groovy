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
        chatUser1.username = "user1"
        chatUser1.password = "i23dm23mid"
        chatUser1.email = "karol.mail@gmail.com"

        chatUser2 = new ChatUser()
        chatUser2.username = "user2"
        chatUser2.password = "i23dm23mid"
        chatUser2.email = "karol.mail@gmail.com"

        chatUserRepository.save(chatUser1)
        chatUserRepository.save(chatUser2)
    }

    def 'entities should be saved'() {
        expect:
        chatUserRepository.findAll().size() >= 2
    }

    def 'native SQL query should work correctly'() {
        setup:
        ChatUser user1 = new ChatUser()
        user1.setUsername("karol3k093")

        ChatUser user2 = new ChatUser()
        user2.setUsername("edi293jd")

        chatUserRepository.saveAll([user1, user2])

        expect:
        chatUserRepository.findMatching("^ed").size() == 1

        cleanup:
        chatUserRepository.deleteAll([user1, user2])
    }

    def cleanup() {
        chatUserRepository.deleteAll([chatUser1, chatUser2])
    }

}
