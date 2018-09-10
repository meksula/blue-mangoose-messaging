package com.bluemangoose.client.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author
 * Karol Meksuła
 * 10-09-2018
 * */

@Getter
@Setter
public class Topic {
    private String title;
    private List<Letter> letters;

    public Topic() {}

    public Topic(String title) {
        this.title = title;
    }


}