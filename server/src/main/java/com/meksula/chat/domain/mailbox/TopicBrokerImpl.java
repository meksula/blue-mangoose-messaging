package com.meksula.chat.domain.mailbox;

import com.meksula.chat.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

@Service
public class TopicBrokerImpl implements TopicBroker {
    private TopicIndex topicIndex;
    private TopicRepository topicRepository;
    private TopicValidator topicValidator;

    @Autowired
    public void setTopicIndex(TopicIndex topicIndex) {
        this.topicIndex = topicIndex;
    }

    @Autowired
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Autowired
    public void setTopicValidator(TopicValidator topicValidator) {
        this.topicValidator = topicValidator;
    }

    @Override
    public Topic getTopic(String topicId) {
        return null;
    }

    @Override
    public Topic getNewestInTopic(String topicId, int topicSize) {
        return null;
    }

    @Override
    public Topic sendLetter(Letter letter, String topicId) {
        boolean isTopicExist = topicIndex.isTopicExist();

        if (!isTopicExist) {
            throw new EntityNotFoundException("There is no topic with id: " + topicId);
        }

        Topic topic = topicRepository.findById(topicId).get();
        boolean access = topicValidator.validateLetter(letter);

        if (!access) {
            throw new RuntimeException("Letter is incorrect.");
        }

        return saveAndIndexTopic(topic, letter);
    }

    @Override
    public Topic createTopic(String title, Letter initLetter) {
        Topic topic = new Topic.TopicBuilder()
                .title(title)
                .addresseeUsername(initLetter.getAddresseeUsername())
                .senderUsername(initLetter.getSenderUsername())
                .build();

        return saveAndIndexTopic(topic, initLetter);
    }

    private Topic saveAndIndexTopic(Topic topic, Letter letter) {
        letter.setTopic(topic);
        topic.addLetter(letter);
        topicIndex.indexTopic(topic);
        return topicRepository.save(topic);
    }

}
