package com.bluemangoose.client.logic.web.mailbox;

import com.bluemangoose.client.model.dto.Letter;
import com.bluemangoose.client.model.dto.Topic;

import java.io.IOException;
import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 12-09-2018
 * */

public interface MailboxLetterExchange {
    Topic createTopic(Letter letter) throws IOException;

    List<TopicShortInfo> getTopicsShortInfo() throws IOException;

    Topic fetchTopic(String topicId);

    Letter sendLetter(Letter letter, String topicId);
}
