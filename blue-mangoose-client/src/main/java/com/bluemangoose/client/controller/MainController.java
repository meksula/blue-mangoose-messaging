package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.CurrentChatCache;
import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.logic.daemon.ChatUserStatusUpdateDaemon;
import com.bluemangoose.client.logic.daemon.LetterStatusDaemon;
import com.bluemangoose.client.logic.web.ChatRoomManager;
import com.bluemangoose.client.logic.web.impl.DefaultRoomManager;
import com.bluemangoose.client.logic.web.socket.ConversationHandler;
import com.bluemangoose.client.model.dto.Mail;
import com.bluemangoose.client.model.gui.ChatGuiManager;
import com.bluemangoose.client.model.gui.ContactLabel;
import com.bluemangoose.client.model.personal.Contact;
import com.bluemangoose.client.model.personal.ContactAddNotification;
import com.bluemangoose.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author
 * Karol Meksuła
 * 14-07-2018
 * */

public class MainController implements Initializable, DataInitializable, SceneRefresh {
    private List<Label> messagesCache = new ArrayList<>();
    private User user;
    private FxmlLoader fxmlLoader;
    private ChatRoomManager chatRoomManager;
    private ConversationHandler conversationHandler;
    private List<ContactLabel> contactLabels;
    private ChatUserStatusUpdateDaemon statusUpdateDaemon;
    private LetterStatusDaemon letterStatusDaemon;
    private AtomicBoolean mailReceived = SessionCache.getInstance().getLettersUnsealed();

    @FXML
    private ImageView loupeButton, autonomicWindowButton, disconnectButton, bell, messageUp, messageDown,
            userAvatar, postButton, mailbox;

    @FXML
    private VBox contactsVbox, chatWindow;
    
    @FXML
    private Label settingButton, newRoom, lookForRoom, usernameField;

    @FXML
    private TextField loupeField;

    @FXML
    private TextArea messageField;

    @FXML
    private TitledPane chatPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fxmlLoader = new FxmlLoaderTemplate();
        this.contactLabels = new ArrayList<>();

        chatWindow.setSpacing(3);

        addAutonomicWindowAction();
        addControlPanelButtonsActions();
        addButtonActions();
        searchingAction();
        addBellAction();
        contactDisplay(SessionCache.getInstance().getProfilePreferences().getContactsBook());

        usernameField.setText(SessionCache.getInstance().getProfilePreferences().getProfileUsername());

        statusUpdateDaemon = new ChatUserStatusUpdateDaemon(this);
        statusUpdateDaemon.updateState(SessionCache.getInstance().getProfilePreferences().getUserId());

        letterStatusDaemon = new LetterStatusDaemon(this);
        letterStatusDaemon.updateState(SessionCache.getInstance().getProfilePreferences().getUserId());

