package com.meksula.chat.client.controller;

import com.meksula.chat.client.controller.loader.DataInitializable;
import com.meksula.chat.client.controller.loader.FxmlLoader;
import com.meksula.chat.client.controller.loader.FxmlLoaderTemplate;
import com.meksula.chat.client.model.dto.Room;
import com.meksula.chat.client.model.logic.RoomSearcher;
import com.meksula.chat.client.model.logic.impl.RoomSearcherImpl;
import com.meksula.chat.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author
 * Karol Meksuła
 * 19-07-2018
 * */

public class RoomSearchController implements Initializable, DataInitializable {
    private FxmlLoader loader = new FxmlLoaderTemplate();
    private RoomSearcher roomSearcher;
    private Set<Room> roomSet;
    private User user;
    private final int ROOM_AMOUNT = 15;
    private int currentRoomPage = 0;

    @FXML
    private Button back;

    @FXML
    private VBox buttonList, idVbox, authorVBox, peopleNumVbox, passwordVbox;

    @FXML
    private ImageView next;

    @Override
    public void initData(Object data) {
        this.user = (User) data;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox[] room = {idVbox, authorVBox, peopleNumVbox, passwordVbox};

        for (VBox vBox : room) {
            vBox.setSpacing(5);
        }

        this.roomSearcher = new RoomSearcherImpl();
        this.roomSet = roomSearcher.searchRooms(ROOM_AMOUNT);

        addBackButtonAction();
        addNextStyle();
        drawRooms(0, ROOM_AMOUNT);
    }

    private void addNextStyle() {
        Image active = new Image("/img/next-active.png");
        Image inactice = new Image("/img/next-inactive.png");

        next.setOnMouseEntered(event -> next.setImage(active));
        next.setOnMouseExited(event -> next.setImage(inactice));
        next.setOnMouseClicked(event -> nextRoomPage());
    }

    private void nextRoomPage() {
        //TODO
        currentRoomPage++;
    }

    private void addBackButtonAction() {
        back.setOnMouseClicked(event -> loader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event));
    }

    private void drawRooms(int begin, int end) {
        AtomicInteger count = new AtomicInteger(0);

        roomSet.forEach(room -> {
            if (count.intValue() >= begin && count.intValue() < end) {
                idVbox.getChildren().add(createLabel(String.valueOf(room.getRoomId())));
                authorVBox.getChildren().add(createLabel(room.getAuthor()));
                peopleNumVbox.getChildren().add(createLabel(String.valueOf(room.getPeople())));
                passwordVbox.getChildren().add(createLabel(String.valueOf(room.isPasswordRequired())));

                buttonList.getChildren().add(createJoinButton(room.getRoomId()).imageView);
            }
            count.incrementAndGet();
        });
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("contact_search");
        return label;
    }

    private ImageButton createJoinButton(String roomId) {
        Image inactive = new Image("/img/join-chat-inactive.png");
        Image active = new Image("/img/join-chat-active.png");

        ImageButton imageButton = new ImageButton(roomId);
        ImageView imageView = imageButton.getImageView();
        imageView.setImage(inactive);

        imageView.setOnMouseEntered(event -> imageView.setImage(active));
        imageView.setOnMouseExited(event -> imageView.setImage(inactive));
        imageView.setOnMouseClicked(event -> joinRoom(imageButton.roomId));

        return imageButton;
    }

    private void joinRoom(String roomId) {
        //TODO logika przechodzenia do pokoju
        System.out.println(roomId);
    }

    class ImageButton {
        private ImageView imageView;
        private String roomId;

        public ImageButton(String roomId) {
            this.roomId = roomId;
            this.imageView = new ImageView();
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

}
