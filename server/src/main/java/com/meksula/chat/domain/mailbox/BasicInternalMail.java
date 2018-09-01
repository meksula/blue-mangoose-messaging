package com.meksula.chat.domain.mailbox;

import com.meksula.chat.repository.LetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

@Service
public class BasicInternalMail implements InternalMail {
    private LetterRepository letterRepository;

    @Autowired
    public void setLetterRepository(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    @Override
    public List<Letter> getAllLettersInMailboxByUsername(String username) {
        List<Letter> letters = letterRepository.getAllByAddresseeUsername(username);
        letters.sort(Letter::compareTo);
        return letters;
    }

    @Override
    public List<Letter> getAllSentMessages(String username) {
        List<Letter> letters = letterRepository.getAllBySenderUsername(username);
        letters.sort(Letter::compareTo);
        return letters;
    }

    @Override
    public List<Letter> getLastNReceivedLetters(String username, int amount) {
        List<Letter> letters = letterRepository.getAllByAddresseeUsername(username);
        letters.sort(Letter::compareTo);

        int to = letters.size();
        int from = to - amount;

        return letters.subList(from, to);
    }

    @Override
    public Letter getConcreteLetter(String letterId) {
        return letterRepository.getById(letterId)
                .orElseThrow(() -> new EntityNotFoundException("Letter with id: " + letterId + " not exist."));
    }

    @Override
    public boolean isNewLetters(String username) {
        List<Letter> letters = letterRepository.getAllByAddresseeUsername(username);
        return letters.stream().anyMatch(letter -> !letter.isUnsealed());
    }

    @Override
    public Letter sendLetter(Letter letter) {
        return null;
    }

    @Override
    public Letter signAsFavourite(String letterId) {
        return null;
    }

    @Override
    public List<Letter> getFavouriteLetters(String username) {
        return null;
    }

}
