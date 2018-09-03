package com.meksula.chat.domain.mailbox;

import com.meksula.chat.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

@Service
@Slf4j
public class TopicIndexImpl implements TopicIndex, TopicIndexer {
    private Set<TopicShortInfo> topicShortSet;
    private Map<TopicShortInfo, Boolean> letterIndex;
    private Map<String, Boolean> freshLettersByUsername;
    private TopicRepository topicRepository;

    @Autowired
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public boolean hasNewLetters(String username) {
        return freshLettersByUsername.get(username);
    }

    @Override
    public List<TopicShortInfo> getTopicListByUsername(String username) {
        return null;
    }

    @Override
    public boolean isTopicExist() {
        //najpierw w indexie
        //potem w razie czego sięgamy do bazy
        return false;
    }

    @Override
    public void indexTopic(Topic topic) {
        //zaindeksuj odpowiednio
    }

    @Override
    public Map<String, Integer> indexReport() {
        Map<String, Integer> raport = new HashMap<>();
        raport.put("topicShortSet", topicShortSet.size());
        raport.put("letterIndex", letterIndex.size());
        raport.put("freshLettersByUsername", freshLettersByUsername.size());
        return raport;
    }

    @Override
    public void createIndexes() {
        this.topicShortSet = new HashSet<>();
        this.letterIndex = new HashMap<>();
        this.freshLettersByUsername = new HashMap<>();
        log.debug("Indexes created.");
        updateIndexes();
    }

    @Override
    public void updateIndexes() {
        log.debug("Topic indexes update.");
        List<Topic> topics = topicRepository.findAll();

        if (topics.size() == 0) {
            return;
        }

        topics.forEach(topic -> {
            TopicShortInfo shortInfo = new TopicShortInfo();
            shortInfo.setTitle(topic.getTitle());
            shortInfo.setInitDate(topic.getInitTimestamp());
            shortInfo.setUsernameA(topic.getSenderUsername());
            shortInfo.setUsernameB(topic.getAddresseeUsername());

            this.topicShortSet.add(shortInfo);

            topic.getLetters().forEach(letter -> {
                if (letter.isUnsealed()) {
                    letterIndex.put(shortInfo, Boolean.TRUE);
                    freshLettersByUsername.put(letter.getAddresseeUsername(), Boolean.TRUE);
                } else {
                    letterIndex.put(shortInfo, Boolean.FALSE);
                    freshLettersByUsername.put(letter.getAddresseeUsername(), Boolean.FALSE);
                }
            });
        });
    }

    @Override
    public String toString() {
        return "TopicShortSet[SIZE]: " + topicShortSet.size() + ";\nLettersIndex[SIZE]: " +
                letterIndex.size() + ";\nFreshLetters[SIZE]: " + freshLettersByUsername.size();
    }

}
