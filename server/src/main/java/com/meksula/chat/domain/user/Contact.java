package com.meksula.chat.domain.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

@Setter
@Getter
@Entity
@Table(name = "contacts")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false)
    private ProfilePreferences profilePreferences;

    private long idOfContactUser;
    private String username;

    @Override
    public String toString() {
        return "ID:" + id + "| username: " + username + ", ContactsID: " + idOfContactUser;
    }

}
