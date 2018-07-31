package com.meksula.chat.domain.chat;

import java.util.Map;

/**
 * @Author
 * Karol Meksuła
 * 31-07-2018
 * */

public interface ResourcesCollector {
    void injectResources(Map<String, ChatWrapper> wrapperMap);

    void updateResources();

    void detectInactiveRooms();

    void detectMessagesOverflow();

    void detectMemoryCriticalState();

    void collectMessages(ChatWrapper chatWrapper);

    void detectStrategy();
}
