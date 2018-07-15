package com.meksula.chat.client.model.logic;

import com.meksula.chat.client.model.dto.Contact;

import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public interface ContactSearcher {
    List<Contact> searchContacts(String tag);
}
