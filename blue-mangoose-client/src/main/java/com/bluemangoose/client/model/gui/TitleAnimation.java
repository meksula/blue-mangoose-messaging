package com.bluemangoose.client.model.gui;

import javafx.scene.control.Label;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author
 * Karol Meksuła
 * 26-08-2018
 * */

public class TitleAnimation {
    private AtomicBoolean run = new AtomicBoolean(true);
    private AtomicInteger counter = new AtomicInteger(0);
    private Label[] letters;

    public TitleAnimation(Label[] letters) {
        this.letters = letters;
    }

    public void animation() {
        Runnable animation = () -> {
            initState();

            while (run.get()) {
                letterAction();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                counter.incrementAndGet();
            }

        };

        Thread animationThread = new Thread(animation);
        animationThread.setDaemon(true);
        animationThread.start();
    }

    private void letterAction() {
        int index = counter.intValue();
        double initX = index * 50;

        Label current = null;
        if (index < letters.length) {
            current = letters[index];
        }

        if (counter.get() == 12) {
            shineLetters();
        }

        while (current != null && current.getLayoutY() < 100) {
            if (current.getStyleClass().size() == 0) {
                current.getStyleClass().add("anim_label");
            } else {
                current.getStyleClass().clear();
            }

            double y = current.getLayoutY();

            if (current.getText().equals("l") || current.getText().equals("B")) {
                initX = initX + 1;
            }

            if (current.getText().equals("a")) {
                initX = initX + 0.6;
            }

            current.setLayoutX(initX);
            current.setLayoutY(y + 10);

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void shineLetters() {
        int counter = 0;
        final String STANDARD = "anim_label";
        final String SHINE = "anim_label_shine";

        while (counter < 10) {
            for (Label letter : letters) {
                try {
                    if (letter.getStyleClass().size() == 0) {
                        letter.getStyleClass().clear();
                        letter.getStyleClass().add(STANDARD);
                    } else {
                        letter.getStyleClass().clear();
                        if (counter % 2 == 0) {
                            letter.getStyleClass().add(SHINE);
                        } else {
                            letter.getStyleClass().add(STANDARD);
                        }
                    }

                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            counter ++;
        }
    }

    public void stop() {
        run.set(false);
    }

    private void initState() {
        for (Label letter : letters) {
            letter.setVisible(true);
            letter.setLayoutX(-50);
            letter.setLayoutY(-50);
        }
    }

}
