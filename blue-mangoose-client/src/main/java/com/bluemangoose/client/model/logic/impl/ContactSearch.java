package com.bluemangoose.client.model.logic.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author
 * Karol Meksuła
 * 12-08-2018
 * */

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ContactSearch {
    private String phrase;

    public ContactSearch(String phrase) {
        this.phrase = phrase;
    }

    public ContactSearch() {}
}
