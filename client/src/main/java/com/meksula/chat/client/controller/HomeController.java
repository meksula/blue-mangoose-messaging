package com.meksula.chat.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 17-07-2018
 * */

public class HomeController implements Initializable {

    @FXML
    private ImageView registerButton, loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLoginAction();
        setRegisterAction();
    }

    private void setLoginAction() {
        loginButton.setOnMouseEntered(event -> loginButton.setImage(new Image("/img/login-active.png")));
        loginButton.setOnMouseExited(event -> loginButton.setImage(new Image("/img/login.png")));

        loginButton.setOnMouseClicked(event -> {
            final String path = "/templates/login.fxml";
            new FxmlLoaderTemplate().loadFxml(path, event);
        });

    }

    private void setRegisterAction() {
        registerButton.setOnMouseEntered(event -> registerButton.setImage(new Image("/img/register-active.png")));
        registerButton.setOnMouseExited(event -> registerButton.setImage(new Image("/img/register.png")));

        registerButton.setOnMouseClicked(event -> {

        });
    }

}