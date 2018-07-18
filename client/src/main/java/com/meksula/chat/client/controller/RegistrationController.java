package com.meksula.chat.client.controller;

import com.meksula.chat.client.model.alert.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class RegistrationController implements Initializable {
    private FxmlLoader fxmlLoader;

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameForm;

    @FXML
    private PasswordField passwordForm, passwordConfirmForm;

    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fxmlLoader = new FxmlLoaderTemplate();
        setRegistrationAction();
    }

    private void setRegistrationAction() {
        registerButton.setOnMouseClicked(event -> {
            final String path = "/templates/home.fxml";
            fxmlLoader.loadSameStage(path, event);
            new Alerts().registrationSuccessfullAlert();
        });
    }

}
