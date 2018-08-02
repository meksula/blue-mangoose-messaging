package com.meksula.chat.domain.chat.impl

import com.meksula.chat.domain.chat.ChatAccessValidator
import com.meksula.chat.domain.room.ChatAccess
import com.meksula.chat.domain.room.ChatRoom
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

class ChatAccessValidatorImplTest extends Specification {
    private ChatAccessValidator chatAccessValidator
    private ChatAccess chatAccess
    private ChatAccess nonGrata
    private ChatRoom chatRoom
    private ChatRoom securedChatRoom
    private ChatRoom securedFamiliarChatRoom

    void setup() {
        chatAccessValidator = new ChatAccessValidatorImpl()

        chatAccess = new ChatAccess()
        nonGrata = new ChatAccess()

        chatRoom = new ChatRoom()
        chatRoom.setId(21525)
        chatRoom.setCreatorUsername("free_man")
        chatRoom.setName("free_room")
        chatRoom.setPasswordRequired(false)

        securedChatRoom = new ChatRoom()
        securedChatRoom.setId(1002)
        securedChatRoom.setCreatorUsername("creator_username")
        securedChatRoom.setName("some_chat_room")
        securedChatRoom.setPasswordRequired(true)
        securedChatRoom.setPassword("password")

        securedFamiliarChatRoom = new ChatRoom()
        securedFamiliarChatRoom.setId(2414)
        securedFamiliarChatRoom.setCreatorUsername("other_creator")
        securedFamiliarChatRoom.setName("accessible_room")
        securedFamiliarChatRoom.setPasswordRequired(true)
        securedFamiliarChatRoom.setPassword("access")
    }

    def 'insecure, free room should able to read messages'() {
        expect:
        chatAccessValidator.permit(chatAccess, chatRoom)
    }

    def 'secured room, user do not know password'() {
        setup:
        chatAccess.setPassword("uncorrect_password")

        expect:
        !chatAccessValidator.permit(chatAccess, securedChatRoom)
    }

    def 'secured room, but user know correct password'() {
        setup:
        chatAccess.setPassword("access")

        expect:
        chatAccessValidator.permit(chatAccess, securedFamiliarChatRoom)
        !chatAccessValidator.permit(nonGrata, securedFamiliarChatRoom)
    }

}
