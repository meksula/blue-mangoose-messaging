package com.meksula.chat.domain.registration.core;

import com.meksula.chat.domain.registration.RegistrationValidator;
import com.meksula.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author
 * Karol Meksuła
 * 21-07-2018
 * */

@Service
public class DatabaseValidator implements RegistrationValidator {
    private ChatUserRepository chatUserRepository;

    @Autowired
    public void setChatUserRepository(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public boolean access(RegistrationForm registrationForm) {
        return !chatUserRepository.findByUsername(registrationForm.getUsername()).isPresent();
    }

}
