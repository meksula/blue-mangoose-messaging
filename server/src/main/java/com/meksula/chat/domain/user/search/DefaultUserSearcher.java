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
                if (phrase.length() > 0) {
                    this.patterns = designatePattern(phrase);
                    return advancedSearch(contactsMatch, 0);
                }

                return contactsMatch;
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

    /**
     * Recursive invoke, when contactsMatch is still empty
     * */
    private Set<ContactFind> advancedSearch(Set<ContactFind> contactsMatch, int counter) {
        Set<ContactFind> contactFinds = chatUserRepository.findMatching(patterns.get(counter).trim())
                .stream()
                .map(chatUser -> new ContactFind(chatUser.getUserId(), chatUser.getUsername()))
                .collect(Collectors.toSet());

        if (contactsMatch.isEmpty() && counter < patterns.size()) {
            if (counter >= patterns.size() - 1) {
                return contactFinds;
            }
            counter++;
            advancedSearch(contactsMatch, counter);
        }

        return contactFinds;
    }

    /**
     * Notice that lower/upper cases are ignore by default here
     * */
    private List<String> designatePattern(String phrase) {
        List<String> patterns = new ArrayList<>();

        if (phrase.length() < 4) {
            patterns.add(phrase);
        }

        else {
            String phrasePiece = phrase.toLowerCase().substring(0, 4);
            String phrasePiece2 = phrasePiece.toLowerCase().substring(0, 3);
            String phrasePiece3 = phrasePiece.toLowerCase().substring(0, 2);
            patterns.addAll(Arrays.asList("^" + phrasePiece, "^" + phrasePiece2, "^" + phrasePiece3));
        }

        return patterns;
    }

}
