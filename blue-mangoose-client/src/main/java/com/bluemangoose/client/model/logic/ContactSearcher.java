package com.bluemangoose.client.model.logic;

import com.bluemangoose.client.model.dto.Contact;

import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public interface ContactSearcher {
    List<Contact> searchContacts(String tag);
}
