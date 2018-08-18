package com.meksula.chat.controller;

import com.meksula.chat.domain.registration.core.ChatUserForm;
import com.meksula.chat.domain.registration.RegistrationService;
import com.meksula.chat.domain.registration.verification.UserVerification;
import com.meksula.chat.domain.registration.verification.VerificationEntity;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.domain.user.registry.StatusRegistry;
import com.meksula.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

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
    private StatusRegistry statusRegistry;

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

    @Autowired
    public void setStatusRegistry(StatusRegistry statusRegistry) {
        this.statusRegistry = statusRegistry;
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
        statusRegistry.loginUser((ChatUser) authentication.getPrincipal());
        return authentication.isAuthenticated();
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(Authentication authentication) {
        statusRegistry.logoutUser((ChatUser) authentication.getPrincipal());
    }

    @GetMapping("/logged/{username}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isOnline(@PathVariable("username") String username) {
        return statusRegistry.isLoggedNow(username);
    }

    @GetMapping("/logged/all")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> onlineUsers() {
        return statusRegistry.getStatusSet();
    }

    @GetMapping("/logged/contacts")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> onlineUsersByContacts(Authentication authentication) {
        ChatUser chatUser = (ChatUser) authentication.getPrincipal();
        return statusRegistry.getContactStatus(chatUser.getUsername());
    }

    @GetMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ChatUser getChatUser(@PathVariable("username") String username) {
        //TODO dodatkowa weryfikacja

        return chatUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username  + "not found."));
    }

}
