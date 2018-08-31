package com.meksula.chat.domain.mailbox;

import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

public interface InternalMail {

    List<Letter> getAllLettersInMailboxByUsername(String username);

    List<Letter> getAllSentMessages(String username);

    List<Letter> getLastNReceivedLetters(String username, int amount);

    Letter getConcreteLetter(String letterId);

    boolean isNewLetters(String username);

    Letter sendLetter(Letter letter);

    Letter signAsFavourite(String letterId);

    List<Letter> getFavouriteLetters(String username);
}
