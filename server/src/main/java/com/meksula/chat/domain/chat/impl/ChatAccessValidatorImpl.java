package com.meksula.chat.domain.chat.impl;

import com.meksula.chat.domain.chat.ChatAccessValidator;
import com.meksula.chat.domain.room.ChatAccess;
import com.meksula.chat.domain.room.ChatRoom;

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

public class ChatAccessValidatorImpl implements ChatAccessValidator {

    @Override
    public boolean permit(ChatAccess chatAccess, ChatRoom chatRoom) throws NullPointerException {
        if (!chatRoom.isPasswordRequired()) {
            return true;
        }

        try {
            if (chatRoom.isPasswordRequired()) {
                return chatAccess.getPassword().equals(chatRoom.getPassword());
            }
        } catch (NullPointerException npo) {
            return false;
        }

        return false;
    }

}
