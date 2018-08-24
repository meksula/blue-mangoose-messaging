package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.reader.DefaultSettingsManager;
import com.bluemangoose.client.logic.reader.SettingReader;
import com.bluemangoose.client.logic.reader.SettingWriter;
import com.bluemangoose.client.logic.reader.SettingsProperties;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 18-07-2018
 * */

public class SettingsController implements Initializable, DataInitializable {
    private FxmlLoader fxmlLoader;
    private User user;
    private File picture;
    private SettingWriter settingWriter;
    private SessionCache sessionCache = SessionCache.getInstance();

    @FXML
    private Label path;

    @FXML
    private Button choosePicture, back, post, saveProfileData;

    @FXML
    private TextField defaultRoomName;

    @FXML
    private CheckBox invisibleStatus;

    public SettingsController() {
        this.settingWriter = new DefaultSettingsManager();
    }

    @Override
    public void initData(Object data) {
        this.user = (User) data;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fxmlLoader = new FxmlLoaderTemplate();
        addChooseAction();
        addPostAction();
        addBackAction();
        addSaveProfileAction();
    }

    private void addSaveProfileAction() {
        saveProfileData.setOnMouseClicked(event -> {
            String username = sessionCache.getTypedUsername();
            String password = sessionCache.getTypedPassword();

            settingWriter.updateSettings(SettingsProperties.SETTING_ENABLED, "true");
            settingWriter.updateSettings(SettingsProperties.SAVED_USER, username);
            settingWriter.updateSettings(SettingsProperties.PASSWORD, password);
        });
    }

    private void addChooseAction() {
        choosePicture.setOnMouseClicked(event -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz swoje zdjęcie: ");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            this.picture = fileChooser.showOpenDialog(stage);

            if (picture == null) {
                return;
            }
            setPathText(picture.getPath());
        });
    }

    private void addPostAction() {
        post.setOnMouseClicked(event -> {
            if (picture == null) {
                return;
            }

            String result = new HttpServerConnectorImpl<>(String.class).putFile(ApiPath.AVATAR, picture);

            if (result.equals("true")) {
                fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event);
            } else {
                new Alerts().settingsError();
            }
        });

    }

    private void setPathText(String path) {
        this.path.setText(path);
    }

    private void addBackAction() {
        back.setOnMouseClicked(event ->
                fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event));
    }

}
