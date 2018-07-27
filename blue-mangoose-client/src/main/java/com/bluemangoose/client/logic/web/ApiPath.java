package com.bluemangoose.client.logic.web;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public enum ApiPath {
    REGISTRATION {
        @Override
        public String getPath() {
            return "http://51.38.129.50:8060/api/v1/registration";
        }
    },
    LOGIN {
        @Override
        public String getPath() {
            return "http://51.38.129.50:8060/api/v1/login";
        }
    },
    PROFILE {
        @Override
        public String getPath() {
            return "http://51.38.129.50:8060/api/v1/profile/" + username;
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
            return "http://51.38.129.50:8060//api/v1/verification/" + userId + "/" + code;
        }
    },
    CHAT_ROOM_LIST {
        @Override
        public String getPath() {
            return "http://51.38.129.50:8060/api/v1/room/list";
        }
    };

    public String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract String getPath();

}