        if (SessionCache.getInstance().getProfilePicture() != null) {
            userAvatar.setImage(SessionCache.getInstance().getProfilePicture());
        }

    }

    @Override
    public void initData(Object data) {
        this.user = (User) data;
        this.chatRoomManager = DefaultRoomManager.getInstance();
        this.conversationHandler = ConversationHandler.getInstance(chatRoomManager.getRoomTarget(), 100);

        new ChatGuiManager.ChatGuiManagerBuilder()
                .conversationHandler(conversationHandler)
                .messagesCache(messagesCache)
                .postButton(postButton)
                .messageField(messageField)
                .disconnectButton(disconnectButton)
                .messageUp(messageUp)
                .messageDown(messageDown)
                .chatPane(chatPane)
                .chatWindow(chatWindow)
                .build()
                .initChatGui();

        addMailboxAction();
    }

    private void addMailboxAction() {
        mailbox.setCursor(Cursor.HAND);
        Image previous = mailbox.getImage();
        Image active = new Image("/img/mailbox_interact.png");
        mailbox.setOnMouseEntered(event -> mailbox.setImage(active));
        mailbox.setOnMouseExited(event -> mailbox.setImage(previous));
        mailbox.setOnMouseClicked(event -> {
            this.mailReceived.set(false);
            fxmlLoader.loadNewStage("/templates/mailbox.fxml");
        });
    }

    private void addAutonomicWindowAction() {
        Image inactive = new Image("/img/max.png");
        Image active = new Image("/img/max-active.png");

        autonomicWindowButton.setCursor(Cursor.HAND);
        autonomicWindowButton.setOnMouseEntered(event -> autonomicWindowButton.setImage(active));
        autonomicWindowButton.setOnMouseExited(event -> autonomicWindowButton.setImage(inactive));
        autonomicWindowButton.setOnMouseClicked(event -> popupChatWindow());
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
        if (chatRoomManager.getRoomTarget() != null) {
            CurrentChatCache.getInstance().setMessagesCache(messagesCache);
            fxmlLoader.loadNewStageWithData(FxmlLoaderTemplate.SceneType.CHAT_DETACHED, conversationHandler);
        }
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

        postButton.setOnMouseEntered(event ->
            postButton.setImage(new Image("/img/send-active.png")));

        postButton.setOnMouseExited(event ->
            postButton.setImage(new Image("/img/send-inactive.png")));

    }

    private void contactDisplay(Set<Contact> contacts) {
        this.contactLabels = new ArrayList<>();

        for (Contact contact : contacts) {
            Label label = new Label();

            if (contact.isOnline()) {
                label.getStyleClass().add("contact_online");
            }
            else {
                label.getStyleClass().add("contact_offline");
            }

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
            else {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add("contact_offline");
            }
        });
    }

    @Override
    public void refresh() {
        Map<String, Boolean> currentStatus = SessionCache.getInstance().getContactsStatus();
        currentStatus.forEach((key, value) ->
                contactLabels
                        .stream()
                        .filter(contactLabel -> contactLabel.getContact().getUsername().equals(key))
                        .findAny()
                        .get()
                        .getContact().setOnline(value)
        );

        statusUpdate();

        if (SessionCache.getInstance().getLettersUnsealed().get()) {
            mailboxIconBlinking();
        }
    }

    private void mailboxIconBlinking() {
        Runnable blinking = new Runnable() {
            Image first = new Image("/img/mailbox_inactive.png");
            Image second = new Image("/img/mailbox_received.png");
            Image third = new Image("/img/mailbox_received_blink.png");
            Image[] icons = { first, second, third };
            int counter = 0;

            @Override
            public void run() {
                while (MainController.this.mailReceived.get()) {
                    mailbox.setImage(icons[counter]);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        MainController.this.mailReceived.set(false);
                    }

                    if (counter < 2) {
                        counter++;
                    } else {
                        counter = 0;
                    }
                }
            }
        };

        Thread blinkThread = new Thread(blinking);
        blinkThread.setDaemon(true);
        blinkThread.start();
    }

    private String currentStyle;
    private void contactLabelAction(ContactLabel contactLabel) {
        final String CONT_ONLINE = "contact_online";
        final String CONT_ONLINE_SH = "contact_online_shine";
        final String CONT_OFFLINE = "contact_offline";
        final String CONT_OFFLINE_SH = "contact_offline_shine";
        contactLabel.getLabel().setOnMouseEntered(event -> {
            try {
                currentStyle = contactLabel.getLabel().getStyleClass().get(1);
            } catch (IndexOutOfBoundsException ioobe) {
                if (contactLabel.getContact().isOnline()) {
                    currentStyle = CONT_ONLINE;
                }
                else {
                    currentStyle = CONT_OFFLINE;
                }
            }

            if (currentStyle.equals(CONT_OFFLINE)) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add(CONT_OFFLINE_SH);
                currentStyle = CONT_OFFLINE_SH;
            }

            if (currentStyle.equals(CONT_ONLINE)) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add(CONT_ONLINE_SH);
                currentStyle = CONT_ONLINE_SH;
            }
        });

        contactLabel.getLabel().setOnMouseExited(event -> {
            if (currentStyle.equals(CONT_ONLINE_SH)) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add(CONT_ONLINE);
            }

            if (currentStyle.equals(CONT_OFFLINE_SH)) {
                contactLabel.getLabel().getStyleClass().clear();
                contactLabel.getLabel().getStyleClass().add(CONT_OFFLINE);
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

}
