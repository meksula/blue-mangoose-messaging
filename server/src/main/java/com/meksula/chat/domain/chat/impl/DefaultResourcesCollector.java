package com.meksula.chat.domain.chat.impl;

import com.meksula.chat.domain.chat.ChatRoomManager;
import com.meksula.chat.domain.chat.ChatWrapper;
import com.meksula.chat.domain.chat.ResourcesCollector;
import com.meksula.chat.domain.chat.dto.ChatMessage;
import com.meksula.chat.domain.chat.dto.Message;
import com.meksula.chat.repository.ChatMessagesRepository;
import com.meksula.chat.repository.ChatRoomRepository;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author
 * Karol Meksuła
 * 31-07-2018
 * */

@Service
public class DefaultResourcesCollector implements ResourcesCollector {
    private ChatRoomManager chatRoomManager;
    private ChatRoomRepository chatRoomRepository;
    private ChatMessagesRepository chatMessagesRepository;
    private Map<String, ChatWrapper> wrapperMap;
    private List<LocalDateTime> collectorLaunches;
    long collectorDelay = 600000;
    long collectorPeriod = 600000;
    private boolean memoryCriticalState;

    @Value("${dead.room}")
    int deadMinutes;

    @Value("${messages.limit}")
    int messagesLimit;

    public DefaultResourcesCollector() {
        this.collectorLaunches = new ArrayList<>();
        detectStrategy();
    }

    @Autowired
    public void setChatRoomManager(ChatRoomManager chatRoomManager) {
        this.chatRoomManager = chatRoomManager;
    }

    @Autowired
    public void setChatRoomRepository(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Autowired
    public void setChatMessagesRepository(ChatMessagesRepository chatMessagesRepository) {
        this.chatMessagesRepository = chatMessagesRepository;
    }

    @Override
    public void injectResources(Map<String, ChatWrapper> wrapperMap) {
        this.wrapperMap = wrapperMap;
    }

    @Override
    public void updateResources() {
        if (chatRoomManager == null) {
            return;
        }

        this.wrapperMap = chatRoomManager.getChatMap();
        Runtime.getRuntime().gc();
    }

    public List<LocalDateTime> getCollectorLaunches() {
        return collectorLaunches;
    }

    /**
     * This method detects that ChatRoom is dead.
     * The criteria are loaded from application.properties.
     * By default minutes to term a ChatRoom as dead are setted at 180 minutes.
     * After this time ChatRoom will be archived and disabled.
     * */

    @Override
    public void detectInactiveRooms() {
        Collection<ChatWrapper> wrappers = wrapperMap.values();

        List<ChatWrapper> inactives = wrappers.stream()
                .filter(wrapper -> Minutes.minutesBetween(LocalDateTime.now(), wrapper.getInitTimestamp()).getMinutes() >= deadMinutes)
                .collect(Collectors.toList());

        inactives.forEach(this::smash);
    }

    private void smash(ChatWrapper chatWrapper) {
        ChatWrapper disabled = wrapperMap.remove(chatWrapper.getChatRoom().getName());
        chatRoomRepository.save(disabled.getChatRoom());
    }

    /**
     * detectMessagesOverflow() should check is wrapper has exceeded a limit of messages in Collection.
     * messages.limit property in application.properties define how much messages can be storage in cache memory
     * After exceed of limit bellow method should trigger collector and save excess of messages to database.
     * */

    @Override
    public void detectMessagesOverflow() {
        List<ChatWrapper> overlflowed = wrapperMap.values()
                .stream()
                .filter(chatWrapper -> chatWrapper.getAllMessages().size() > messagesLimit)
                .collect(Collectors.toList());

        overlflowed.forEach(this::collectMessages);
    }

    @Override
    public void collectMessages(ChatWrapper chatWrapper) {
        List<Message> messages = chatWrapper.getAllMessages();
        int end = messages.size();
        int begin = end - messagesLimit;

        List<Message> shortenedList = messages.subList(begin, end);
        chatWrapper.setAllMessages(shortenedList);

        List<Message> oldMessages = messages.subList(0, begin);
        persistOldMessages(oldMessages);
    }

    private void persistOldMessages(List<Message> messages) {
        List<ChatMessage> toPersist = new ArrayList<>();

        for (Message message : messages) {
            toPersist.add((ChatMessage) message);
        }

        chatMessagesRepository.saveAll(toPersist);
    }

    @Override
    public void detectMemoryCriticalState() {
        long maxMemory = Runtime.getRuntime().maxMemory();

        if (maxMemory < 1434483712) {
            memoryCriticalState = true;
        }

    }

    /**
     * Defines when ResourcesCollector and Garbage Collector should activate
     * By default collector run 10 minutes (60000 milliseconds) after application's start and a period between collector's work.
     * */

    @Override
    public void detectStrategy() {
        TimerTask stateUpdate = new TimerTask() {
            @Override
            public void run() {
                if (memoryCriticalState) {
                    detectInactiveRooms();
                    detectMessagesOverflow();
                }

                Runtime.getRuntime().gc();
                memoryCriticalState = false;
                collectorLaunches.add(LocalDateTime.now());
            }
        };

        Timer timer = new Timer();
        timer.schedule(stateUpdate, collectorDelay, collectorPeriod);
    }

}
