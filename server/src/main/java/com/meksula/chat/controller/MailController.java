package com.meksula.chat.controller;

import com.meksula.chat.domain.mailbox.*;
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
    private TopicBroker topicBroker;
    private TopicIndex topicIndex;

    @Autowired
    public void setInternalMail(InternalMail internalMail) {
        this.internalMail = internalMail;
    }

    @Autowired
    public void setTopicBroker(TopicBroker topicBroker) {
        this.topicBroker = topicBroker;
    }

    @Autowired
    public void setTopicIndex(TopicIndex topicIndex) {
        this.topicIndex = topicIndex;
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

    /*
    * Mailing by topic section
    * */

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/topic/new/{title}")
    public Topic createTopic(@RequestBody Letter letter, @PathVariable("title") String title) {
        return topicBroker.createTopic(title, letter);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/topic/{topicId}")
    public Topic sendLetterTopic(@PathVariable("topicId") String topicId, @RequestBody Letter letter) {
        return topicBroker.sendLetter(letter, topicId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/topic/status")
    public boolean hasNewLettersTopic(Authentication authentication) {
        ChatUser user = (ChatUser) authentication.getPrincipal();
        return topicIndex.hasNewLetters(user.getUsername());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/topic/{topicId}")
    public Topic getWholeTopic(@PathVariable("topicId") String topicId) {
        return topicBroker.getTopic(topicId);
    }

    /*
    * This method ask client about current topic letters list size.
    * The method getNewestInTopic(..) returns superplus letters.
    * Example:
    * Client has 13 Letters in Topic. Server has now 15 Letters.
    * -> should be returned 2 extra (differ) letters: 15 - 13 = 2
    * */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/topic/newest/{topicId}/{topicSize}")
    public Topic getNewestInTopic(@PathVariable("topicId") String topicId, @PathVariable("topicSize") int topicSize) {
        return topicBroker.getNewestInTopic(topicId, topicSize);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/topic/list")
    public List<TopicShort> getTopicList(Authentication authentication) {
        ChatUser user = (ChatUser) authentication.getPrincipal();
        return topicIndex.getTopicListByUsername(user.getUsername());
    }

}
