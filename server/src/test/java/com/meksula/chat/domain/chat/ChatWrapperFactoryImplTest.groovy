package com.meksula.chat.domain.chat

import com.meksula.chat.domain.chat.impl.ChatWrapperFactoryImpl
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 30-07-2018
 * */

class ChatWrapperFactoryImplTest extends Specification {

    private ChatWrapperFactoryImpl factory

    def setup() {
        factory = new ChatWrapperFactoryImpl()
    }


}
