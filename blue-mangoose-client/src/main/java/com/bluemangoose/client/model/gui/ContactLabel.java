package com.bluemangoose.client.model.gui;

import com.bluemangoose.client.model.personal.Contact;
import javafx.scene.control.Label;
import lombok.Getter;

/**
 * @Author
 * Karol Meksuła
 * 17-08-2018
 * */

@Getter
public class ContactLabel {
    private Label label;
    private Contact contact;

    public ContactLabel(Label label, Contact contact) {
        this.label = label;
        this.contact = contact;
    }

}
