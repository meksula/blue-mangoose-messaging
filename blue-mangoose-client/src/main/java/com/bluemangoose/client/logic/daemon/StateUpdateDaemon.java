package com.bluemangoose.client.logic.daemon;

/**
 * @Author
 * Karol Meksuła
 * 13-08-2018
 * */

public interface StateUpdateDaemon {
    void updateState(long userId);

    void updateStateOnce();
}
