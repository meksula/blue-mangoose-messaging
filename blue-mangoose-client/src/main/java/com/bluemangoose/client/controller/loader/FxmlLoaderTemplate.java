package com.bluemangoose.client.controller.loader;

import com.bluemangoose.client.controller.*;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @Author
 * Karol Meksuła
 * 14-07-2018
 * */

public class FxmlLoaderTemplate implements FxmlLoader {

    @Override
    public void loadNewStage(final String PATH) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
        Scene scene = sceneLoad(loader);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void loadSameStage(final String PATH, final Event event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
        Scene scene = sceneLoad(loader);

        loadSameStage(scene, event);
    }

    @Override
    public void loadSameStage(String PATH, Node node) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH));
        Scene scene = sceneLoad(loader);

        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void loadNewStageWithData(final SceneType sceneType, final Object DATA) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneType.getPath()));
        Scene scene = sceneLoad(loader);

        sceneType.loadData(loader, DATA);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void loadSameStageWithData(final FxmlLoaderTemplate.SceneType sceneType, final Object DATA, final Event event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneType.getPath()));
        Scene scene = sceneLoad(loader);

        sceneType.loadData(loader, DATA);
        loadSameStage(scene, event);
    }

    @Override
    public void loadSameStageWithData(final FxmlLoaderTemplate.SceneType sceneType, final Object DATA, final Node node) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneType.getPath()));
        Scene scene = sceneLoad(loader);

        sceneType.loadData(loader, DATA);

        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private Scene sceneLoad(FXMLLoader loader) {
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = null;
        if (parent != null) {
            scene = new Scene(parent);
        }
        return scene;
    }

    private void loadSameStage(Scene scene, Event event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public enum SceneType {
        SETTINGS {
            @Override
            protected void loadData(final FXMLLoader loader, final Object object) {
                SettingsController controller = loader.getController();
                controller.initData(object);
            }

            @Override
            public String getPath() {
                return "/templates/settings.fxml";
            }
        },

        SEARCH_CONTACTS {
            @Override
            protected void loadData(final FXMLLoader loader, final Object data) {
                SearchContactWindowController controller = loader.getController();
                controller.initData(data);
            }

            @Override
            public String getPath() {
                return "/templates/search_contacts.fxml";
            }
        },

        MAIN {
            @Override
            protected void loadData(final FXMLLoader loader, final Object object){
                MainController controller = loader.getController();
                controller.initData(object);
            }

            @Override
            public String getPath() {
                return "/templates/main.fxml";
            }
        },

        ROOM_SEARCH {
            @Override
            protected void loadData(FXMLLoader loader, Object object) {
                RoomSearchController controller = loader.getController();
                controller.initData(object);
            }

            @Override
            public String getPath() {
                return "/templates/room_search.fxml";
            }
        },

        ROOM_CREATION {
            @Override
            protected void loadData(FXMLLoader loader, Object object) {
                RoomCreationController controller = loader.getController();
                controller.initData(object);
            }

            @Override
            public String getPath() {
                return "/templates/room_new.fxml";
            }
        },

        CHAT_DETACHED {
            @Override
            protected void loadData(FXMLLoader loader, Object object) {
            }

            @Override
            public String getPath() {
                return "/templates/detached_chat.fxml";
            }

        }, NOTIFICATION_DETACHED {
            @Override
            protected void loadData(FXMLLoader loader, Object object) {
                DetachedNotificationController controller = loader.getController();
                controller.initData(object);
            }

            @Override
            public String getPath() {
                return "/templates/detached_notification.fxml";
            }
        };

        protected abstract void loadData(final FXMLLoader loader, final Object object);

        public abstract String getPath();
    }

}
