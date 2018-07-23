package com.meksula.chat.client.logic.web.exchange;

import com.meksula.chat.client.logic.web.ApiPath;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public interface HttpServerConnector<T> {
    String login(String username, String password);

    T post(Object entity, ApiPath apiPath);

    T put();

    T get(ApiPath apiPath);
}
