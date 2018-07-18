package com.meksula.chat.client.controller;

import javafx.event.Event;

/**
 * @Author
 * Karol Meksuła
 * 18-07-2018
 * */

public interface FxmlLoader {
    void loadNewStage(final String PATH);

    void loadSameStage(final String PATH, final Event event);

    void loadNewStageWithData(final FxmlLoaderTemplate.SceneType sceneType, final Object DATA);

    void loadSameStageWithData(final FxmlLoaderTemplate.SceneType sceneType, final Object DATA, final Event event);
}
