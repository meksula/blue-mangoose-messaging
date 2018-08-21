package com.meksula.chat.controller;

import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.domain.user.ContactFind;
import com.meksula.chat.domain.user.registry.StatusRegistry;
import com.meksula.chat.domain.user.search.UserSearcher;
import com.meksula.chat.domain.user.social.ContactAddNotification;
import com.meksula.chat.domain.user.social.Notification;
import com.meksula.chat.domain.user.social.SocialManager;
import com.meksula.chat.repository.ProfilePreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    private ProfilePreferencesRepository profilePreferencesRepository;
    private StatusRegistry statusRegistry;

    @Autowired
    public void setUserSearcher(UserSearcher userSearcher) {
        this.userSearcher = userSearcher;
    }

    @Autowired
    public void setSocialManager(SocialManager socialManager) {
        this.socialManager = socialManager;
    }

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    @Autowired
    public void setStatusRegistry(StatusRegistry statusRegistry) {
        this.statusRegistry = statusRegistry;
    }

    @PostMapping("/contact")
    @ResponseStatus(HttpStatus.OK)
    public Set<ContactFind> lookForUser(@RequestBody ContactSearch contactSearch) {
        return userSearcher.findMatching(contactSearch.getPhrase());
    }

    @PostMapping("/invitation")
    @ResponseStatus(HttpStatus.CREATED)
    public Notification inviteUserToFriend(Authentication auth, @RequestBody ContactSearch contactSearch) {
        return socialManager.inviteToFriends(auth.getPrincipal(), contactSearch.getPhrase());
    }

    @PostMapping("/invitation/response/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    public Notification responseToInvitation(Authentication auth,
                                             @PathVariable("notificationId") long notificationId,
                                             @RequestBody boolean decision) {
        return socialManager.invitationResponse(auth.getPrincipal(), notificationId, decision);
    }

    @GetMapping("/notifications")
    @ResponseStatus(HttpStatus.OK)
    public List<ContactAddNotification> getNotifications(Authentication auth) {
        ChatUser chatUser = (ChatUser) auth.getPrincipal();

        //TODO mechanizm, który będzie sprawdzał czy są nowe powiadomienia.
        return profilePreferencesRepository.findByProfileUsername(chatUser.getUsername()).get().getNotifications();
    }

    @DeleteMapping("/notifications/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeNotification(@PathVariable("notificationId") long notificationId, Authentication authentication) {
        ChatUser logged = (ChatUser) authentication.getPrincipal();

        socialManager.removeNotification(logged, notificationId);
    }

    @GetMapping("/status/friends")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> getMyContactStatus(Authentication authentication) {
        ChatUser chatUser = (ChatUser) authentication.getPrincipal();
        return statusRegistry.getContactStatus(chatUser.getUsername());
    }

}

