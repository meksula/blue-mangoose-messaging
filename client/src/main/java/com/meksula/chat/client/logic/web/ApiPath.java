package com.meksula.chat.client.logic.web;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public enum ApiPath {
    REGISTRATION {
        @Override
        public String getPath() {
            return "http://localhost:8060/api/v1/registration";
        }
    },
    LOGIN {
        @Override
        public String getPath() {
            return "http://localhost:8060/login";
        }
    },
    PROFILE {
        @Override
        public String getPath() {
            return "http://localhost:8060/api/v1/profile/" + username;
        }
    };

    public String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract String getPath();
}
