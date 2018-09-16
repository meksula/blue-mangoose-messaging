package com.bluemangoose.client.controller;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.controller.loader.FxmlLoaderTemplate;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.logic.web.mailbox.MailboxLetterExchange;
import com.bluemangoose.client.logic.web.mailbox.MailboxLetterExchangeImpl;
import com.bluemangoose.client.logic.web.mailbox.MailboxTemporaryCache;
import com.bluemangoose.client.logic.web.mailbox.TopicShortInfo;
import com.bluemangoose.client.model.alert.Alerts;
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
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author
 * Karol Meksuła
 * 30-08-2018
 * */

@Slf4j
public class MailboxController implements Initializable {
    private List<TopicShortInfo> topicList = new ArrayList<>();
    private List<Label> topicLetterList = new ArrayList<>();
    private List<Label> lettersLabelBufor = new ArrayList<>();
    private final int MAX = 8;
    private final int MAX_LETTERS = 5;
    private AtomicInteger topicCounter = new AtomicInteger();
    private AtomicInteger letterCounter = new AtomicInteger();
    private MailboxLetterExchange mailboxLetterExchange = new MailboxLetterExchangeImpl();
    private TopicShortInfo current;

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
        try {
            topicList = new MailboxLetterExchangeImpl().getTopicsShortInfo();
        } catch (IOException e) {
            log.debug("Cannot receive shorten list of Topics. " + e.getCause());
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
        newTopic.setOnMouseClicked(event -> new FxmlLoaderTemplate().loadNewStage("/templates/new_topic.fxml"));
    }

    private void removeTopicAction() {
        Image inactive = removeTopic.getImage();
        Image active = new Image("/img/remove-topic-active.png");

        removeTopic.setOnMouseEntered(event -> removeTopic.setImage(active));
        removeTopic.setOnMouseExited(event -> removeTopic.setImage(inactive));
        removeTopic.setOnMouseClicked(event -> {
            if (current == null) {
                new Alerts().other("Usuwanie tematu", "Kliknij najpierw na odpowiedni temat, który chcesz usunąć.", null);
                return;
            }

            ApiPath path = ApiPath.DELETE_TOPIC_ONE_SIDE;
            path.topicId = current.getTopicId();
            new HttpServerConnectorImpl<>(String.class).delete(path);
            new Alerts().other("Usunięto", "Usunąłeś swoją konwersację.", null);
        });
    }

    private void responseTopicAction() {
        Image inactive = responseTopic.getImage();
        Image active = new Image("/img/response-topic-active.png");

        responseTopic.setOnMouseEntered(event -> responseTopic.setImage(active));
        responseTopic.setOnMouseExited(event -> responseTopic.setImage(inactive));
        responseTopic.setOnMouseClicked(event -> {
            if (current == null) {
                new Alerts().error("Błąd", "Wybierz temat.", "Nie wybrałeś żadnego tematu, na który" +
                        " chcesz odpowiedzieć.");
                return;
            }

            MailboxTemporaryCache.setCurrentTopic(current.getTopicId());
            MailboxTemporaryCache.setCurrentMailboxController(this);
            new FxmlLoaderTemplate()
                    .loadNewStageWithData(FxmlLoaderTemplate.SceneType.LETTER_EDITOR, assgignSeccondSide(current));
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
            try {
                Label topicLabel = topicLetterList.get(i);
                topicField.getChildren().add(topicLabel);
            } catch (IndexOutOfBoundsException exception) {
                break;
            }
        }
    }

    private void drawTopics() {
        topicField.setSpacing(5);
        topicList.forEach(this::createLabel);
    }

    private void createLabel(TopicShortInfo topic) {
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
            this.current = topic;
            fetchLetters(topic);
        });
        topicLetterList.add(label);
    }

    private void fetchLetters(TopicShortInfo topicShortInfo) {
        Topic topic = mailboxLetterExchange.fetchTopic(topicShortInfo.getTopicId());

        topic.getLetters().forEach(letter -> {
            if (letter.getSendTimestamp() != null) {
                LocalDateTime dateTime = LocalDateTime.parse(letter.getSendTimestamp());
                letter.setParsedTimestamp(dateTime);
            }
        });

        sortLetters(topic.getLetters());
        createLettersLabel(topic.getLetters());
        drawLetters(0, MAX_LETTERS);
    }

    private void sortLetters(List<Letter> letters) {
        letters.sort((letter1, letter2) -> {
            if (letter1.getParsedTimestamp().isAfter(letter2.getParsedTimestamp())) {
                return 1;
            }
            else if (letter1.getParsedTimestamp().isBefore(letter2.getParsedTimestamp())) {
                return -1;
            }
            return 0;
        });
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
        labelStyles(label);

        String date = dateDisplayFormat(letter);
        label.setText("Napisał: " + letter.getSenderUsername() + ", " + date
                + "\n\n" + letter.getContent());
        return label;
    }

    private String dateDisplayFormat(Letter letter) {
        if (letter.getSendTimestamp() == null) {
            return  "Przed chwilą";
        } else {
            LocalDateTime dateTime = LocalDateTime.parse(letter.getSendTimestamp());
            if (dateTime.getDayOfMonth() == LocalDateTime.now().getDayOfMonth()
                    && dateTime.getMonthOfYear() == LocalDateTime.now().getMonthOfYear()) {
                return  "Dziś o " + dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour()
                        + ":" + dateTime.getSecondOfMinute();
            } else {
                return  "data: " + dateTime.toString();
            }
        }
    }

    private void addTextLabel(TopicShortInfo topic, Label label) {
        String title;
        if (topic.getTitle().length() > 25) {
            title = topic.getTitle().substring(0, 25) + "...";
        } else {
            title = topic.getTitle();
        }

        String secondSide = assgignSeccondSide(topic);

        label.setText(title + "\nZ: " + secondSide + "\n" + topic.getInitDate());
    }

    private String assgignSeccondSide(TopicShortInfo topic) {
        if (topic.getUsernameA().equals(SessionCache.getInstance().getProfilePreferences().getProfileUsername())) {
            return topic.getUsernameB();
        } else {
            return topic.getUsernameA();
        }
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

    private void labelStyles(Label label) {
        label.setOnMouseEntered(event -> {
            label.getStyleClass().clear();
            label.getStyleClass().add("letter_shine");
        });
        label.setOnMouseExited(event -> {
            label.getStyleClass().clear();
            label.getStyleClass().add("letter");
        });
    }

    void drawNewestLetter(Letter letter) {
        Label lastLetter = letterLabelNew(letter);
        lettersField.getChildren().add(lastLetter);
    }

}
