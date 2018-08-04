package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.logic.web.socket.ChatForm;
import com.bluemangoose.client.model.dto.ChatRoom;
import com.bluemangoose.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

public class RoomCreationController implements DataInitializable, Initializable {
    private FxmlLoader loader;
    private User user;

    @FXML
    private Button back, createRoom;

    @FXML
    private CheckBox securedCheckBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField chatNameField;

    @Override
    public void initData(Object data) {
        this.user = (User) data;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loader = new FxmlLoaderTemplate();

        backAction();
        roomCreate();
    }

    private void backAction() {
        back.setOnMouseClicked(event -> loader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event));
    }

    private void roomCreate() {
        createRoom.setOnMouseClicked(event -> {
            createRoomRequest();
            loader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event);
        });
    }

    private void createRoomRequest() {
        ChatForm chatForm = new ChatForm(
                chatNameField.getText(),
                SessionCache.getInstance().getProfilePreferences().getProfileUsername(),
                securedCheckBox.isSelected(),
                passwordField.getText());

        new HttpServerConnectorImpl<>(ChatRoom.class).post(chatForm, ApiPath.CHAT_ROOM_CREATE);
    }

}