package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.logic.web.mailbox.MailboxLetterExchange;
import com.bluemangoose.client.logic.web.mailbox.MailboxLetterExchangeImpl;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.dto.Letter;
import com.bluemangoose.client.model.dto.LetterCreator;
import com.bluemangoose.client.model.personal.Contact;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author
 * Karol Meksuła
 * 16-09-2018
 * */

public class TopicCreationController implements Initializable {
    private Set<Contact> contactSet;
    private MailboxLetterExchange mailboxLetterExchange;
    private String addresseeUsername;

    @FXML
    private SplitMenuButton menuContacts;

    @FXML
    private TextArea letterContent;

    @FXML
    private TextField topicTitleField;

    @FXML
    private Label addresseeLabel;

    @FXML
    private ImageView sendButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mailboxLetterExchange = new MailboxLetterExchangeImpl();
        this.contactSet = SessionCache.getInstance().getProfilePreferences().getContactsBook();
        addContactsMenu(contactSet);
        sendButtonAction();
        sendButtonStyle();
    }

    private void addContactsMenu(Set<Contact> contactList) {
        contactList.iterator().forEachRemaining(contact -> {
            this.addresseeUsername = contact.getUsername();
            MenuItem menuItem = new MenuItem(addresseeUsername);
            menuItem.setOnAction(event -> {
                this.addresseeUsername = menuItem.getText();
                this.addresseeLabel.setText(addresseeUsername);
            });
            menuContacts.getItems().add(menuItem);
        });
    }

    private void sendButtonAction() {
        sendButton.setOnMouseClicked(event -> {
            String title = topicTitleField.getText();
            String content = letterContent.getText();

            if (content.isEmpty() || title.isEmpty() || addresseeUsername.isEmpty()) {
                new Alerts().other("Wypełnij formularz", "Najpierw musisz wypełnić formularz\nnastępnie wyślij.", null);
                return;
            }

            Letter letter = new LetterCreator().letterBuild(content, title, addresseeUsername);
            try {
                mailboxLetterExchange.createTopic(letter);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) sendButton.getScene().getWindow();
            new Alerts().other("Wysłano", "Wiadomość została wysłana.", null);
            stage.close();
        });
    }

    private void sendButtonStyle() {
        Image inactive = sendButton.getImage();
        Image active = new Image("/img/send-letter-active.png");

        sendButton.setCursor(Cursor.HAND);
        sendButton.setOnMouseEntered(event -> sendButton.setImage(active));
        sendButton.setOnMouseExited(event -> sendButton.setImage(inactive));
    }

}
