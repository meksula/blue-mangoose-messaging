package com.meksula.chat.domain.user

import com.meksula.chat.repository.ChatUserRepository
import com.meksula.chat.repository.ContactRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 01-08-2018
 * */

@SpringBootTest
class ChatUserTest extends Specification {
    private ChatUser chatUser

    @Autowired
    private ChatUserRepository chatUserRepository

    @Autowired
    private ContactRepository contactRepository

    private final String USERNAME = "admin"


    void setup() {
        chatUser = new ChatUser()
        chatUser.setUsername(USERNAME)
        chatUserRepository.save(chatUser)
        ChatUser fetchedUser = chatUserRepository.findByUsername(USERNAME).get()
        Contact contact = new Contact()
        //contact.setContactId(32)
        contact.setContactUsername("admin")
        contact.setChatUser(fetchedUser)
        fetchedUser.addContact(contact)
        chatUserRepository.save(fetchedUser)
    }

    def 'add and persist contacts test'() {
        setup: 'check if user is persisted - get empty contact list'
        ChatUser fetchedUser = chatUserRepository.findByUsername(USERNAME).get()
        Set<Contact> contacts = fetchedUser.getContacts()

        when: 'add 2 contacts'
        contacts.size() == 0
        Contact contact = new Contact()
        contact.setContactUsername("olcia94")

        Contact contact1 = new Contact()
        contact1.setContactUsername("henio192d")

        fetchedUser.addContact(contact)
        fetchedUser.addContact(contact1)
        fetchedUser.getContacts().size() == 2
        chatUserRepository.deleteById(fetchedUser.getUserId())

        chatUserRepository.save(fetchedUser)

        then: 'check if contact book has suitable size'
        ChatUser updatedUser = chatUserRepository.findByUsername(USERNAME).get()
        //updatedUser.getContacts().size() == 2
    }

    /*void cleanup() {
        chatUserRepository.deleteAll()
        contactRepository.deleteAll()
    }
*/
    def 'ChatUser entity save test'() {
        setup:
        ChatUser chatUser = new ChatUser()
        chatUser.setUsername(USERNAME)
        chatUser.setEmail("email@email.com")

        Contact contact = new Contact()
        contact.setContactUsername("usernmae")
        contact.setOwnerId(232)
        chatUser.setContactsBook(new HashSet<Contact>())
        //chatUser.addContact(contact)

        chatUserRepository.save(chatUser)

        ChatUser updated = chatUserRepository.findByUsername(USERNAME).get()
        updated.addContact(contact)

        //chatUserRepository.deleteById(chatUser.getUserId())
        //expect:
        contact.setChatUser(updated)
        contactRepository.save(contact)

        updated.getContacts().size() == 1
        println(updated.getUserId())

        //chatUserRepository.save(updated)


    }

    def 'save contact test'() {
        setup:
        Contact contact = new Contact()
        contact.setContactUsername("consss")

        expect:
        contactRepository.save(contact)
    }

}
