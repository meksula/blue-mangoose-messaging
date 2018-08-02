package com.meksula.chat.domain.chat;

import com.meksula.chat.domain.room.ChatAccess;
import com.meksula.chat.domain.room.ChatRoom;

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

public interface ChatAccessValidator {
    boolean permit(ChatAccess chatAccess, ChatRoom chatRoom);
}
