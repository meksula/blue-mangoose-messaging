package com.bluemangoose.client.logic.web.mailbox;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

/**
 * @author
 * Karol Meksuła
 * 02-09-2018
 * */

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TopicShortInfo {
    private String topicId;
    private String title;
    private String usernameA;
    private String usernameB;
    private String initDate;
}
