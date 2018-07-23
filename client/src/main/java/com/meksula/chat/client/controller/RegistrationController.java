package com.meksula.chat.client.controller;

import com.meksula.chat.client.controller.loader.FxmlLoader;
import com.meksula.chat.client.controller.loader.FxmlLoaderTemplate;
import com.meksula.chat.client.logic.web.ApiPath;
import com.meksula.chat.client.logic.web.RegistrationForm;
import com.meksula.chat.client.logic.web.impl.UserCredentialExchange;
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
            boolean flag = executePostRequest();

            if (flag) {
                new Alerts().registrationSuccessfullAlert();
            }
            else {
                new Alerts().registrationFailAlert("Flag is " + flag);
            }

            fxmlLoader.loadSameStage("/templates/home.fxml", event);
        });
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
