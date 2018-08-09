package com.meksula.chat.domain.user.social;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.meksula.chat.domain.user.Contact;
import com.meksula.chat.domain.user.ProfilePreferences;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

@Getter
@Setter
@Entity
@Table(name = "contact_notifications")
public class ContactAddNotification extends Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false)
    private ProfilePreferences profilePreferences;

    private String inviterUsername;
    private Long inviterUserId;

    public ContactAddNotification(String title, String message) {
        super(title, message);
    }

    public ContactAddNotification() {}

}
