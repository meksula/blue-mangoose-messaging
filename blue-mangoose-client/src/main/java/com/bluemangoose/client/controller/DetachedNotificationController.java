package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnector;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.personal.ContactAddNotification;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 14-08-2018
 * */

public class DetachedNotificationController implements Initializable, DataInitializable {
    private HttpServerConnector<String> httpServerConnector;
    private ContactAddNotification notification;
    private final String TITLE = "Tytuł: ";
    private final String DATE = "Data: ";
    private final String SENDER = "Nadawca: ";
    private final String CONTENT = "Treść: ";

    @FXML
    private Label title, date, sender;

    @FXML
    private ImageView back, decline, accept;

    @FXML
    private AnchorPane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.httpServerConnector = new HttpServerConnectorImpl<>(String.class);

        addButtonsAction();
    }

    private void initializeLabels() {
        title.setText(TITLE + notification.getTitle());
        date.setText(DATE + notification.getInitDate());
        sender.setText(SENDER + notification.getInviterUsername());

        Label message = new Label(CONTENT + notification.getMessage());
        message.setLayoutX(14);
        message.setLayoutY(89);
        message.getStyleClass().add("text_label");
        message.setWrapText(true);
        pane.getChildren().add(message);
    }

    @Override
    public void initData(Object data) {
        this.notification = (ContactAddNotification) data;
        initializeLabels();
    }

    private void addButtonsAction() {
        Image backActive = new Image("/img/back-active.png");
        Image backInactive = back.getImage();

        back.setOnMouseEntered(event -> back.setImage(backActive));
        back.setOnMouseExited(event -> back.setImage(backInactive));
        back.setOnMouseClicked(event -> backToNotifications());

        Image declineActive = new Image("/img/discard-active.png");
        Image declineInactive = decline.getImage();

        decline.setOnMouseEntered(event -> decline.setImage(declineActive));
        decline.setOnMouseExited(event -> decline.setImage(declineInactive));
        decline.setOnMouseClicked(event -> declineNotification());

        Image acceptActive = new Image("/img/accept-active.png");
        Image acceptInactive = accept.getImage();

        accept.setOnMouseEntered(event -> accept.setImage(acceptActive));
        accept.setOnMouseExited(event -> accept.setImage(acceptInactive));
        accept.setOnMouseClicked(event -> acceptNotification());

        new ArrayList<>(Arrays.asList(back, decline, accept))
                .forEach(node -> node.setCursor(Cursor.HAND));

    }

    private void backToNotifications() {
        new FxmlLoaderTemplate().loadSameStage("/templates/notifications.fxml", title);
    }

    private void declineNotification() {
        //TODO odrzucenie powiadomienia
    }

    private void acceptNotification() {
        if (notification == null) {
            return;
        }

        ApiPath apiPath = ApiPath.CHAT_USER_INVITATION_RESPONSE;
        apiPath.setNotificationId(notification.getId());

        httpServerConnector.post(Boolean.valueOf("true"), apiPath);
    }

}
