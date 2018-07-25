package com.bluemangoose.client.model.personal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginCredential {
    private String username;
    private String password;

    public LoginCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
