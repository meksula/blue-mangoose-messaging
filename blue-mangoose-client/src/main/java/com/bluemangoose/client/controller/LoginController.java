package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.reader.DefaultSettingsManager;
import com.bluemangoose.client.logic.reader.SettingReader;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.impl.UserCredentialExchange;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.personal.LoginCredential;
import com.bluemangoose.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 14-07-2018
 * */

public class LoginController implements Initializable {
    private FxmlLoader fxmlLoader;
    private UserCredentialExchange userCredentialExchange;
    private SettingReader settingReader;
    public static String username;

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

        this.settingReader = new DefaultSettingsManager();
        Map<String, String> settings = settingReader.loadSettings();

        usernameField.setText(settings.get("prop2"));
        passwordForm.setText(settings.get("prop3"));
    }

    private void loginHandler() {
        loginConfirm.setOnMouseClicked(event -> {
            if (access()) {
                LoginController.username = usernameField.getText();
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
        try {
            return userCredentialExchange.login(loginCredential, ApiPath.LOGIN);
        } catch (RuntimeException re) {
            return false;
        }
    }

    private User fetchUser() {
        ApiPath apiPath = ApiPath.PROFILE;
        apiPath.username = usernameField.getText();
        return userCredentialExchange.retrieveProfile(apiPath);
    }

}
