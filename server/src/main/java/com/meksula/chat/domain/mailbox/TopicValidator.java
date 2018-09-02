package com.meksula.chat.domain.mailbox;

import com.meksula.chat.repository.ProfilePreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

@Service
public class TopicValidator {
    private ProfilePreferencesRepository profilePreferencesRepository;
    private Letter letter;

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    /**
     * Extract username from Principal in controller.
     * */
    public boolean validate(String username, Letter letter) {
        this.letter = letter;

        if (username.equals(letter.getSenderUsername()) && areExist()) {
            return true;
        }

        return false;
    }

    private boolean areExist() {
        boolean senderExist = profilePreferencesRepository.findByProfileUsername(letter.getSenderUsername()).isPresent();
        boolean addresseeExist = profilePreferencesRepository.findByProfileUsername(letter.getAddresseeUsername()).isPresent();
        return senderExist && addresseeExist;
    }

    public boolean validateLetter(Letter letter) {
        return letter.getAddresseeUsername() != null && letter.getSenderUsername() != null;

    }

}
