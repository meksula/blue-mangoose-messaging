package com.bluemangoose.client.logic.web.socket;

import com.bluemangoose.client.controller.cache.CurrentChatCache;
import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.model.dto.ChatAccess;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

/**
* @Author
* Karol Meksuła
* 25-07-2018	
*/

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private StompSession session;
    private String roomTarget;
    private ConversationHandler conversationHandler;
    private ChatAccess access = CurrentChatCache.getInstance().getChatAccess();

    public MyStompSessionHandler(String roomTarget) {
        this.roomTarget = roomTarget;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
        session.subscribe("/topic/" + roomTarget, this);
        this.conversationHandler = ConversationHandler.getInstance(roomTarget, 100);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        ChatMessage message = (ChatMessage) payload;

        try {
            if (!message.getUsernmame().equals(SessionCache.getInstance().getProfilePreferences().getProfileUsername())) {
                conversationHandler.insertMessage(message);
            }
        } catch (Exception e) {
            return;
        }

    }

    public void postMessage(ChatMessage chatMessage) {
        //TODO add some secure logic
        this.session.send("/app/main", chatMessage);
    }

    public void changeRoomTarget(String roomTarget) {
        this.roomTarget = roomTarget;
    }

    public void disconect() {
        this.session.disconnect();
    }

    public ConversationHandler getConversationHandler() {
        return conversationHandler;
    }
}
