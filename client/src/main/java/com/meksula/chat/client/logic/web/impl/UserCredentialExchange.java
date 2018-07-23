package com.meksula.chat.client.logic.web.impl;

import com.meksula.chat.client.logic.web.ApiPath;
import com.meksula.chat.client.logic.web.ApplicationLogin;
import com.meksula.chat.client.logic.web.ApplicationRegistration;
import com.meksula.chat.client.logic.web.RegistrationForm;
import com.meksula.chat.client.logic.web.exchange.HttpServerConnector;
import com.meksula.chat.client.logic.web.exchange.HttpServerConnectorImpl;
import com.meksula.chat.client.model.personal.LoginCredential;
import com.meksula.chat.client.model.personal.User;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public class UserCredentialExchange implements ApplicationRegistration, ApplicationLogin {
    private HttpServerConnector httpServerConnector;

    @Override
    public boolean registration(RegistrationForm registrationForm, ApiPath apiPath) {
        httpServerConnector = new HttpServerConnectorImpl<>(String.class);
        String out = (String) httpServerConnector.post(registrationForm, apiPath);

        return Boolean.valueOf(out);
    }

    @Override
    public boolean login(LoginCredential credential, ApiPath apiPath) {
        httpServerConnector = new HttpServerConnectorImpl<>(String.class);
        String output = httpServerConnector.login(credential.getUsername(), credential.getPassword());

        return Boolean.valueOf(output);
    }

    @Override
    public User retrieveProfile(ApiPath apiPath) {
        httpServerConnector = new HttpServerConnectorImpl<>(User.class);
        return (User) httpServerConnector.get(apiPath);
    }

}
