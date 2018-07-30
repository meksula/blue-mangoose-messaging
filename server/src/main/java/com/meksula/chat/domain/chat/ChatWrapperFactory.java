package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.room.ChatForm;

/**
 * @Author
 * Karol Meksuła
 * 30-07-2018
 * */

public interface ChatWrapperFactory {
    ChatWrapper buildChatWrapper(ChatForm chatForm);
}
