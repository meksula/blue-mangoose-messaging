package com.bluemangoose.client.controller;

import com.bluemangoose.client.model.dto.Letter;
import com.bluemangoose.client.model.dto.Topic;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

public class MailboxController implements Initializable {
    private List<Topic> topicList = new ArrayList<>();
    private List<Label> topicLetterList = new ArrayList<>();
    private final int MAX = 9;

    @FXML
    private VBox topicField;

    @FXML
    private VBox lettersField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Topic topic = new Topic("topic1 ");
        Topic topic2 = new Topic("topic2");
        Topic topic3 = new Topic("topic3");
        Topic topic4 = new Topic("topic4");
        Topic topic5 = new Topic("topic5");
        Topic topic6 = new Topic("topic6");
        Topic topic7 = new Topic("topic7");
        Topic topic8 = new Topic("topic8");
        Topic topic9 = new Topic("topic9");
        Topic topic10 = new Topic("topic10");
        topicList.addAll(Arrays.asList(topic, topic2, topic3, topic4, topic5, topic6, topic7, topic8, topic9, topic10));

        drawTopics();
        topicListScrolling();
    }

    private void drawTopics() {
        topicField.setSpacing(3);
        topicList.forEach(this::createLabel);
    }

    private void createLabel(Topic topic) {
        Label label = new Label();
        label.setMinWidth(180);
        label.setMinHeight(40);
        label.setText(topic.getTitle());
        label.setWrapText(true);
        label.getStyleClass().add("background_topic");
        label.setCursor(Cursor.HAND);
        addTextLabel(topic, label);

        label.setOnMouseEntered(event -> {
            label.getStyleClass().clear();
            label.getStyleClass().add("shine_topic");
        });

        label.setOnMouseExited(event -> {
            label.getStyleClass().clear();
            label.getStyleClass().add("background_topic");
        });

        label.setOnMouseClicked(event -> {
            fetchLetters(topic);
        });

        topicField.getChildren().add(label);
        topicLetterList.add(label);
    }

    private void fetchLetters(Topic topic) {
        //pobieranie listów z tematu
        drawLetters(topic.getLetters());
    }

    private void drawLetters(List<Letter> letters) {

    }

    private void addTextLabel(Topic topic, Label label) {
        String title;
        if (topic.getTitle().length() > 25) {
            title = topic.getTitle().substring(0, 25) + "...";
        } else {
            title = topic.getTitle();
        }
        label.setText(title + "\nZ: Rozmówca" + "\n20-10-2018");  //TODO zmień to
    }

    private void topicListScrolling() {


        if (topicField.getChildren().size() >= MAX) {

        }

        topicField.setOnScroll(event -> {
            double direction = event.getDeltaY();
            if (direction > 0) {

            } else {

            }

        });
    }

}
