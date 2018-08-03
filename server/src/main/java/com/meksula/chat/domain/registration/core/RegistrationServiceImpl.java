package com.meksula.chat.domain.registration.core;

import com.meksula.chat.domain.registration.RegistrationService;
import com.meksula.chat.domain.registration.verification.UserVerification;
import com.meksula.chat.domain.user.ApplicationUser;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.domain.user.ProfilePreferences;
import com.meksula.chat.repository.ChatUserRepository;
import com.meksula.chat.repository.ProfilePreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private ChatUserRepository chatUserRepository;
    private ProfilePreferencesRepository profilePreferencesRepository;
    private FormValidator formValidator;
    private DatabaseValidator databaseValidator;
    private UserVerification userVerification;

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

    @Autowired
    public void setUserVerification(UserVerification userVerification) {
        this.userVerification = userVerification;
    }

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    @Override
    public boolean registerUser(final RegistrationForm registrationForm) {
        boolean accessForm = formValidator.access(registrationForm);
        boolean accessDb = databaseValidator.access(registrationForm);

        if (accessForm && accessDb) {
            ChatUser chatUser = buildChatUser((ChatUserForm) registrationForm);
            ChatUser saved = chatUserRepository.save(chatUser);
            makeVerifProcess(chatUser);
            createProfile(saved.getUserId(), saved.getUsername());
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

    private void makeVerifProcess(ApplicationUser applicationUser) {
        userVerification.makeVerificationProcess(applicationUser);
    }

    private void createProfile(final long userId, final String username) {
        ProfilePreferences preferences = new ProfilePreferences();
        preferences.setContactsBook(new HashSet<>());
        preferences.setUserId(userId);
        preferences.setProfileUsername(username);

        profilePreferencesRepository.save(preferences);
    }

}
