package com.meksula.chat.controller;

import com.meksula.chat.domain.mailbox.InternalMail;
import com.meksula.chat.domain.mailbox.Letter;
import com.meksula.chat.domain.user.ChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {
    private InternalMail internalMail;

    @Autowired
    public void setInternalMail(InternalMail internalMail) {
        this.internalMail = internalMail;
    }

    @GetMapping("/all/received")
    @ResponseStatus(HttpStatus.OK)
    public List<Letter> getAllReceivedLetters(Authentication authentication) {
        ChatUser chatUser = (ChatUser) authentication.getPrincipal();
        return internalMail.getAllLettersInMailboxByUsername(chatUser.getUsername());
    }

    @GetMapping("/all/sent")
    @ResponseStatus(HttpStatus.OK)
    public List<Letter> getAllSentLetters(Authentication authentication) {
        ChatUser chatUser = (ChatUser) authentication.getPrincipal();
        return internalMail.getAllSentMessages(chatUser.getUsername());
    }

    @GetMapping("/last/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public List<Letter> getLastNLetters(Authentication authentication, @PathVariable("amount") int amount) {
        ChatUser chatUser = (ChatUser) authentication.getPrincipal();
        return internalMail.getLastNReceivedLetters(chatUser.getUsername(), amount);
    }

    @GetMapping("/id/{letterId}")
    @ResponseStatus(HttpStatus.OK)
    public Letter getConcreteLetter(@PathVariable("letterId") String letterId) {
        return internalMail.getConcreteLetter(letterId);
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public boolean isNewLetters(Authentication authentication) {
        ChatUser chatUser = (ChatUser) authentication.getPrincipal();
        return internalMail.isNewLetters(chatUser.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Letter sendLetter(@RequestBody Letter letter) {
        return internalMail.sendLetter(letter);
    }

}
