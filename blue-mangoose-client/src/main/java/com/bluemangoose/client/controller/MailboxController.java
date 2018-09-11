package com.bluemangoose.client.controller;

import com.bluemangoose.client.model.dto.Letter;
import com.bluemangoose.client.model.dto.Topic;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

@Slf4j
public class MailboxController implements Initializable {
    private List<Topic> topicList = new ArrayList<>();
    private List<Label> topicLetterList = new ArrayList<>();
    private List<Label> lettersLabelBufor = new ArrayList<>();
    private final int MAX = 8;
    private final int MAX_LETTERS = 3;
    private AtomicInteger topicCounter = new AtomicInteger();
    private AtomicInteger letterCounter = new AtomicInteger();

    @FXML
    private VBox topicField;

    @FXML
    private VBox lettersField;

    @FXML
    private Label topicAmount;

    @FXML
    private ImageView responseTopic, removeTopic, newTopic;

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
        Topic topic11 = new Topic("topic11");
        topicList.addAll(Arrays.asList(topic, topic2, topic3, topic4, topic5, topic6, topic7, topic8, topic9, topic10, topic11));

        for (int i = 0; i < 4; i++) {
            topic2.addLetter(new Letter("Letter " + i + "|||" + "This is where our \"deep study\" of machine learning begins. We introduce some of the core building blocks and concepts that we will use throughout the remainder of this course: input space, action space, outcome space, prediction functions, loss functions, and hypothesis spaces. We present our first machine learning method: empirical risk minimization. We also highlight the issue of overfitting, which may occur when we find the empirical risk minimizer over too large a hypothesis space."));
        }

        for (int i = 0; i < 4; i++) {
            topic4.addLetter(new Letter("Letter " + i+100 + "|||" + "This is where our \"deep study\" of machine learning begins. We introduce some of the core building blocks and concepts that we will use throughout the remainder of this course: input space, action space, outcome space, prediction functions, loss functions, and hypothesis spaces. We present our first machine learning method: empirical risk minimization. We also highlight the issue of overfitting, which may occur when we find the empirical risk minimizer over too large a hypothesis space."));
        }

        lettersField.setSpacing(8);
        responseTopicAction();
        removeTopicAction();
        newTopicAction();
        drawTopics();
        displayMaxTopic();
        topicListScrolling();
        lettersFieldScrolling();
        topicAmount.setText(String.valueOf(topicLetterList.size()));
    }

    private void newTopicAction() {
        Image inactive = newTopic.getImage();
        Image active = new Image("/img/new-topic-active.png");

        newTopic.setOnMouseEntered(event -> newTopic.setImage(active));
        newTopic.setOnMouseExited(event -> newTopic.setImage(inactive));
        newTopic.setOnMouseClicked(event -> {

        });
    }

    private void removeTopicAction() {
        Image inactive = removeTopic.getImage();
        Image active = new Image("/img/remove-topic-active.png");

        removeTopic.setOnMouseEntered(event -> removeTopic.setImage(active));
        removeTopic.setOnMouseExited(event -> removeTopic.setImage(inactive));
        removeTopic.setOnMouseClicked(event -> {

        });
    }

    private void responseTopicAction() {
        Image inactive = responseTopic.getImage();
        Image active = new Image("/img/response-topic-active.png");

        responseTopic.setOnMouseEntered(event -> responseTopic.setImage(active));
        responseTopic.setOnMouseExited(event -> responseTopic.setImage(inactive));
        responseTopic.setOnMouseClicked(event -> {

        });
    }

    private void lettersFieldScrolling() {
        lettersField.setOnScroll(event -> {
            double direction = event.getDeltaY();
            if (direction > 0 && letterCounter.get() > 0) {
                letterCounter.decrementAndGet();
            } else {
                if (letterCounter.get() < lettersLabelBufor.size() - 1)
                    letterCounter.incrementAndGet();
            }
            log.debug(String.valueOf(letterCounter.get()));
            drawLetters(letterCounter.get(), MAX_LETTERS + letterCounter.get());
        });
    }

    private void displayMaxTopic() {
        for (int i = 0; i < MAX; i++) {
            Label topicLabel = topicLetterList.get(i);
            topicField.getChildren().add(topicLabel);
        }
    }

    private void drawTopics() {
        topicField.setSpacing(5);
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

        label.setOnMouseClicked(event -> fetchLetters(topic));
        topicLetterList.add(label);
    }

    private void fetchLetters(Topic topic) {
        //TODO pobieranie listów z tematu

        createLettersLabel(topic.getLetters());
        drawLetters(0, MAX_LETTERS);
    }

    private void drawLetters(int from, int to) {
        lettersField.getChildren().clear();

        if (lettersLabelBufor.size() == 0) {
            lettersLabelBufor.clear();

        } else {
            for (int i = from; i < to; i++) {
                try {
                    lettersField.getChildren().add(lettersLabelBufor.get(i));
                } catch (IndexOutOfBoundsException exception) {
                    break;
                }
            }
        }
    }

    private void createLettersLabel(List<Letter> letters) {
        lettersLabelBufor.clear();

        for (Letter letter : letters) {
            lettersLabelBufor.add(letterLabelNew(letter));
        }
    }

    private Label letterLabelNew(Letter letter) {
        Label label = new Label();
        label.getStyleClass().add("letter");
        label.setMinWidth(490);
        label.setWrapText(true);
        label.setText("Napisał: " + letter.getAuthorUsername() + ", data: " + letter.getDate() + "\n\n" + letter.getContent());

        return label;
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
        topicField.setOnScroll(event -> {
            double direction = event.getDeltaY();
            if (direction > 0 && topicCounter.get() > 0) {
                topicCounter.getAndDecrement();
            } else {
                if (topicCounter.get() < topicLetterList.size() - 1) {
                    topicCounter.getAndIncrement();
                }
            }
            redrawTopics(topicCounter.get(), MAX + topicCounter.get());
        });
    }

    private void redrawTopics(int from, int to) {
        if (from >= 0) {
            topicField.getChildren().clear();

            for (int i = from; i < to; i++) {
                try {
                    topicField.getChildren().add(topicLetterList.get(i));
                } catch (IndexOutOfBoundsException ioobe) {
                    break;
                }
            }
        }
    }

}
