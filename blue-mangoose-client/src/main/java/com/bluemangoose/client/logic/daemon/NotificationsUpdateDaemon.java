package com.bluemangoose.client.logic.daemon;

import com.bluemangoose.client.Main;
import com.bluemangoose.client.logic.web.ApiPath;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnector;
import com.bluemangoose.client.logic.web.exchange.HttpServerConnectorImpl;
import com.bluemangoose.client.model.personal.ContactAddNotification;
import com.bluemangoose.client.model.personal.ProfilePreferences;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @Author
 * Karol Meksuła
 * 13-08-2018
 * */

@Slf4j
public class NotificationsUpdateDaemon implements StateUpdateDaemon {
    private ProfilePreferences profilePreferences;
    private HttpServerConnector<String> httpServerConnector;
    private ObjectMapper objectMapper;

    public NotificationsUpdateDaemon(ProfilePreferences profilePreferences) {
        this.profilePreferences = profilePreferences;
        this.httpServerConnector = new HttpServerConnectorImpl<>(String.class);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void updateState(long userId) {
        Runnable fetchNotifications = () -> {
            while (Main.isRunning) {
                String responseJson = httpServerConnector.get(ApiPath.NOTIFICATION_FETCH);
                List<ContactAddNotification> contactAddNotifications = parseJsonToList(responseJson);
                profilePreferences.setNotifications(contactAddNotifications);

                /** TODO
                 * Bug here!
                 * Trzeba tutaj koniecznie rozwiązać problem z wątkami i synchronizacją 'contactAddNotifications'
                 * */

                try {
                    Thread.sleep(15000);
                    log.info("Thread is invoke: fetching notifications...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread notifThread = new Thread(fetchNotifications);
        notifThread.start();
    }

    @Override
    public void updateStateOnce() {
        String responseJson = httpServerConnector.get(ApiPath.NOTIFICATION_FETCH);
        List<ContactAddNotification> contactAddNotifications = parseJsonToList(responseJson);
        profilePreferences.setNotifications(contactAddNotifications);
    }

    private List<ContactAddNotification> parseJsonToList(final String JSON) {
        try {
            return this.objectMapper.readValue(JSON, new TypeReference<List<ContactAddNotification>>() {});
        } catch (IOException e) {
            return null;
        }
    }

}
