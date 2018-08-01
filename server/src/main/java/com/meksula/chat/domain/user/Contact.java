package com.meksula.chat.domain.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Author
 * Karol Meksuła
 * 01-07-2018
 * */

@Getter
@Setter
@Entity
@Table(name = "contacts")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long ownerId;
    private String ownerUsername;

    private String contactUsername;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ChatUser chatUser;

    @Override
    public int hashCode() {
        return Objects.hash(ownerUsername, contactUsername);
    }

    @Override
    public String toString() {
        return id + ", " + contactUsername + ", " + chatUser.getUsername();
    }
}
