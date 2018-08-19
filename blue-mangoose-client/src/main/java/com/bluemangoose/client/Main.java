package com.bluemangoose.client;

import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Author
 * Karol Meksuła
 * 14-07-2018
 * */

public class Main extends Application {
    public static String runMode;
    public static boolean isRunning;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/templates/home.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            isRunning = false;
            logout();
        });
        isRunning = true;
    }

    private void logout() {
        new HttpServerConnectorImpl<>(String.class).get(ApiPath.LOGOUT);
    }

    public static void main(String[]args) {
        Main.runMode = args[0];
        launch(args);
    }

}
