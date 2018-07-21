package com.meksula.chat.config;

import com.meksula.chat.repository.ChatUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    private ChatUserRepository chatUserRepository;

    @Autowired
    public void setChatUserRepository(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return chatUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find username: " + username));
    }

}
