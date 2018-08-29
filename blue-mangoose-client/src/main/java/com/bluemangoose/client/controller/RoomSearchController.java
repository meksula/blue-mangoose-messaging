package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.CurrentChatCache;
import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.DataInitializable;
import com.bluemangoose.client.controller.loader.FxmlLoader;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.web.ChatRoomManager;
import com.bluemangoose.client.logic.web.impl.DefaultRoomManager;
import com.bluemangoose.client.model.alert.Alerts;
import com.bluemangoose.client.model.dto.ChatAccess;
import com.bluemangoose.client.model.dto.ChatRoom;
import com.bluemangoose.client.model.personal.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @Author
 * Karol Meksuła
 * 19-07-2018
 * */

public class RoomSearchController implements Initializable, DataInitializable {
    private FxmlLoader loader = new FxmlLoaderTemplate();
    private ChatRoomManager chatRoomManager;
    private List<ChatRoom> roomList;
    private User user;
    public final int BEG_CONST = 16;
    public final int ROOM_AMOUNT = 15;
    private int currentRoomPage = 1;
    private VBox[] boxes;

    @FXML
    private Button back;

    @FXML
    private VBox buttonList, idVbox, authorVBox, peopleNumVbox, passwordVbox;

    @FXML
    private HBox pageStrip;

    @FXML
    private ImageView next;

    @Override
    public void initData(Object data) {
        this.user = (User) data;

        this.chatRoomManager = DefaultRoomManager.getInstance();
        this.roomList = chatRoomManager.fetchChatRoomList();
        drawRooms(0, ROOM_AMOUNT);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox[] room = {idVbox, authorVBox, peopleNumVbox, passwordVbox};
        this.boxes = room;

        for (VBox vBox : room) {
            vBox.setSpacing(5);
        }

        addBackButtonAction();
        addNextStyle();
    }

    private void clearRoomBox() {
        for (VBox box : boxes) {
            box.getChildren().clear();
        }
        buttonList.getChildren().clear();
        pageStrip.getChildren().clear();
    }

    private void addNextStyle() {
        Image active = new Image("/img/next-active.png");
        Image inactice = new Image("/img/next-inactive.png");
        next.setCursor(Cursor.HAND);

        next.setOnMouseEntered(event -> next.setImage(active));
        next.setOnMouseExited(event -> next.setImage(inactice));
        next.setOnMouseClicked(event -> {
            if (pages() > currentRoomPage) {
                currentRoomPage++;
                int[] index = computeIndexes();
                drawRooms(index[0], index[1]);
            }
        });
    }

    private void addBackButtonAction() {
        back.setOnMouseClicked(event -> loader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, event));
    }

    private void drawRooms(int begin, int end) {
        clearRoomBox();
        roomsPagination();

        for (int i = begin; i < end; i++) {
            ChatRoom room;
            try {
                room = roomList.get(i);
            } catch (IndexOutOfBoundsException exception) {
                break;
            }

            idVbox.getChildren().add(createLabel(String.valueOf(room.getName())));
            authorVBox.getChildren().add(createLabel(room.getCreatorUsername()));
            peopleNumVbox.getChildren().add(createLabel(String.valueOf(room.getPeople())));
            passwordVbox.getChildren().add(createLabel(String.valueOf(room.isPasswordRequired())));

            buttonList.getChildren().add(createJoinButton(room).imageView);
        }

    }

    private void roomsPagination() {
        int rooms = roomList.size();
        int pages = (rooms / ROOM_AMOUNT) + 1;

        for(int i = 1; i < (pages + 1); i++) {
            Label pageIndicator = new Label(String.valueOf(i));
            pageIndicator.getStyleClass().add("text_label");
            pageIndicator.setCursor(Cursor.HAND);
            pageStrip.getChildren().add(pageIndicator);

            pageIndicator.setOnMouseEntered(event -> {
                pageIndicator.getStyleClass().clear();
                pageIndicator.getStyleClass().add("text_active");
            });
            pageIndicator.setOnMouseExited(event -> {
                pageIndicator.getStyleClass().clear();
                pageIndicator.getStyleClass().add("text_label");
            });
            pageIndicator.setOnMouseClicked(event -> {
                int page = Integer.parseInt(pageIndicator.getText());

                if (currentRoomPage == page) {
                    return;
                }

                this.currentRoomPage = page;
                int[] index = computeIndexes();

                clearRoomBox();
                drawRooms(index[0], index[1]);
            });
        }
    }

    private int pages() {
        return (roomList.size() / ROOM_AMOUNT) + 1;
    }

    private int[] computeIndexes() {
        int beginIndex = BEG_CONST * (currentRoomPage - 1);
        int lastIndex = ROOM_AMOUNT + ((ROOM_AMOUNT * (currentRoomPage - 1)) + currentRoomPage);
        return new int[] {beginIndex, lastIndex};
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("contact_search");
        return label;
    }

    private ImageButton createJoinButton(ChatRoom chatRoom) {
        Image inactive = new Image("/img/join-chat-inactive.png");
        Image active = new Image("/img/join-chat-active.png");

        ImageButton imageButton = new ImageButton(chatRoom);
        ImageView imageView = imageButton.getImageView();
        imageView.setImage(inactive);
        imageView.setCursor(Cursor.HAND);

        imageView.setOnMouseEntered(event -> imageView.setImage(active));
        imageView.setOnMouseExited(event -> imageView.setImage(inactive));
        imageView.setOnMouseClicked(event -> joinRoom(imageButton.getChatRoom()));

        return imageButton;
    }

    private void joinRoom(ChatRoom chatRoom) {
        ChatAccess chatAccess;
        if (chatRoom.isPasswordRequired()) {
            String entered = new Alerts().enterPassword();
            chatAccess = chatAccessBuild(chatRoom, entered);
        } else {
            chatAccess = chatAccessBuild(chatRoom, null);
        }

        CurrentChatCache.getInstance().setChatAccess(chatAccess);

        boolean isJoined = chatRoomManager.joinRoom(chatRoom.getName());

        if (!isJoined) {
            return;
        }

        loader.loadSameStageWithData(FxmlLoaderTemplate.SceneType.MAIN, user, back);
    }

    private ChatAccess chatAccessBuild(ChatRoom chatRoom, String enteredPassword) {
        String username = SessionCache.getInstance().getProfilePreferences().getProfileUsername();

        ChatAccess access = new ChatAccess();
        access.setUsername(username);
        access.setSecured(chatRoom.isPasswordRequired());
        access.setPassword(enteredPassword);
        access.setChatName(chatRoom.getName());

        return access;
    }

    class ImageButton {
        private ChatRoom chatRoom;
        private ImageView imageView;

        ImageButton(ChatRoom chatRoom) {
            this.chatRoom = chatRoom;
            this.imageView = new ImageView();
        }

        ChatRoom getChatRoom() {
            return chatRoom;
        }

        ImageView getImageView() {
            return imageView;
        }

    }

}
