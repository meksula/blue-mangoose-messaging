package com.meksula.chat.domain.mailbox;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

@Service
public class TopicIndexImpl implements TopicIndex {
    @Override
    public boolean hasNewLetters(String username) {
        return false;
    }

    @Override
    public List<TopicShort> getTopicListByUsername(String username) {
        return null;
    }

    @Override
    public boolean isTopicExist() {
        return false;
    }

    @Override
    public void indexTopic(Topic topic) {

    }
}
