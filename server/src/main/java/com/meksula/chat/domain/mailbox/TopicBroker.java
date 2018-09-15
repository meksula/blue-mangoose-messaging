package com.meksula.chat.domain.mailbox;

import com.meksula.chat.domain.user.ChatUser;

import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

public interface TopicBroker {
    Topic getTopic(String topicId);

    List<Letter> getNewestInTopic(String topicId, int topicSize);

    Topic sendLetter(Letter letter, String topicId);

    Topic createTopic(String title, Letter initLetter);

    void deleteOneSide(ChatUser chatUser, String topicId);
}
