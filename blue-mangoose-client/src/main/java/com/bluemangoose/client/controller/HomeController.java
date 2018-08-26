package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.model.gui.TitleAnimation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private FxmlLoader fxmlLoader;
    private TitleAnimation titleAnimation;

    @FXML
    private ImageView registerButton, loginButton;

    @FXML
    private Label b, l, u, e, m, a, n, g, o1, o2, s, e2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fxmlLoader = new FxmlLoaderTemplate();
        setLoginAction();
        setRegisterAction();
        titleAnimation = new TitleAnimation(new Label[] {b, l, u, e, m, a, n, g, o1, o2, s, e2});
        titleAnimation.animation();
    }

    private void setLoginAction() {
        loginButton.setOnMouseEntered(event -> loginButton.setImage(new Image("/img/login-active.png")));
        loginButton.setOnMouseExited(event -> loginButton.setImage(new Image("/img/login.png")));

        loginButton.setOnMouseClicked(event -> {
            titleAnimation.stop();
            final String path = "/templates/login.fxml";
            fxmlLoader.loadSameStage(path, event);
        });

    }

    private void setRegisterAction() {
        registerButton.setOnMouseEntered(event -> registerButton.setImage(new Image("/img/register-active.png")));
        registerButton.setOnMouseExited(event -> registerButton.setImage(new Image("/img/register.png")));

        registerButton.setOnMouseClicked(event -> {
            titleAnimation.stop();
            final String path = "/templates/registration.fxml";
            fxmlLoader.loadSameStage(path, event);
        });

    }

}