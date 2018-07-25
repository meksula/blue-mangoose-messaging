package com.bluemangoose.client.logic.web;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public class RegistrationForm {
    private String username;
    private String email;
    private String password;
    private String passwordConfirmation;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
