package com.meksula.chat.controller;

import com.meksula.chat.domain.user.ContactFind;
import com.meksula.chat.domain.user.search.UserSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 06-08-2018
 * */

@RestController
@RequestMapping("/api/v1/contact")
public class ContactSearchController {
    private UserSearcher userSearcher;

    @Autowired
    public void setUserSearcher(UserSearcher userSearcher) {
        this.userSearcher = userSearcher;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<ContactFind> lookForUser(@RequestBody String phrase) {
        return userSearcher.findMatching(phrase);
    }

}
