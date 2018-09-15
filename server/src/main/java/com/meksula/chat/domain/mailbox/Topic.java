package com.meksula.chat.domain.mailbox;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.meksula.chat.domain.registration.verification.CodeGenerator;
import lombok.Getter;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

@Getter
@Entity
@Table(name = "mail_topics")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Topic {

    @Id
    private String topicId;

    private long senderId;
    private String senderUsername;

    private long addresseeId;
    private String addresseeUsername;

    private String initTimestamp;
    private String title;
    private boolean overshadowed = false;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topic", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Letter> letters;

    public Topic() {}

    public Topic(TopicBuilder topicBuilder) {
        this.topicId = topicBuilder.topicId;
        this.senderId = topicBuilder.senderId;
        this.senderUsername = topicBuilder.senderUsername;
        this.addresseeId = topicBuilder.addresseeId;
        this.addresseeUsername = topicBuilder.addresseeUsername;
        this.initTimestamp = topicBuilder.initTimestamp.toString();
        this.letters = topicBuilder.letters;
        this.title = topicBuilder.title;
    }

    String[] changeConversationSides(String sideUsername) {
        if (addresseeUsername.equals(sideUsername)) {
            addresseeUsername = null;
        }
        if (senderUsername.equals(sideUsername)) {
            senderUsername = null;
        }

        return new String[] {addresseeUsername, senderUsername};
    }

    public void addLetter(Letter letter) {
        this.letters.add(letter);
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setAddresseeId(long addresseeId) {
        this.addresseeId = addresseeId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    @Override
    public int hashCode() {
        return topicId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Topic other = (Topic) obj;
        return this.topicId.equals(other.getTopicId());
    }

    static class TopicBuilder {
        private String topicId;
        private long senderId;
        private String senderUsername;
        private long addresseeId;
        private String addresseeUsername;
        private LocalDateTime initTimestamp;
        private String title;
        private List<Letter> letters;

        public TopicBuilder senderId(long senderId) {
            this.senderId = senderId;
            return this;
        }

        public TopicBuilder senderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
            return this;
        }

        public TopicBuilder addresseId(long addresseeId) {
            this.addresseeId = addresseeId;
            return this;
        }

        public TopicBuilder addresseeUsername(String addresseeUsername) {
            this.addresseeUsername = addresseeUsername;
            return this;
        }

        public TopicBuilder title(String title) {
            this.title = title;
            return this;
        }

        public Topic build() {
            this.initTimestamp = LocalDateTime.now();

            if (topicId == null) {
                this.topicId = CodeGenerator.generateCode(10);
            }

            if (this.letters == null) {
                this.letters = new LinkedList<>();
            }

            return new Topic(this);
        }
    }

}
