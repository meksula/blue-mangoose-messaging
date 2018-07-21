package com.meksula.chat.controller;

import com.meksula.chat.domain.registration.core.ChatUserForm;
import com.meksula.chat.domain.registration.RegistrationService;
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

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    public boolean register(@RequestBody ChatUserForm chatUserForm) {
        return registrationService.registerUser(chatUserForm);
    }

    @GetMapping("/test")
    public String accessTest() {
        return "Masz dostęp.";
    }

}
