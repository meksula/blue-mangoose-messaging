package com.bluemangoose.client.logic.web;

import com.bluemangoose.client.Main;
import com.bluemangoose.client.controller.cache.SessionCache;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public enum ApiPath {
    SOCKET {
        @Override
        public String getPath() {
            if (Main.runMode.equals("remote")) {
                return "ws://51.38.129.50:8060/chat";
            }
            else {
                return "ws://localhost:8060/chat";
            }
        }
    },
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
    USER_HANDLE {
        @Override
        public String getPath() {
            return buildUrl("api/v1/user");
        }
    },
    PROFILE {
        @Override
        public String getPath() {
            return buildUrl("api/v1/profile");
        }
    },
    AVATAR {
        @Override
        public String getPath() {
            return buildUrl("api/v1/profile/avatar");
        }
    },
    AVATAR_GET {
        @Override
        public String getPath() {
            return buildUrl("api/v1/profile/avatar/bytes/" + SessionCache.getInstance().getProfilePreferences().getProfileUsername());
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
    },
    CHAT_ROOM_CREATE {
        @Override
        public String getPath() {
            return buildUrl("api/v1/room");
        }
    },
    MESSAGES_LAST {
        @Override
        public String getPath() {
            return buildUrl("api/v1/room/messages/1");
        }
    },
    CHAT_USERS_SEARCH {
        @Override
        public String getPath() {
            return buildUrl("api/v1/social/contact");
        }
    },
    CHAT_USER_INVITATION {
        @Override
        public String getPath() {
            return buildUrl("api/v1/social/invitation");
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
