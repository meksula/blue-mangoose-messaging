package com.meksula.chat.controller;

import com.meksula.chat.domain.registration.core.ChatUserForm;
import com.meksula.chat.domain.registration.RegistrationService;
import com.meksula.chat.domain.registration.verification.UserVerification;
import com.meksula.chat.domain.registration.verification.VerificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Autowired
    public void setUserVerification(UserVerification userVerification) {
        this.userVerification = userVerification;
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

}
