package com.bluemangoose.client.logic.web.mailbox;

import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnector;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.dto.Letter;
import com.bluemangoose.client.model.dto.Topic;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 12-09-2018
 * */

@Slf4j
public class MailboxLetterExchangeImpl implements MailboxLetterExchange {
    private HttpServerConnector<String> connector;
    private ObjectMapper mapper;

    public MailboxLetterExchangeImpl() {
        this.connector = new HttpServerConnectorImpl<>(String.class);
        this.mapper = new ObjectMapper();
    }

    @Override
    public Topic createTopic(Letter letter) throws IOException {
        ApiPath path = ApiPath.CREATE_TOPIC;
        path.setTitle(letter.getTitle());
        String result = connector.post(letter, path);
        log.debug(result);
        return mapper.readValue(result, Topic.class);
    }

    @Override
    public List<TopicShortInfo> getTopicsShortInfo() throws IOException {
        String result = connector.get(ApiPath.TOPIC_INFO);
        log.debug(result);
        return mapper.readValue(result, new TypeReference<List<TopicShortInfo>>() {});
    }

    @Override
    public Topic fetchTopic(String topicId) {
        ApiPath path = ApiPath.WHOLE_TOPIC;
        path.setTopicId(topicId);
        String result = connector.get(path);
        try {
            return mapper.readValue(result, Topic.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Letter sendLetter(Letter letter) {
        return null;
    }

}
