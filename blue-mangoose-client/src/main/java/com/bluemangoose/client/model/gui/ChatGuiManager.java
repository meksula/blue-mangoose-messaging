package com.bluemangoose.client.model.gui;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.logic.web.ChatRoomManager;
import com.bluemangoose.client.logic.web.impl.DefaultRoomManager;
import com.bluemangoose.client.logic.web.socket.ChatMessage;
import com.bluemangoose.client.logic.web.socket.ConversationHandler;
import com.bluemangoose.client.logic.web.socket.WebsocketReceiver;
import com.bluemangoose.client.model.alert.Alerts;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 24-08-2018
 * */

public class ChatGuiManager implements ChatManager, WebsocketReceiver {
    private ConversationHandler conversationHandler;
    private ChatRoomManager chatRoomManager = DefaultRoomManager.getInstance();
    private List<Label> messagesCache;
    private TitledPane chatPane;
    private VBox chatWindow;
    private ImageView postButton, disconnectButton, messageUp, messageDown;
    private TextArea messageField;
    private final int MAX_AMOUNT = 9;

    private ChatGuiManager(ChatGuiManagerBuilder builder) {
        conversationHandler = builder.conversationHandler;
        messagesCache = builder.messagesCache;
        chatPane = builder.chatPane;
        chatWindow = builder.chatWindow;
        postButton = builder.postButton;
        disconnectButton = builder.disconnectButton;
        messageUp = builder.messageUp;
        messageDown = builder.messageDown;
        messageField = builder.messageField;
    }

    @Override
    public void initChatGui() {
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

        messagesMoving();
        scrollMessages();
        addDisconnectChatAction();
        sendMessageAction();
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

    private void displayExistingMessages() {
        conversationHandler.getMessages().forEach(chatMessage -> {
            Label label = displayForeignMessage(chatMessage);
            chatWindow.getChildren().add(label);
            chatWindowMoving();
        });
    }

    private void messagesMoving() {
        Image current = messageUp.getImage();
        messageUp.setOnMouseEntered(event -> messageUp.setImage(new Image("/img/message-up-active.png")));
        messageUp.setOnMouseExited(event -> messageUp.setImage(current));
        messageUp.setOnMouseClicked(event -> scrollUp(chatWindow.getChildren()));
        messageUp.setCursor(Cursor.HAND);

        Image current2 = messageDown.getImage();
        messageDown.setOnMouseEntered(event -> messageDown.setImage(new Image("/img/message-down-active.png")));
        messageDown.setOnMouseExited(event -> messageDown.setImage(current2));
        messageDown.setOnMouseClicked(event -> scrollDown(chatWindow.getChildren()));
        messageDown.setCursor(Cursor.HAND);
    }

    private void scrollMessages() {
        chatPane.setOnScroll(event -> {
            int currentAmount = chatWindow.getChildren().size();
            double factor = event.getDeltaY();
            List<Node> messages = chatWindow.getChildren();

            if (factor > 0) {
                scrollUp(messages);
            } else if (currentAmount < MAX_AMOUNT) {
                scrollDown(messages);
            }

        });

    }

    private void scrollUp(List<Node> messages) {
        if (chatWindow.getChildren().size() <= MAX_AMOUNT && chatWindow.getChildren().size() > 0) {
            messagesCache.add((Label) messages.get(0));
            messages.remove(0);
        }
    }

    private void scrollDown(List<Node> messages) {
        if (chatWindow.getChildren().size() < MAX_AMOUNT && (messagesCache.size() - 1) >= 0) {
            Label tmp = messagesCache.get(messagesCache.size() - 1);
            messagesCache.remove(tmp);

            List<Node> updatedMessages = new ArrayList<>();
            updatedMessages.add(tmp);
            updatedMessages.addAll(messages);
            chatWindow.getChildren().clear();
            chatWindow.getChildren().setAll(updatedMessages);
        }
    }

    private void addDisconnectChatAction() {
        Image inactive = new Image("/img/disconnect.png");
        Image active = new Image("/img/disconnect-active.png");

        disconnectButton.setCursor(Cursor.HAND);
        disconnectButton.setOnMouseEntered(event -> disconnectButton.setImage(active));
        disconnectButton.setOnMouseExited(event -> disconnectButton.setImage(inactive));
        disconnectButton.setOnMouseClicked(event -> disconnectChat());
    }

    private void disconnectChat() {
        chatRoomManager.disconnect();
        chatWindow.getChildren().clear();
        chatPane.setText("DISCONNECTED");
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

        if (chatMessage.getUsernmame().equals("ADMIN")) {
            label.getStyleClass().clear();
            label.getStyleClass().add("error");
        }

        LocalTime localTime = LocalTime.now();
        String time = localTime.format(DateTimeFormatter.ofPattern("kk:mm:ss"));
        label.setText(time + ", " + chatMessage.getUsernmame() + "\n> " + chatMessage.getContent());

        return label;
    }

    public void chatWindowMoving() {
        int amount = chatWindow.getChildren().size();

        if (amount >= 9) {
            Label oldest = (Label) chatWindow.getChildren().get(0);
            messagesCache.add(oldest);
            chatWindow.getChildren().remove(0);
        }

    }

    public static class ChatGuiManagerBuilder {
        private ConversationHandler conversationHandler;
        private List<Label> messagesCache;
        private TitledPane chatPane;
        private VBox chatWindow;
        private ImageView postButton, disconnectButton, messageUp, messageDown;
        private TextArea messageField;

        public ChatGuiManager build() {
            return new ChatGuiManager(this);
        }

        public ChatGuiManagerBuilder conversationHandler(ConversationHandler conversationHandler) {
            this.conversationHandler = conversationHandler;
            return this;
        }

        public ChatGuiManagerBuilder messagesCache(List<Label> labels) {
            this.messagesCache = labels;
            return this;
        }

        public ChatGuiManagerBuilder chatPane(TitledPane titledPane) {
            this.chatPane = titledPane;
            return this;
        }

        public ChatGuiManagerBuilder chatWindow(VBox vBox) {
            this.chatWindow = vBox;
            return this;
        }

        public ChatGuiManagerBuilder postButton(ImageView imageView) {
            this.postButton = imageView;
            return this;
        }

        public ChatGuiManagerBuilder disconnectButton(ImageView imageView) {
            this.disconnectButton = imageView;
            return this;
        }

        public ChatGuiManagerBuilder messageUp(ImageView imageView) {
            this.messageUp = imageView;
            return this;
        }

        public ChatGuiManagerBuilder messageDown(ImageView imageView) {
            this.messageDown = imageView;
            return this;
        }

        public ChatGuiManagerBuilder messageField(TextArea textArea) {
            this.messageField = textArea;
            return this;
        }

    }

}
