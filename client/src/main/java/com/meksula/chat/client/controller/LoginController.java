package com.meksula.chat.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 14-07-2018
 * */

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordForm;

    @FXML
    private Button loginConfirm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginHandler();
    }

    private void loginHandler() {
        loginConfirm.setOnMouseClicked(event -> {
            final String path = "/templates/main.fxml";
            new FxmlLoaderTemplate().loadFxml(path, event);
        });
    }

}
