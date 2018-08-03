package com.meksula.chat.controller;

import com.meksula.chat.domain.registration.core.ChatUserForm;
import com.meksula.chat.domain.registration.RegistrationService;
import com.meksula.chat.domain.registration.verification.UserVerification;
import com.meksula.chat.domain.registration.verification.VerificationEntity;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    private RegistrationService registrationService;
    private UserVerification userVerification;
    private ChatUserRepository chatUserRepository;

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Autowired
    public void setUserVerification(UserVerification userVerification) {
        this.userVerification = userVerification;
    }

    @Autowired
    public void setChatUserRepository(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    public boolean register(@RequestBody ChatUserForm chatUserForm) {
        return registrationService.registerUser(chatUserForm);
    }

    @PostMapping("/verification")
    @ResponseStatus(HttpStatus.OK)
    public boolean accountVerification(@RequestBody VerificationEntity verificationEntity) {
        return userVerification.verify(verificationEntity);
    }

    @GetMapping("/verification/{userId}/{code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean accountVerification(@PathVariable("userId") long userId,
                                       @PathVariable("code") String code) {
        return userVerification.verify(new VerificationEntity(userId, code));
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public boolean login(Authentication authentication) {
        return authentication.isAuthenticated();
    }

    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ChatUser getChatUser(@PathVariable("username") String username) {
        //TODO dodatkowa weryfikacja

        return chatUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username  + "not found."));
    }

}
