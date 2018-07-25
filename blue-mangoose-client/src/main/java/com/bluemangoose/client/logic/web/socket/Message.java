package com.bluemangoose.client.logic.web.socket;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Message {
    private String usernmame;
    private String content;
    private String sendTime;
    private String roomTarget;

    public Message(String usernmame, String content) {
        this.usernmame = usernmame;
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }

    public String getRoomTarget() {
        return roomTarget;
    }

    public String getUsernmame() {
        return usernmame;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRoomTarget(String roomTarget) {
        this.roomTarget = roomTarget;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setUsernmame(String usernmame) {
        this.usernmame = usernmame;
    }

}

