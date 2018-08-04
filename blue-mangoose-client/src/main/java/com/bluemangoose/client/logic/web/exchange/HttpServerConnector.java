package com.bluemangoose.client.logic.web.exchange;

import com.bluemangoose.client.logic.web.ApiPath;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;

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

    Image getImage(ApiPath avatarGet) throws IOException;
}
