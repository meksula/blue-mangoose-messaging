package com.meksula.chat.client.model.logic.impl;

import com.meksula.chat.client.model.dto.Contact;
import com.meksula.chat.client.model.logic.ContactsManager;

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
