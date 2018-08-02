package com.meksula.chat.domain.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

@Getter
@Setter
@Entity
@Table(name = "profile")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProfilePreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long profileId;

    private long userId;
    private String profileUsername;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "profilePreferences", cascade = CascadeType.ALL)
    private Set<Contact> contactsBook;
}
