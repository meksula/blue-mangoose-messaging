package com.bluemangoose.client.model.logic.impl;

import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.dto.Contact;
import com.bluemangoose.client.model.dto.ContactFind;
import com.bluemangoose.client.model.logic.ContactSearcher;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class ContactSearcherImpl implements ContactSearcher {

    @Override
    public List<ContactFind> searchContacts(String phrase) {
        String json = new HttpServerConnectorImpl<>(String.class).post(new ContactSearch(phrase), ApiPath.CHAT_USERS_SEARCH);
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<ContactFind>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
