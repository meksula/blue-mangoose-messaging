package com.bluemangoose.client.logic.daemon;

import com.bluemangoose.client.Main;
import com.bluemangoose.client.controller.SceneRefresh;
import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnector;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author
 * Karol Meksuła
 * 13-08-2018
 * */

public class ChatUserStatusUpdateDaemon implements StateUpdateDaemon {
    private SceneRefresh sceneRefresh;
    private HttpServerConnector<String> serverConnector;

    public ChatUserStatusUpdateDaemon(SceneRefresh sceneRefresh) {
        this.sceneRefresh = sceneRefresh;
        this.serverConnector = new HttpServerConnectorImpl<>(String.class);
    }

    @Override
    public void updateState(long userId) {
        Timer timer = new Timer();

        TimerTask refreshTask = new TimerTask() {
            @Override
            public void run() {
                if (!Main.isRunning) {
                    timer.cancel();
                    timer.purge();
                }

                String json = serverConnector.get(ApiPath.FRIENDS_ONLINE_STATUS);
                updateSessionCacheState(json);
                sceneRefresh.refresh();
            }
        };

        timer.schedule(refreshTask, 0, 12000);
    }

    private Map<String, Boolean> stringParse(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, Boolean>>() {});
        } catch (IOException e) {
            return null;
        }
    }

    private void updateSessionCacheState(String json) {
        SessionCache.getInstance().updateContactStatus(stringParse(json));
    }

    @Override
    public void updateStateOnce() {

    }

}
