package com.meksula.chat.domain.mailbox;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meksula.chat.domain.registration.verification.CodeGenerator;
import lombok.Getter;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

@Getter
@Entity
@Table(name = "letters")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Letter implements Comparable<Letter> {

    @Transient
    @JsonIgnore
    private final static int ID_LENGTH = 10;

    @Id
    private String id;

    private long senderId;
    private String senderUsername;

    private long addresseeId;
    private String addresseeUsername;

    private LocalDateTime sendTime;
    private boolean unsealed;

    private String title;
    private String content;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "topicId", nullable = false)
    private Topic topic;

    public Letter() {}

    private Letter(LetterBuilder builder) {
        this.id = builder.id;
        this.senderId = builder.senderId;
        this.senderUsername = builder.senderUsername;
        this.addresseeId = builder.addresseeId;
        this.addresseeUsername = builder.addresseeUsername;
        this.sendTime = builder.sendTime;
        this.unsealed = builder.unsealed;
        this.title = builder.title;
        this.content = builder.content;
        this.topic = builder.topic;
    }

    public void setUnsealed(boolean unsealed) {
        this.unsealed = unsealed;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", SENDER: " + senderUsername + ", ADDRESSEE: " + addresseeUsername
                + ", TIME: " + sendTime + ", UNSEALED: " + unsealed + ", TITLE: " + title;
    }

    @Override
    public int compareTo(Letter other) {
        if (this.getSendTime().isAfter(other.sendTime)) {
            return 1;
        }
        else return -1;
    }

    static class LetterBuilder {
        private String id;
        private long senderId;
        private String senderUsername;
        private long addresseeId;
        private String addresseeUsername;
        private LocalDateTime sendTime;
        private boolean unsealed;
        private String title;
        private String content;
        private Topic topic;

        public LetterBuilder senderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public LetterBuilder senderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
            return this;
        }

        public LetterBuilder addresseeId(long addresseeId) {
            this.addresseeId = addresseeId;
            return this;
        }

        public LetterBuilder addresseeUsername(String addresseeUsername) {
            this.addresseeUsername = addresseeUsername;
            return this;
        }

        public LetterBuilder sendTime(LocalDateTime sendTime) {
            this.sendTime = sendTime;
            return this;
        }

        public LetterBuilder unsealed(boolean unsealed) {
            this.unsealed = unsealed;
            return this;
        }

        public LetterBuilder title(String title) {
            this.title = title;
            return this;
        }

        public LetterBuilder content(String content) {
            this.content = content;
            return this;
        }

        public LetterBuilder topic(Topic topic) {
            this.topic = topic;
            return this;
        }

        public Letter build() {
            if (this.id == null) {
                String prefix = this.senderUsername.substring(0, 3);
                this.id = prefix + CodeGenerator.generateCode(ID_LENGTH);
            }

            if (this.sendTime == null) {
                this.sendTime = LocalDateTime.now();
            }

            return new Letter(this);
        }

    }

}
