package com.bluemangoose.client.logic.daemon;

import com.bluemangoose.client.controller.SceneRefresh;
import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnector;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author
 * Karol Meksuła
 * 20-09-2018
 * */

@Slf4j
public class LetterStatusDaemon implements StateUpdateDaemon {
    private SceneRefresh sceneRefresh;
    private AtomicBoolean mailReceived = new AtomicBoolean(false);
    private HttpServerConnector<String> httpServerConnector;

    public LetterStatusDaemon(SceneRefresh sceneRefresh) {
        this.sceneRefresh = sceneRefresh;
        this.httpServerConnector = new HttpServerConnectorImpl<>(String.class);
    }

    @Override
    public void updateState(long userId) {
        Runnable runnable = () -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean old = mailReceived.get();
                boolean freshLetters = isNewLetters();
                this.mailReceived.set(freshLetters);
                SessionCache.getInstance().setLettersUnsealed(freshLetters);
                stateChange(old);
            }
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void updateStateOnce() {

    }

    private boolean isNewLetters() {
        String isNewLetterString = httpServerConnector.get(ApiPath.IS_NEW_LETTER);
        log.debug(isNewLetterString);
        boolean isNewLetter = Boolean.parseBoolean(isNewLetterString);
        SessionCache.getInstance().setLettersUnsealed(isNewLetter);
        log.debug(String.valueOf(SessionCache.getInstance().getLettersUnsealed().get()));
        return isNewLetter;
    }

    private void stateChange(boolean oldState) {
        if (SessionCache.getInstance().getLettersUnsealed().get() == oldState) {
            return;
        }

        sceneRefresh.refresh();
    }

}
