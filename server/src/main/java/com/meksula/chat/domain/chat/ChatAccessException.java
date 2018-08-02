package com.meksula.chat.domain.chat;

/**
 * @Author
 * Karol Meksuła
 * 02-08-2018
 * */

public class ChatAccessException extends Exception {
    private String message;

    public ChatAccessException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
