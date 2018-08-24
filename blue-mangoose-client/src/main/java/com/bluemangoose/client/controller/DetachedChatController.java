package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.CurrentChatCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.logic.web.socket.ConversationHandler;
import com.bluemangoose.client.model.gui.ChatGuiManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

public class DetachedChatController implements DataInitializable {
    private ChatGuiManager chatGuiManager;
    private ConversationHandler conversationHandler;
    private List<Label> messagesCache;

    @FXML
    private TitledPane chatPane;

    @FXML
    private VBox chatWindow;

    @FXML
    private TextArea messageField;

    @FXML
    private ImageView postButton, disconnectButton, messageUp, messageDown;

    @Override
    public void initData(Object data) {
        this.conversationHandler = (ConversationHandler) data;
        this.messagesCache = CurrentChatCache.getInstance().getMessagesCache();

        chatGuiManager = new ChatGuiManager.ChatGuiManagerBuilder()
                .conversationHandler(conversationHandler)
                .messagesCache(messagesCache)
                .postButton(postButton)
                .messageField(messageField)
                .disconnectButton(disconnectButton)
                .messageUp(messageUp)
                .messageDown(messageDown)
                .chatPane(chatPane)
                .chatWindow(chatWindow)
                .build();

        chatGuiManager.initChatGui();
    }

}
