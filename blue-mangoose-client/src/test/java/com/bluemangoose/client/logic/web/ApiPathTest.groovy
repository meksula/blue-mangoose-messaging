package com.bluemangoose.client.logic.web

import com.bluemangoose.client.Main
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

class ApiPathTest extends Specification {

    def 'returned url path should be correct'() {
        when:
        def path = ApiPath.REGISTRATION.getPath()

        then:
        path == "http://51.38.129.50:8060/api/v1/registration"
    }

    def 'returned url should be dependent on start argument'() {
        setup:
        Main.runMode = "localhost"

        expect:
        ApiPath.REGISTRATION.getPath() == "http://localhost:8060/api/v1/registration"
    }

}
