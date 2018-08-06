package com.meksula.chat.domain.user.search;

import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

/**
 * @Author
 * Karol Meksuła
 * 06-08-2018
 * */

@Service
public class DefaultUserSearcher implements UserSearcher {
    private ChatUserRepository chatUserRepository;
    private Long userId;

    @Autowired
    public void setChatUserRepository(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public Set<Map<Long, String>> findMatching(String phrase) {
        ChatUser chatUser;

        if (searchByUserId(phrase) != null) {
            chatUser = chatUserRepository.findByUserId(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Not found user with this arg: " + phrase));
        }

        else {
            chatUser = chatUserRepository.findByUsername(phrase)
                    .orElseThrow(() -> new EntityNotFoundException("Not found user with this arg: " + phrase));;
        }

        return mapToSet(chatUser);
    }

    private Long searchByUserId(String phrase) {
        try {
            userId = Long.parseLong(phrase);
            return userId;
        } catch (Exception e) {
            return null;
        }
    }

    private Set<Map<Long, String>> mapToSet(ChatUser chatUser) {
        Set<Map<Long, String>> set = new HashSet<>();
        Map<Long, String> map = new HashMap<>();

        map.put(chatUser.getUserId(), chatUser.getUsername());
        set.add(map);

        return set;
    }

    private Set<Map<Long, String>> mapToSet(List<ChatUser> chatUserList) {
        Set<Map<Long, String>> set = new HashSet<>();

        chatUserList.forEach(chatUser -> {
            Map<Long, String> map = new HashMap<>();
            map.put(chatUser.getUserId(), chatUser.getUsername());
            set.add(map);
        });

        return set;
    }

}
