package com.meksula.chat.domain.mailbox;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

public interface TopicBroker {
    Topic getTopic(String topicId);

    Topic getNewestInTopic(String topicId, int topicSize);

    Topic sendLetter(Letter letter, String topicId);

    Topic createTopic(String title, Letter initLetter);
}
