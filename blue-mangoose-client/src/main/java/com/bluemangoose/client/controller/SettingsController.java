package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label path;

    @FXML
    private Button choosePicture, back, post;

    @Override
    public void initData(Object data) {
        this.user = (User) data;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fxmlLoader = new FxmlLoaderTemplate();
        addChooseAction();
        addBackAction();
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

    private void setPathText(String path) {
        this.path.setText(path);
    }

    private void addBackAction() {
        back.setOnMouseClicked(event ->
                fxmlLoader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event));
    }

}
