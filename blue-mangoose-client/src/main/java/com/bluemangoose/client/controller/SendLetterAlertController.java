package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.logic.web.mailbox.MailboxLetterExchange;
import com.bluemangoose.client.logic.web.mailbox.MailboxLetterExchangeImpl;
import com.bluemangoose.client.logic.web.mailbox.MailboxTemporaryCache;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.dto.Letter;
import com.bluemangoose.client.model.dto.LetterCreator;
import com.bluemangoose.client.model.dto.Mail;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.joda.time.LocalDateTime;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author
 * Karol Meksuła
 * 12-09-2018
 * */

public class SendLetterAlertController implements Initializable, DataInitializable {
    private MailboxLetterExchange mailboxLetterExchange;
    private String addresseUsername;

    @FXML
    private ImageView sendLetterButton, cancelButton;

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mailboxLetterExchange = new MailboxLetterExchangeImpl();

        sendLetterButtonAction();
        cancelButtonAction();
    }

    @Override
    public void initData(Object data) {
        this.addresseUsername = (String) data;
    }

    private void sendLetterButtonAction() {
        Image inactive = sendLetterButton.getImage();
        Image active = new Image("/img/send-letter-active.png");

        sendLetterButton.setOnMouseEntered(event -> sendLetterButton.setImage(active));
        sendLetterButton.setOnMouseExited(event -> sendLetterButton.setImage(inactive));
        sendLetterButton.setOnMouseClicked(event -> {
            if (addresseUsername == null || textArea.getText().length() == 0) {
                new Alerts().error("Bład", "Nie można wysłać wiadomości!",
                        "Odbiorca nie jest ustalony, albo wiadomość jest pusta.\nKliknij na dowolny temat, " +
                                "wtedy będziesz mógł wysłać wiadomość.");
                return;
            }

            Letter letter = new LetterCreator().letterBuild(textArea.getText(), addresseUsername);
            mailboxLetterExchange.sendLetter(letter, MailboxTemporaryCache.getCurrentTopic());
            MailboxTemporaryCache.getCurrentMailboxController().drawNewestLetter(letter);
            close(sendLetterButton);
        });
    }

    private void cancelButtonAction() {
        Image inactive = cancelButton.getImage();
        Image active = new Image("/img/cancel-active.png");

        cancelButton.setOnMouseEntered(event -> cancelButton.setImage(active));
        cancelButton.setOnMouseExited(event -> cancelButton.setImage(inactive));
        cancelButton.setOnMouseClicked(event -> {
            close(cancelButton);
        });
    }

    private void close(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
