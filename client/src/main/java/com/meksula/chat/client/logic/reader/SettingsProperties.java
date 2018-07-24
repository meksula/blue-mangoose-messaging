package com.meksula.chat.client.logic.reader;

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

public enum SettingsProperties {

    SETTING_ENABLED {
        @Override
        public String property() {
            return "setting_enabled=";
        }

        @Override
        public int index() {
            return 3;
        }
    },

    DEFAULT_CHAT_ROOM {
        @Override
        public String property() {
            return "default_chat_room=";
        }

        @Override
        public int index() {
            return 4;
        }
    },

    SAVED_USER {
        @Override
        public String property() {
            return "saved_user=";
        }

        @Override
        public int index() {
            return 6;
        }
    },

    PASSWORD {
        @Override
        public String property() {
            return "password=";
        }

        @Override
        public int index() {
            return 7;
        }
    };

    public abstract String property();

    public abstract int index();
}
