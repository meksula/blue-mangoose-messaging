package com.meksula.chat.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

@Getter
@Setter
@MappedSuperclass
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Message {
    private String roomTarget;
    private String usernmame;
    private String content;
    private String sendTime;
}
