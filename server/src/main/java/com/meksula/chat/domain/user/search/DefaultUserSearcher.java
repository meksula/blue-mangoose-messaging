package com.meksula.chat.domain.user.search;

import com.meksula.chat.domain.user.ContactFind;
import com.meksula.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author
 * Karol Meksuła
 * 06-08-2018
 * */

@Service
public class DefaultUserSearcher implements UserSearcher {
    private ChatUserRepository chatUserRepository;
    private Long userId;
    private List<String> patterns;

    @Autowired
    public void setChatUserRepository(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public Set<ContactFind> findMatching(String phrase) {
        Set<ContactFind> contactsMatch = new HashSet<>();

        if (searchByUserId(phrase) != null) {
            chatUserRepository.findByUserId(userId)
                    .ifPresent(chatUser ->
                            contactsMatch.add(new ContactFind(chatUser.getUserId(), chatUser.getUsername())));

        } else {
            chatUserRepository.findByUsername(phrase)
                    .ifPresent(user ->
                            contactsMatch.add(new ContactFind(user.getUserId(), user.getUsername())));

            if (contactsMatch.isEmpty()) {
                this.patterns = designatePattern(phrase);

                advancedSearch(contactsMatch, phrase, 0);
            }
        }

        return contactsMatch;
    }

    private Long searchByUserId(String phrase) {
        try {
            userId = Long.parseLong(phrase);
            return userId;
        } catch (Exception e) {
            return null;
        }
    }

    private Set<ContactFind> advancedSearch(Set<ContactFind> contactsMatch, String phrase, int counter) {

        Set<ContactFind> contactFinds = chatUserRepository.findMatching(patterns.get(counter))
                .stream()
                .map(chatUser -> new ContactFind(chatUser.getUserId(), chatUser.getUsername()))
                .collect(Collectors.toSet());

        if (contactsMatch.isEmpty() && counter < patterns.size()) {
            advancedSearch(contactsMatch, phrase, counter);
        }

        return contactFinds;
    }

    private List<String> designatePattern(String phrase) {
        List<String> patterns = new ArrayList<>();
        
        //TODO

        return patterns;
    }

}
