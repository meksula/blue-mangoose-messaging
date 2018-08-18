package com.meksula.chat.domain.user.registry;

import com.meksula.chat.domain.user.ChatUser;

import java.util.Map;
import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 18-08-2018
 * */

public interface StatusRegistry {
    void loginUser(ChatUser chatUser);

    void logoutUser(ChatUser chatUser);

    Set<String> getStatusSet();

    boolean isLoggedNow(String username);

    Map<String, Boolean> getContactStatus(String username);
}
