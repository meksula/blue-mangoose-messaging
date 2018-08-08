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

    def 'userSearcher should find one entity without paying attention to case size'() {
        setup: "save few entities"
        def usernameUpperCases = "KazikG209"
        def user4 = new ChatUser()
        user4.setUsername(usernameUpperCases) // username should contain upper cases
        def user5 = new ChatUser()
        user5.setUsername("some_insignificant")
        def user6 = new ChatUser()
        user6.setUsername("some_insignificant")
        chatUserRepository.saveAll([user4, user5, user6])

        when:
        chatUserRepository.findByUsername(usernameUpperCases) //should not found anything

        then: "here I'd changed case's size `compare with @param usernameUpperCases`"
        userSearcher.findMatching("kazikg209").size() == 1

        cleanup:
        chatUserRepository.deleteAll([user4, user5, user6])
    }

    def 'userSearcher should find few matched entities similar to few first letters'() {
        setup:
        def user100 = new ChatUser()
        user100.setUsername("kazikg209")
        def user101 = new ChatUser()
        user101.setUsername("kazek2000")
        def user102 = new ChatUser()
        user102.setUsername("kamilek2343")
        chatUserRepository.saveAll([user100, user101, user102])

        expect: "searcher should match another but similar entities, from 4 entities searcher should match only 2"
        userSearcher.findMatching("kazi394939").size() == 2
        chatUserRepository.findMatching("^kaz").size() == 3

        cleanup:
        chatUserRepository.deleteAll([user100, user101, user102])
    }

    void cleanup() {
        chatUserRepository.deleteAll([user1, user2])
    }
}
