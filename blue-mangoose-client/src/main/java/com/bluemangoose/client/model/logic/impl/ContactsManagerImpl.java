package com.bluemangoose.client.model.logic.impl;

import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.dto.ContactFind;
import com.bluemangoose.client.model.logic.ContactsManager;
import com.bluemangoose.client.model.personal.ContactAddNotification;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class ContactsManagerImpl implements ContactsManager {

    /**
     * TODO
     * Teraz jestem na etapie realizacji procesu zapraszania.
     * Zaproszenie dochodzi na serwer i przekazuje powiadomieie do innego użytkownika.
     * (baza danych nie wyświetla UTF-8)
     * Teraz zaimplementować dalszy proces dodawania do znajomych.
     * */

    @Override
    public void addContact(ContactFind contact) {
        new HttpServerConnectorImpl<>(ContactAddNotification.class)
                .post(new ContactSearch(contact.getUsername()), ApiPath.CHAT_USER_INVITATION);
    }

}
