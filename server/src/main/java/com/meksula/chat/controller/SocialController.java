package com.meksula.chat.controller;

import com.meksula.chat.domain.user.ContactFind;
import com.meksula.chat.domain.user.search.UserSearcher;
import com.meksula.chat.domain.user.social.Notification;
import com.meksula.chat.domain.user.social.SocialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 06-08-2018
 * */

@RestController
@RequestMapping("/api/v1/social")
public class SocialController {
    private UserSearcher userSearcher;
    private SocialManager socialManager;

    @Autowired
    public void setUserSearcher(UserSearcher userSearcher) {
        this.userSearcher = userSearcher;
    }

    @Autowired
    public void setSocialManager(SocialManager socialManager) {
        this.socialManager = socialManager;
    }

    @PostMapping("/contact")
    @ResponseStatus(HttpStatus.OK)
    public Set<ContactFind> lookForUser(@RequestBody String phrase) {
        return userSearcher.findMatching(phrase);
    }

    @PostMapping("/invitation")
    @ResponseStatus(HttpStatus.OK)
    public Notification inviteUserToFriend(Authentication auth, @RequestBody String friendsUsername) {
        return socialManager.inviteToFriends(auth.getPrincipal(), friendsUsername);
    }

}
