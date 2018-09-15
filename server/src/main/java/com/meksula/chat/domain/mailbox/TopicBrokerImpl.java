package com.meksula.chat.domain.mailbox;

import com.meksula.chat.domain.registration.verification.CodeGenerator;
import com.meksula.chat.domain.user.ChatUser;
import com.meksula.chat.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Exception: Topic not exist. ID: " + topicId));
    }

    @Override
    public List<Letter> getNewestInTopic(String topicId, int currentTopicSize) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Exception: Topic not exist. ID: " + topicId));
        List<Letter> allLetters = topic.getLetters();
        allLetters.sort(Letter::compareTo);

        int last;
        if (allLetters.size() - 1 == 0) {
            last = 1;
        } else {
            last = allLetters.size() - 1;
        }

        try {
            return allLetters.subList(currentTopicSize, last);
        } catch (IndexOutOfBoundsException exception) {
            return new ArrayList<>();
        }
    }

    @Override
    public Topic sendLetter(Letter letter, String topicId) {
        boolean isTopicExist = topicIndex.isTopicExist(topicId);

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

    @Override
    public void deleteOneSide(ChatUser chatUser, String topicId) {
        topicIndex.deleteOneSideOfConversation(topicId, chatUser.getUsername());
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found [ID: " + topicId + "]"));
        topic.changeConversationSides(chatUser.getUsername());
        topicRepository.save(topic);
    }

    private Topic saveAndIndexTopic(Topic topic, Letter letter) {
        letter.setSendTimestamp();
        letter.setTopic(topic);
        topic.addLetter(letter);
        topicIndex.indexTopic(topic);

        if (topic.getTopicId() == null || topic.getTopicId().isEmpty()) {
            topic.setTopicId(CodeGenerator.generateCode(10));
        }

        if (letter.getId() == null || letter.getId().isEmpty()) {
            letter.setId(CodeGenerator.generateCode(10));
        }

        return topicRepository.save(topic);
    }

}
