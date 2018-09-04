package com.meksula.chat.domain.mailbox;

import java.util.List;
import java.util.Map;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

public interface TopicIndex {
    boolean hasNewLetters(String username);

    List<TopicShortInfo> getTopicListByUsername(String username);

    boolean isTopicExist(String topicId);

    void indexTopic(Topic topic);

    Map<String, Integer> indexReport();
}
