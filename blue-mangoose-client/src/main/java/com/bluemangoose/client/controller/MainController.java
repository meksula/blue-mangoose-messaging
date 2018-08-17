package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.logic.web.ChatRoomManager;
import com.bluemangoose.client.logic.web.impl.DefaultRoomManager;
import com.bluemangoose.client.logic.web.socket.ChatMessage;
import com.bluemangoose.client.logic.web.socket.ConversationHandler;
import com.bluemangoose.client.logic.web.socket.WebsocketReceiver;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.gui.ContactLabel;
import com.bluemangoose.client.model.personal.Contact;
import com.bluemangoose.client.model.personal.ContactAddNotification;
import com.bluemangoose.client.model.personal.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author
 * Karol Meksuła
 * 14-07-2018
 * */

public class MainController implements Initializable, DataInitializable, WebsocketReceiver {
    private List<Label> messagesCache = new ArrayList<>();
    private User user;
    private FxmlLoader fxmlLoader;
    private ChatRoomManager chatRoomManager;
    private ConversationHandler conversationHandler;
    private List<ContactLabel> contactLabels;

    @FXML
    private ImageView loupeButton, autonomicWindowButton, disconnectButton, bell;

    @FXML
    private TextField loupeField;

    @FXML
    private ImageView userAvatar;

    @FXML
    private VBox contactsVbox;

    @FXML
    private VBox chatWindow;

    @FXML
    private Label usernameField;
    
    @FXML
    private Label settingButton, newRoom, lookForRoom;

    @FXML
    private ImageView postButton;

    @FXML
    private TextArea messageField;

    @FXML
    private TitledPane contacts;

    @FXML
    private TitledPane chatPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fxmlLoader = new FxmlLoaderTemplate();
        this.contactLabels = new ArrayList<>();

        chatWindow.setSpacing(3);

        addAutonomicWindowAction();
        addDisconnectChatAction();
        addControlPanelButtonsActions();
        addButtonActions();
        sendMessageAction();
        searchingAction();
        addBellAction();
        contactDisplay(SessionCache.getInstance().getProfilePreferences().getContactsBook());

        usernameField.setText(SessionCache.getInstance().getProfilePreferences().getProfileUsername());

