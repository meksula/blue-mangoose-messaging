package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.daemon.NotificationsUpdateDaemon;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnector;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.personal.ContactAddNotification;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

/**
 * Remember that one scene can display max 13 notifications.
 * */

public class NotificationsController implements Initializable {
    private List<ContactAddNotification> notifications;
    private HttpServerConnector<String> httpServerConnector;

    @FXML
    private VBox notificationPanel, deletePanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.notifications = SessionCache.getInstance().getProfilePreferences().getNotifications();
        this.httpServerConnector = new HttpServerConnectorImpl<>(String.class);

        notificationPanel.setSpacing(6);
        deletePanel.setSpacing(15);

        addNotificationPanelAction();
    }

    private void addNotificationPanelAction() {
        if (notifications.size() == 0) {
            return;
        }

        for (int i = 0; i < notifications.size(); i++) {
            Label label = drawLabel(i);
            ImageView imageView = drawImageView(notifications.get(i));

            deletePanel.getChildren().add(label);
            notificationPanel.getChildren().add(imageView);
        }

    }

    private Label drawLabel(int i) {
        ContactAddNotification notification = notifications.get(i);
        Label label = new Label(notification.getTitle());
        label.setWrapText(true);
        final String INACTIVE = "text_label";
        final String ACTIVE = "text_active";
        label.getStyleClass().add(INACTIVE);
        notificationPanel.getChildren().add(label);

        Tooltip tooltip = new Tooltip();
        tooltip.setText(notification.getMessage() + ",\n " + notification.getInitDate());
        label.setTooltip(tooltip);

        label.setOnMouseClicked(event -> notificationDialogue(notification));

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

    private ImageView drawImageView(ContactAddNotification notification) {
        Image active = new Image("/img/delete-active.png");
        Image inacative = new Image("/img/delete-inactive.png");

        ImageView delete = new ImageView();
        delete.setImage(inacative);

        delete.setOnMouseClicked(event -> removeNotification(notification.getId()));

        delete.setOnMouseEntered(event -> delete.setImage(active));
        delete.setOnMouseExited(event -> delete.setImage(inacative));

        deletePanel.getChildren().add(delete);

        return delete;
    }

    private void notificationDialogue(ContactAddNotification notification) {
        new FxmlLoaderTemplate().loadSameStageWithData(FxmlLoaderTemplate.SceneType.NOTIFICATION_DETACHED, notification, deletePanel);
    }

    private void removeNotification(long notificationId) {
        ApiPath apiPath = ApiPath.CHAT_USER_NOTIFICATION_REMOVE;
        apiPath.setNotificationId(notificationId);
        httpServerConnector.delete(apiPath);

        new NotificationsUpdateDaemon(SessionCache.getInstance().getProfilePreferences()).updateStateOnce();
        new FxmlLoaderTemplate().loadSameStage(FxmlLoaderTemplate.SceneType.NOTIFICATION_DETACHED.getPath(), notificationPanel);
    }

}
