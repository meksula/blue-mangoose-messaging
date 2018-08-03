package com.bluemangoose.client.logic.web;

import com.bluemangoose.client.Main;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public enum ApiPath {
    REGISTRATION {
        @Override
        public String getPath() {
            return buildUrl("api/v1/registration");
        }
    },
    LOGIN {
        @Override
        public String getPath() {
            return buildUrl("api/v1/login");
        }
    },
    PROFILE {
        @Override
        public String getPath() {
            return buildUrl("api/v1/profile");
        }
    },
    VERIFICATION {
        private String userId;
        private String code;

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String getPath() {
            return buildUrl("api/v1/verification/" + userId + "/" + code);
        }
    },
    CHAT_ROOM_LIST {
        @Override
        public String getPath() {
            return buildUrl("api/v1/room/list");
        }
    };

    final String LOCALHOST = "http://localhost:8060/";
    final String REMOTE = "http://51.38.129.50:8060/";

    public String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract String getPath();

    public String buildUrl(final String PATH) {
        if (Main.runMode == null || Main.runMode.isEmpty() || Main.runMode.equals("remote")) {
            return REMOTE + PATH;
        }

        else if (Main.runMode.equals("localhost")) {
            return LOCALHOST + PATH;
        }

        return REMOTE + PATH;
    }

}
