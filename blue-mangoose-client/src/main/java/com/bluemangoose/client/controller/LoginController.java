package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.daemon.ChatUserStatusUpdateDaemon;
import com.bluemangoose.client.logic.daemon.NotificationsUpdateDaemon;
import com.bluemangoose.client.logic.reader.DefaultSettingsManager;
import com.bluemangoose.client.logic.reader.SettingReader;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.logic.web.impl.UserCredentialExchange;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.personal.LoginCredential;
import com.bluemangoose.client.model.personal.ProfilePreferences;
import com.bluemangoose.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.File;
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
                ProfilePreferences profilePreferences;
                try {
                    profilePreferences = fetchProfile();
                } catch (RuntimeException re) {
                    new Alerts().error("Bład pobierania", "Nie można pobrać Twojego profilu z serwera.",
                            "Prawdopodobnie Twój profil jest uszkodzony, albo serwer uległ awarii. Cierpliwości.");
                    return;
                }
                SessionCache.getInstance().setUser(user);
                SessionCache.getInstance().setProfilePreferences(profilePreferences);
                SessionCache.getInstance().setProfilePicture(fetchPicture());
                new NotificationsUpdateDaemon(profilePreferences).updateState(user.getUserId()); //daemon
                fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event);
            }
            else {
                new Alerts().loginFailed();
            }

        });
    }

    private ProfilePreferences fetchProfile() {
        return new HttpServerConnectorImpl<>(ProfilePreferences.class).get(ApiPath.PROFILE);
    }

    private Image fetchPicture() {
        Image image = new HttpServerConnectorImpl<>(File.class).getImage(ApiPath.AVATAR_GET);

        if (image.isError()) {
            image = new Image("/img/user.png");
        }

        return image;
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
        ApiPath apiPath = ApiPath.USER_HANDLE;
        apiPath.username = usernameField.getText();
        return userCredentialExchange.retrieveProfile(apiPath);
    }

}
