package com.meksula.chat.domain.mailbox;

import com.meksula.chat.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

@Service
@Slf4j
public class TopicIndexImpl implements TopicIndex, TopicIndexer {

    /**
     * @param Set<TopicShortInfo> topicShortSet collects all topics's shorten information in memory. Topics has used
     *                           before specified date are ignored.
     * @param Map<TopicShortInfo, Boolean> collects information about new letters in current topic list (above)
     * @param Map<String, Boolean> is mapping username to fresh message boolean flag.
     * */
    private Set<TopicShortInfo> topicShortSet;
    private Map<TopicShortInfo, Boolean> letterIndex;
    private Map<String, Boolean> freshLettersByUsername;
    private TopicRepository topicRepository;
    public static final String TOPIC_SHORT_SET = "topicShortSet";
    public static final String LETTER_INDEX = "letterIndex";
    public static final String FRESH_LETTERS_BY_USERNAME = "freshLettersByUsername";

    @Autowired
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public TopicIndexImpl() {
        this.topicShortSet = new HashSet<>();
        this.letterIndex = new HashMap<>();
        this.freshLettersByUsername = new HashMap<>();
    }

    @Override
    public boolean hasNewLetters(String username) {
        return freshLettersByUsername.get(username);
    }

    @Override
    public List<TopicShortInfo> getTopicListByUsername(String username) {
        return topicShortSet
                .stream()
                .filter(topicShortInfo -> topicShortInfo.getUsernameA().equals(username)
                                       || topicShortInfo.getUsernameB().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTopicExist(String topicId) {
        return topicShortSet
                .stream()
                .anyMatch(topicShortInfo -> topicShortInfo.getTopicId().equals(topicId));
    }

    @Override
    public void indexTopic(Topic topic) {
        TopicShortInfo topicShortInfo = this.topicShort(topic);

        if (topicShortSet.contains(topicShortInfo)) {
            log.debug("TopicShortSet contains received topic.");
        } else {
            topicShortSet.add(topicShortInfo);
        }

        letterIndex.put(topicShortInfo, Boolean.TRUE);

        freshLettersByUsername.put(topicShortInfo.getUsernameA(), Boolean.TRUE);
        freshLettersByUsername.put(topicShortInfo.getUsernameB(), Boolean.TRUE);
    }

    @Override
    public Map<String, Integer> indexReport() {
        Map<String, Integer> raport = new HashMap<>();
        raport.put(TOPIC_SHORT_SET, topicShortSet.size());
        raport.put(LETTER_INDEX, letterIndex.size());
        raport.put(FRESH_LETTERS_BY_USERNAME, freshLettersByUsername.size());
        return raport;
    }

    @Override
    public void deleteOneSideOfConversation(String topicId, String username) {
        if (topicShortSet.contains(new TopicShortInfo(topicId))) {
            topicShortSet.iterator().forEachRemaining(topicShortInfo -> {
                if (topicShortInfo.getTopicId().equals(topicId)) {
                    if (topicShortInfo.getUsernameA().equals(username)) {
                        topicShortInfo.setUsernameA("");
                    }
                    if (topicShortInfo.getUsernameB().equals(username)) {
                        topicShortInfo.setUsernameB("");
                    }
                }
            });
        }
    }

    @Override
    public void createIndexes() {
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
            TopicShortInfo shortInfo = topicShort(topic);
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

    private TopicShortInfo topicShort(Topic topic) {
        TopicShortInfo shortInfo = new TopicShortInfo(topic.getTopicId());
        shortInfo.setTitle(topic.getTitle());
        shortInfo.setInitDate(topic.getInitTimestamp());
        shortInfo.setUsernameA(topic.getSenderUsername());
        shortInfo.setUsernameB(topic.getAddresseeUsername());
        return shortInfo;
    }

    @Override
    public String toString() {
        return "TopicShortSet[SIZE]: " + topicShortSet.size() + ";\nLettersIndex[SIZE]: " +
                letterIndex.size() + ";\nFreshLetters[SIZE]: " + freshLettersByUsername.size();
    }

}
