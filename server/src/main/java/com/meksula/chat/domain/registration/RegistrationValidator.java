package com.meksula.chat.domain.registration;

import com.meksula.chat.domain.registration.core.RegistrationForm;

/**
 * @Author
 * Karol Meksuła
 * 21-07-2018
 * */

public interface RegistrationValidator {
    boolean access(RegistrationForm registrationForm);
}
