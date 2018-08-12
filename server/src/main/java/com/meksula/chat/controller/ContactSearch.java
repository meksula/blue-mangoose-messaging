package com.meksula.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

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
