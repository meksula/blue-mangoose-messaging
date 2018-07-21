package com.meksula.chat.domain.registration.core;

import com.meksula.chat.domain.registration.RegistrationService;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private ChatUserRepository chatUserRepository;
    private FormValidator formValidator;
    private DatabaseValidator databaseValidator;

    public RegistrationServiceImpl() {
        this.formValidator = new FormValidator();
    }

    @Autowired
    public void setDatabaseValidator(DatabaseValidator databaseValidator) {
        this.databaseValidator = databaseValidator;
    }

    @Autowired
    public void setChatUserRepository(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public boolean registerUser(final RegistrationForm registrationForm) {
        boolean accessForm = formValidator.access(registrationForm);
        boolean accessDb = databaseValidator.access(registrationForm);

        if (accessForm && accessDb) {
            ChatUser chatUser = buildChatUser((ChatUserForm) registrationForm);
            chatUserRepository.save(chatUser);
            return true;
        }

        return false;
    }

    private ChatUser buildChatUser(ChatUserForm chatUserForm) {
        ChatUser chatUser = new ChatUser();
        chatUser.setUsername(chatUserForm.getUsername());
        chatUser.setPassword(new BCryptPasswordEncoder().encode(chatUserForm.getPassword()));
        chatUser.setEmail(chatUserForm.getEmail());
        chatUser.setAuthorities(Collections.singleton("chatter"));
        chatUser.setEnable(false);
        return chatUser;
    }

}
