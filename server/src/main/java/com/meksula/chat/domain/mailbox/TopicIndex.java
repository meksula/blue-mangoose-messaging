package com.meksula.chat.domain.mailbox;

import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

public interface TopicIndex {
    boolean hasNewLetters(String username);

    List<TopicShort> getTopicListByUsername(String username);

    boolean isTopicExist();

    void indexTopic(Topic topic);
}
