package com.bluemangoose.client.logic.web.exchange;

import com.bluemangoose.client.logic.web.ApiPath;

import java.io.File;

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

    T putFile(ApiPath apiPath, File picture);
}
