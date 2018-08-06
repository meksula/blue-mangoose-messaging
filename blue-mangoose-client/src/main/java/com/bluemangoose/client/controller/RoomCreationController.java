package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.logic.web.socket.ChatForm;
import com.bluemangoose.client.model.alert.Alerts;
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
    private PasswordField passwordField, passwordConfirmationField;

    @FXML
    private TextField chatNameField, roomCapacity;

    @Override
    public void initData(Object data) {
        this.user = (User) data;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loader = new FxmlLoaderTemplate();

        inactiveFields();
        backAction();
        roomCreate();
    }

    private void inactiveFields() {
        passwordFieldsActive(true);
        securedCheckBox.setOnMouseClicked(event -> passwordFieldsActive(!passwordField.isDisable()));
    }

    private void passwordFieldsActive(boolean isActive) {
        passwordField.setDisable(isActive);
        passwordConfirmationField.setDisable(isActive);
    }

    private void backAction() {
        back.setOnMouseClicked(event -> loader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event));
    }

    private void roomCreate() {
        createRoom.setOnMouseClicked(event -> {
            if (createRoomRequest()) {
                loader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event);
            }
        });
    }

    private boolean createRoomRequest() {
        ChatForm chatForm = buildChatForm();

        if (chatForm == null) {
            return false;
        }

        new HttpServerConnectorImpl<>(ChatRoom.class).post(chatForm, ApiPath.CHAT_ROOM_CREATE);
        return true;
    }

    private ChatForm buildChatForm() {
        if (!passwordField.getText().equals(passwordConfirmationField.getText())) {
            new Alerts().error("Hasła nie są jednakowe.", "",
                    "Niestety, hasła nie są takie same.\nWprowadź dane jeszcze raz");
            return null;
        }

        if (chatNameField.getText().length() < 4 || chatNameField.getText().length() > 25) {
            new Alerts().error("Nie podałeś nazwy.", "",
                    "Zapomniałeś podać nazwy pokoju chatu.\nWprowadź dane jeszcze i kliknij jeszcze raz.");
            return null;
        }

        ChatForm chatForm = new ChatForm(
                chatNameField.getText(),
                SessionCache.getInstance().getProfilePreferences().getProfileUsername(),
                securedCheckBox.isSelected(),
                passwordField.getText());

        chatForm.setCapacity(getCapacity());

        return chatForm;
    }

    private int getCapacity() {
        int capacity;
        try {
            capacity = Integer.parseInt(roomCapacity.getText());
        } catch (Exception ex) {
            return 0;
        }
        return capacity;
    }

}