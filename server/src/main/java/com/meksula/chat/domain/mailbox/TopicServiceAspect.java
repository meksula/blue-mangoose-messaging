package com.meksula.chat.domain.mailbox;

import com.meksula.chat.domain.user.ProfilePreferences;
import com.meksula.chat.repository.ProfilePreferencesRepository;
import com.meksula.chat.repository.TopicRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

@Aspect
@Component
public class TopicServiceAspect {
    private TopicRepository topicRepository;
    private ProfilePreferencesRepository profilePreferencesRepository;

    @Autowired
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Autowired
    public void setProfilePreferencesRepository(ProfilePreferencesRepository profilePreferencesRepository) {
        this.profilePreferencesRepository = profilePreferencesRepository;
    }

    /**
     * It is save here to use Optional<T>.get()
     * Validated before execution.
     * */
    @AfterReturning(pointcut = "execution(* com.meksula.chat.domain.mailbox.Conversation.initTopic(..))", returning = "topic")
    public void complementTopicAndSave(Topic topic) {
        ProfilePreferences sender = profilePreferencesRepository.findByProfileUsername(topic.getSenderUsername()).get();
        ProfilePreferences addressee = profilePreferencesRepository.findByProfileUsername(topic.getAddresseeUsername()).get();

        topic.setSenderId(sender.getProfileId());
        topic.setAddresseeId(addressee.getProfileId());

        topicRepository.save(topic);
    }

}
