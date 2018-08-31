package com.meksula.chat.repository;

import com.meksula.chat.domain.mailbox.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

public interface LetterRepository extends JpaRepository<Letter, String> {
    List<Letter> getAllByAddresseeUsername(String addresseeUsername);

    List<Letter> getAllBySenderUsername(String senderUsername);

    Optional<Letter> getById(String letterId);
}
