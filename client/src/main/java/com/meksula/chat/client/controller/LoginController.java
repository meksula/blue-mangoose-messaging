package com.meksula.chat.client.controller;

import com.meksula.chat.client.controller.loader.FxmlLoader;
import com.meksula.chat.client.controller.loader.FxmlLoaderTemplate;
import com.meksula.chat.client.logic.web.ApiPath;
import com.meksula.chat.client.logic.web.impl.UserCredentialExchange;
import com.meksula.chat.client.model.alert.Alerts;
import com.meksula.chat.client.model.personal.LoginCredential;
import com.meksula.chat.client.model.personal.User;
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
 * 14-07-2018
 * */

public class LoginController implements Initializable {
    private FxmlLoader fxmlLoader;
    private UserCredentialExchange userCredentialExchange;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordForm;

    @FXML
    private Button loginConfirm;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fxmlLoader = new FxmlLoaderTemplate();
        this.userCredentialExchange = new UserCredentialExchange();
        loginHandler();
    }

    private void loginHandler() {
        loginConfirm.setOnMouseClicked(event -> {
            if (access()) {
                User user = fetchUser();
                fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event);
            }
            else {
                new Alerts().loginFailed();
            }

        });
    }

    private boolean access() {
        LoginCredential loginCredential = new LoginCredential(usernameField.getText(), passwordForm.getText());
        return userCredentialExchange.login(loginCredential, ApiPath.LOGIN);
    }

    private User fetchUser() {
        ApiPath apiPath = ApiPath.PROFILE;
        apiPath.username = usernameField.getText();
        return userCredentialExchange.retrieveProfile(apiPath);
    }

}
