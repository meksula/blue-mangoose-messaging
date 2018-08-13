package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.model.personal.ContactAddNotification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 13-08-2018
 * */

public class NotificationsController implements Initializable {
    private List<ContactAddNotification> notifications;

    @FXML
    private VBox notificationPanel, deletePanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.notifications = SessionCache.getInstance().getProfilePreferences().getNotifications();

        addNotificationPanelAction();
    }

    /**
     * Tutaj dalej trzeba zaimplementować zachowanie po kliknięciu w powiadomienie.
     * Co ma robić, odrzucać, akceptować itd.
     * */

    private void addNotificationPanelAction() {
        if (notifications.size() == 0) {
            return;
        }

        for (int i = 0; i < notifications.size(); i++) {
            Label label = drawLabel(i);
            ImageView imageView = drawImageView();

            deletePanel.getChildren().add(label);
            notificationPanel.getChildren().add(imageView);
        }

    }

    private Label drawLabel(int i) {
        Label label = new Label(notifications.get(i).getMessage());
        label.setWrapText(true);
        final String INACTIVE = "text_label";
        final String ACTIVE = "text_active";
        label.getStyleClass().add(INACTIVE);
        notificationPanel.getChildren().add(label);

        label.setOnMouseClicked(event -> {
            //TODO po kliknięciu w powiadomiennie
        });

        label.setOnMouseEntered(event -> {
            label.getStyleClass().clear();
            label.getStyleClass().add(ACTIVE);
        });

        label.setOnMouseExited(event -> {
            label.getStyleClass().clear();
            label.getStyleClass().add(INACTIVE);
        });

        return label;
    }

    private ImageView drawImageView() {
        Image active = new Image("/img/delete-active.png");
        Image incative = new Image("/img/delete-inactive.png");

        ImageView delete = new ImageView();
        delete.setImage(incative);

        delete.setOnMouseClicked(event -> System.out.println("Szczegółowe informacje."));

        delete.setOnMouseEntered(event -> delete.setImage(active));
        delete.setOnMouseExited(event -> delete.setImage(incative));

        deletePanel.getChildren().add(delete);

        return delete;
    }

}
