package com.meksula.chat.domain.user.search

import com.meksula.chat.domain.user.ChatUser
import com.meksula.chat.repository.ChatUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 07-08-2018
 * */

@SpringBootTest
class DefaultUserSearcherTest extends Specification {

    @Autowired
    UserSearcher userSearcher

    @Autowired
    ChatUserRepository chatUserRepository

    @Shared
    def user1 = new ChatUser()

    @Shared
    def user2 = new ChatUser()

    def username = "kazik22d"
    def usernameToRegexSearch = "adamm2394c"

    void setup() {
        user1.setUsername("user1")
        user2.setUsername(username)

        user1 = chatUserRepository.save(user1)
        user2 = chatUserRepository.save(user2)
    }

    def 'should be found by userId'() {
        expect:
        userSearcher.findMatching(String.valueOf(user1.getUserId())).size() == 1
    }

    def 'should be found by username'() {
        expect:
        userSearcher.findMatching(username).size() == 1
    }

    /**
     * In this case, repository can't find any entity, so it has to trigger another method,
     * in order to generate some REGEX to much a similar results.
     * */
    def 'default repository should not find any entity'() {
        expect:
        !chatUserRepository.findByUsername(usernameToRegexSearch).isPresent()
    }

    /**
     * Above method should test correct way to design most accurate regex
     * */
    def 'userSearcher should find few matched entities'() {

    }

    void cleanup() {
        chatUserRepository.deleteAll([user1, user2])
    }
}
