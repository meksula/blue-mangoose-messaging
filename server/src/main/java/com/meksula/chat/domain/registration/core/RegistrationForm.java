package com.meksula.chat.domain.registration.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author
 * Karol Meksuła
 * 21-07-2018
 * */

@Getter
@Setter
public abstract class RegistrationForm {
    private String username;
    private String email;
    private String password;
    private String passwordConfirmation;
}
