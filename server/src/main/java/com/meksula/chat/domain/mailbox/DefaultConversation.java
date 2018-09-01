package com.meksula.chat.domain.mailbox;

import com.meksula.chat.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

@Service
public class DefaultConversation implements Conversation {
    private TopicRepository topicRepository;

    @Autowired
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic initTopic(Letter letter, String title) {
        Topic topic = new Topic.TopicBuilder()
                .title(title)
                .addresseeUsername(letter.getAddresseeUsername())
                .senderUsername(letter.getSenderUsername())
                .build();

        letter.setTopic(topic);
        topic.addLetter(letter);
        return topicRepository.save(topic);
    }

    @Override
    public Topic sendLetter(Letter letter, String topicId) {
        return null;
    }

    @Override
    public Topic overshadowTopic(String topicId) {
        return null;
    }

}
