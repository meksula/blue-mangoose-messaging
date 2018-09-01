package com.meksula.chat.domain.mailbox;

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

public interface Conversation {
    Topic initTopic(Letter letter, String title);

    Topic sendLetter(Letter letter, String topicId);

    Topic overshadowTopic(String topicId);
}
