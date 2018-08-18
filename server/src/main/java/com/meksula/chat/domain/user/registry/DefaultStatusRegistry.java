package com.meksula.chat.domain.user.registry;

import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.repository.ProfilePreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 18-08-2018
 * */

@Service
public class DefaultStatusRegistry implements StatusRegistry {
    private Set<String> statusSet;
    private ProfilePreferencesRepository profilePreferencesRepository;

    public DefaultStatusRegistry() {
        this.statusSet = new HashSet<>();
    }

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    @Override
    public void loginUser(ChatUser chatUser) {
        statusSet.add(chatUser.getUsername());
    }

    @Override
    public void logoutUser(ChatUser chatUser) {
        statusSet.remove(chatUser.getUsername());
    }

    @Override
    public Set<String> getStatusSet() {
        return statusSet;
    }

    @Override
    public boolean isLoggedNow(String username) {
        try {
            return statusSet.contains(username);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public Map<String, Boolean> getContactStatus(String username) {
        Map<String, Boolean> statusContactMap = new HashMap<>();

        profilePreferencesRepository.findByProfileUsername(username)
                .get()
                .getContactsBook()
                .forEach(contact -> {
                    if (statusSet.contains(contact.getUsername())) {
                        statusContactMap.put(contact.getUsername(), true);
                    }
                    else {
                        statusContactMap.put(contact.getUsername(), false);
                    }
                });

        return statusContactMap;
    }

}
