package com.meksula.chat.client.logic.reader

import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

class DefaultSettingsReaderTest extends Specification {
    private DefaultSettingsManager settingReader

    def setup() {
        settingReader = new DefaultSettingsManager()
    }

    def 'loading settings from classpath test'() {
        when:
        def map = settingReader.loadSettings()

        then:
        map.size() == 2
        println(map.get("prop0"))
    }

    def 'write property to file test'() {
        when:
        settingReader.updateSettings(SettingsProperties.SETTING_ENABLED, "true")
        settingReader.updateSettings(SettingsProperties.DEFAULT_CHAT_ROOM, "404ro5om")

        then:
        def map = settingReader.loadSettings()
        map.size() == 2
    }

    def 'read custom settings file test'() {
        expect:
        def s = settingReader.readCustomSettingsFile()
        s != null
    }

}
