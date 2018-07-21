package com.meksula.chat.domain.registration;

import com.meksula.chat.domain.registration.core.RegistrationForm;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

public interface RegistrationService {
    boolean registerUser(RegistrationForm registrationForm);
}
