package com.bluemangoose.client.model.logic.impl;

import com.bluemangoose.client.model.dto.Contact;
import com.bluemangoose.client.model.logic.ContactsManager;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class ContactsManagerImpl implements ContactsManager {
    @Override
    public void addContact(Contact contact) {
        System.out.println(contact.getUsername() + " dodany do kontaktów.");
    }
}
