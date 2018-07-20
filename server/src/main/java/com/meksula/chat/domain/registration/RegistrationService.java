package com.meksula.chat.domain.registration;

import com.meksula.chat.domain.registration.core.ChatUserForm;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

public interface RegistrationService {
    boolean registerUser(ChatUserForm chatUserForm);
}
