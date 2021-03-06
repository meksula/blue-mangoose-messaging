package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.dto.ContactFind;
import com.bluemangoose.client.model.logic.ContactSearcher;
import com.bluemangoose.client.model.logic.ContactsManager;
import com.bluemangoose.client.model.logic.impl.ContactSearcherImpl;
import com.bluemangoose.client.model.logic.impl.ContactsManagerImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class SearchContactWindowController implements Initializable, DataInitializable {
    private ContactSearcher contactSearcher;
    private ContactsManager contactsManager;
    private String initData;
    private List<ContactFind> contactList;
    private int index;

    @FXML
    private VBox contactsVbox;

    @FXML
    private VBox plusVbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactSearcher = new ContactSearcherImpl();
        contactsManager = new ContactsManagerImpl();

        contactsVbox.setSpacing(9);
        plusVbox.setSpacing(3);
    }

    @Override
    public void initData(Object searchPhrase) {
        this.initData = searchPhrase.toString();

        contactList = contactSearcher.searchContacts(initData);
        drawData(contactList);
        addToContactsAction();
    }

    private void drawData(List<ContactFind> contactList) {

        if(contactList.isEmpty()) {
            new Alerts().emptySearchResult();
            return;
        }

        contactList.forEach(item -> {
            Label label = new Label();
            label.setText(item.getUsername());
            label.getStyleClass().add("contact_search");
            contactsVbox.getChildren().add(label);

            ImageView button = new ImageView(new Image("/img/plus.png"));
            plusVbox.getChildren().add(button);
        });

    }

    private void addToContactsAction() {
        ObservableList<Node> addButtons = plusVbox.getChildren();

        for (Node button : addButtons) {
            button.setOnMouseClicked(event -> {
                index = plusVbox.getChildren().indexOf(button);
                Label label = (Label) contactsVbox.getChildren().get(index);
                addNewContact(label.getText());
            });
        }
    }

    private void addNewContact(String text) {
        boolean decission = new Alerts().contactAdded(text);

        if (!decission) {
            return;
        }

        ContactFind contactFind = contactList.stream()
                .filter(contact -> contact.getUsername().equals(text))
                .findFirst().get();

        contactsManager.addContact(new ContactFind(contactFind.getUserId(), contactFind.getUsername()));
    }

}