        if (SessionCache.getInstance().getProfilePicture() != null) {
            userAvatar.setImage(SessionCache.getInstance().getProfilePicture());
        }

    }

    private void addAutonomicWindowAction() {
        Image inactive = new Image("/img/max.png");
        Image active = new Image("/img/max-active.png");

        autonomicWindowButton.setCursor(Cursor.HAND);
        autonomicWindowButton.setOnMouseEntered(event -> autonomicWindowButton.setImage(active));
        autonomicWindowButton.setOnMouseExited(event -> autonomicWindowButton.setImage(inactive));
        autonomicWindowButton.setOnMouseClicked(event -> popupChatWindow());
    }

    private void addDisconnectChatAction() {
        Image inactive = new Image("/img/disconnect.png");
        Image active = new Image("/img/disconnect-active.png");

        disconnectButton.setCursor(Cursor.HAND);
        disconnectButton.setOnMouseEntered(event -> disconnectButton.setImage(active));
        disconnectButton.setOnMouseExited(event -> disconnectButton.setImage(inactive));
        disconnectButton.setOnMouseClicked(event -> disconnectChat());
    }

    private void addBellAction() {
        Image active = new Image("/img/bell-active.png");
        Image inactive = new Image("/img/bell-inactive.png");

        List<ContactAddNotification> notifications = SessionCache.getInstance().getProfilePreferences().getNotifications();

        if (notifications.size() == 0) {
            bell.setImage(inactive);
        }

        else {
            notifications.size();
            bell.setImage(active);
        }

        bell.setOnMouseClicked(event -> new FxmlLoaderTemplate().loadNewStage("/templates/notifications.fxml"));
    }

    private void popupChatWindow() {
        fxmlLoader.loadNewStage(FxmlLoaderTemplate.SceneType.CHAT_DETACHED.getPath());
    }

    private void disconnectChat() {
        chatRoomManager.disconnect();
        chatWindow.getChildren().clear();
        chatPane.setText("DISCONNECTED");
    }

    @Override
    public void initData(Object data) {
        this.user = (User) data;
        this.chatRoomManager = DefaultRoomManager.getInstance();

        if (chatRoomManager.isConnected()) {
            chatPane.setText("Pokój: " + chatRoomManager.getRoomTarget());
            conversationHandler = ConversationHandler.getInstance(chatRoomManager.getRoomTarget(), 100);
            conversationHandler.setWebsocketReceiver(this);
            try {
                conversationHandler.fetchLastMessages();
            } catch (IOException e) {
                new Alerts().error("Błędne hasło!", "Podane hasło jest nieprawidłowe.\nSpróbuj ponownie.", "");
                e.printStackTrace();
            }

            if (conversationHandler.getMessages().size() > 0) {
                displayExistingMessages();
            }
        }

        else {
            chatPane.setText("DISCONNECTED");
        }

    }

    private void displayExistingMessages() {
        conversationHandler.getMessages().forEach(chatMessage -> {
            Label label = displayForeignMessage(chatMessage);
            chatWindow.getChildren().add(label);
            chatWindowMoving();
        });
    }

    private void addButtonActions() {
        Label[] actions = {settingButton, newRoom, lookForRoom};

        for (Label label : actions) {
            label.setOnMouseEntered(event -> {
                label.getStyleClass().remove("menu_button");
                label.getStyleClass().add("menu_button_shine");
            });

            label.setOnMouseExited(event -> {
                label.getStyleClass().remove("menu_button_shine");
                label.getStyleClass().add("menu_button");
            });
        }

        postButton.setOnMouseEntered(event -> {
            postButton.setImage(new Image("/img/send-active.png"));
        });

        postButton.setOnMouseExited(event -> {
            postButton.setImage(new Image("/img/send-inactive.png"));
        });

    }

    private void sendMessageAction() {
        postButton.setOnMouseClicked(event -> send());

        messageField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                send();
            }
        });
    }

    private void send() {
        chatWindowMoving();

        String text = messageField.getText();

        if (text.isEmpty() || text.length() == 1) {
            return;
        }

        Label message = displayMessage(text);
        chatWindow.getChildren().add(message);
        this.messageField.clear();

        try {
            chatRoomManager.postMessage(text);
        } catch (Exception e) {
            String exceptionText = e.getMessage();
            new Alerts().websocketClosed(exceptionText);
        }
    }

    private Label displayMessage(String text) {
        chatWindowMoving();

        Label label = new Label();
        label.setWrapText(true);
        label.getStyleClass().add("own_message");

        LocalTime localTime = LocalTime.now();
        String time = localTime.format(DateTimeFormatter.ofPattern("kk:mm:ss"));
        label.setText(time + ", " +
                SessionCache.getInstance().getProfilePreferences().getProfileUsername() + "\n> " + text);

        return label;
    }

    private Label displayForeignMessage(ChatMessage chatMessage) {
        chatWindowMoving();

        Label label = new Label();
        label.setWrapText(true);
        label.getStyleClass().add("message");

        LocalTime localTime = LocalTime.now();
        String time = localTime.format(DateTimeFormatter.ofPattern("kk:mm:ss"));
        label.setText(time + ", " + chatMessage.getUsernmame() + "\n> " + chatMessage.getContent());

        return label;
    }

    private void chatWindowMoving() {
        int amount = chatWindow.getChildren().size();

        if (amount >= 9) {
            Label oldest = (Label) chatWindow.getChildren().get(0);
            messagesCache.add(oldest);
            chatWindow.getChildren().remove(0);
        }

    }

    private void contactDisplay(Set<Contact> contacts) {
        this.contactLabels = new ArrayList<>();

        for (Contact contact : contacts) {
            Label label = new Label();
            label.getStyleClass().add("contact_offline");

            label.setText(contact.getUsername());
            contactsVbox.getChildren().add(label);

            ContactLabel contactLabel = new ContactLabel(label, contact);
            contactLabelAction(contactLabel);
            this.contactLabels.add(contactLabel);
            statusUpdate();
        }

    }

    private void statusUpdate() {
        contactLabels.forEach(contactLabel -> {
            if (contactLabel.getContact().isOnline()) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add("contact_online");
            }
        });
    }

    private String currentStyle;
    private void contactLabelAction(ContactLabel contactLabel) {
        contactLabel.getLabel().setOnMouseEntered(event -> {
            try {
                currentStyle = contactLabel.getLabel().getStyleClass().get(1);
            } catch (IndexOutOfBoundsException ioobe) {
                currentStyle = "contact_offline";
            }

            if (currentStyle.equals("contact_offline")) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add("contact_offline_shine");
                currentStyle = "contact_offline_shine";
            }

            if (currentStyle.equals("contact_online")) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add("contact_online_shine");
                currentStyle = "contact_online_shine";
            }
        });

        contactLabel.getLabel().setOnMouseExited(event -> {
            if (currentStyle.equals("contact_online_shine")) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add("contact_online");
            }

            if (currentStyle.equals("contact_offline_shine")) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add("contact_offline");
            }
        });

        contactLabel.getLabel().setOnMouseClicked(cl -> openPrivateConversation());
    }

    private void openPrivateConversation() {
        //TODO action after mouse button clicked at assigned label
    }

    private void searchingAction() {
        loupeButton.setOnMouseClicked(event -> {
            displaySearch(loupeField.getText());
        });

        loupeField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                displaySearch(loupeField.getText());
            }
        });
    }

    private void displaySearch(String text) {
        fxmlLoader.loadNewStageWithData(FxmlLoaderTemplate.SceneType.SEARCH_CONTACTS, text);
    }

    private void addControlPanelButtonsActions() {
        settingButton.setOnMouseClicked(event -> fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.SETTINGS, user, event));

        lookForRoom.setOnMouseClicked(event -> fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.ROOM_SEARCH, user, event));

        newRoom.setOnMouseClicked(event -> fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.ROOM_CREATION, user, event));
    }

    @Override
    public void actionOnMessage(ChatMessage chatMessage) {
        Label label = new Label();
        label.setWrapText(true);
        label.getStyleClass().add("message");

        label.setText(chatMessage.getSendTime() + ", " + chatMessage.getUsernmame() + "\n> " + chatMessage.getContent());

        Platform.runLater(() -> {
            chatWindow.getChildren().add(label);
            chatWindowMoving();
        });
    }

}
