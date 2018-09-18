package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.RegistrationForm;
import com.bluemangoose.client.logic.web.impl.UserCredentialExchange;
import com.bluemangoose.client.model.alert.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author
 * Karol Meksuła
 * 15-07-2018
 * */

public class RegistrationController implements Initializable {
    public ImageView refresh;
    public ImageView logo;

    private FxmlLoader fxmlLoader;
    private AtomicBoolean anim = new AtomicBoolean(true);

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
        refresh.setVisible(true);
    }

    private void setRegistrationAction() {
        registerButton.setOnMouseClicked(event -> {
            if (anim.get())
                waitAnim();

            boolean flag = executePostRequest();

            if (flag) {
                new Alerts().registrationSuccessfullAlert();
            }
            else {
                new Alerts().registrationFailAlert("Flag is " + flag);
            }

            this.anim.set(false);
            fxmlLoader.loadSameStage("/templates/home.fxml", event);
        });
    }

    private void waitAnim() {
        Runnable runnable = () -> {
            int c = 1;
            while (anim.get()) {
                Image in = new Image("/img/refresh-perp.png");
                Image ac = new Image("/img/refresh-hor.png");
                if (c % 2 == 0) {
                    refresh.setImage(in);
                } else refresh.setImage(ac);
                c++;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    private boolean executePostRequest() {
        return new UserCredentialExchange().registration(buildForm(), ApiPath.REGISTRATION);
    }

    private RegistrationForm buildForm() {
        RegistrationForm form = new RegistrationForm();
        form.setUsername(usernameForm.getText());
        form.setEmail(emailField.getText());
        form.setPassword(passwordForm.getText());
        form.setPasswordConfirmation(passwordConfirmForm.getText());
        return form;
    }

}
