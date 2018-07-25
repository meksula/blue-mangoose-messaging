package com.bluemangoose.client.logic.web;

import com.bluemangoose.client.model.personal.LoginCredential;
import com.bluemangoose.client.model.personal.User;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public interface ApplicationLogin {
    boolean login(LoginCredential credential, ApiPath apiPath);

    User retrieveProfile(ApiPath apiPath);
}
