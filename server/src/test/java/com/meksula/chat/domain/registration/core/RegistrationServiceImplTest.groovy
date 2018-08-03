package com.meksula.chat.domain.registration.core

import com.meksula.chat.domain.registration.RegistrationService
import com.meksula.chat.domain.user.ChatUser
import com.meksula.chat.repository.ChatUserRepository
import com.meksula.chat.repository.ProfilePreferencesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

@SpringBootTest
class RegistrationServiceImplTest extends Specification {

    @Autowired
    private ChatUserRepository chatUserRepository

    @Autowired
    private ProfilePreferencesRepository profilePreferencesRepository

    @Autowired
    private RegistrationService registrationService

    def 'check is userId not zero or null after save()'() {
        setup:
        ChatUser chatUser = new ChatUser()
        ChatUser saved = chatUserRepository.save(chatUser)

        expect:
        saved.getUserId() > 0
        println(saved.getUserId())
    }

    def 'user should be registered test'() {
        setup:
        RegistrationForm form = new ChatUserForm()
        form.setUsername("commonUser")
        form.setPassword("userspassword")
        form.setEmail("users.email@gmail.com")
        form.setPasswordConfirmation("userspassword")

        expect:
        registrationService.registerUser(form)
    }

    def 'user should NOT be registered test'() {
        setup: 'not equivalent passwords'
        RegistrationForm form = new ChatUserForm()
        form.setUsername("commonUser")
        form.setPassword("userspas3sword")
        form.setEmail("users.email@gmail.com")
        form.setPasswordConfirmation("userspassword")

        expect:
        !registrationService.registerUser(form)
    }

    void cleanup() {
        chatUserRepository.deleteAll()
        profilePreferencesRepository.deleteAll()
    }

}
