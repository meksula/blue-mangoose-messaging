package com.meksula.chat.repository;

import com.meksula.chat.domain.user.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

@Transactional
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByUsername(String username);

    Optional<ChatUser> findByUserId(long userId);

    @Query(value = "SELECT * FROM chat_users WHERE username REGEXP ?1",
            nativeQuery = true)
    Set<ChatUser> findMatching(String phrase);
}
