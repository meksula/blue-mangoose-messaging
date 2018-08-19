package com.bluemangoose.client.model.logic.impl;

import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.dto.ContactFind;
import com.bluemangoose.client.model.logic.ContactsManager;
import com.bluemangoose.client.model.personal.ContactAddNotification;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class ContactsManagerImpl implements ContactsManager {

    @Override
    public void addContact(ContactFind contact) {
        ContactAddNotification notification = new HttpServerConnectorImpl<>(ContactAddNotification.class)
                .post(new ContactSearch(contact.getUsername()), ApiPath.CHAT_USER_INVITATION);

        if (notification.getTitle().equals("Invitation exist")) {
            new Alerts().error("Kontakt istnieje", "Ten kontakt istnieje już w twojej książce kontaktów!", null);
        }
        else {
            new Alerts().other("Wysłano zaproszenie", "Użytkownik otrzyma Twoje zaproszenie.", null);
        }
    }

}
