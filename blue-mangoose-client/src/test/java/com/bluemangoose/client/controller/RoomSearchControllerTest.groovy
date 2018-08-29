package com.bluemangoose.client.controller

import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 29-08-2018
 * */

class RoomSearchControllerTest extends Specification {
    RoomSearchController roomSearchController = new RoomSearchController()

    def 'rooms amount one the one page test'() {
        given: 'in case 4 rooms only exist'
        def rooms = 4
        def rooms2 = 19

        expect:
        int res = (int) ((rooms / roomSearchController.ROOM_AMOUNT) + 1)
        res == 1

        int res2 = (int) ((rooms2 / roomSearchController.ROOM_AMOUNT) + 1)
        res2 == 2
    }

    def "room dividing at pages"() {
        given:
        def from = 16
        def to = 15
        def rooms = 83

        expect:
        def strTab = function(from, to, rooms)
        for(String s : strTab) {
            println(s)
        }
    }

    def function(int from, int to, int rooms) {
        String[] rage = new String[((rooms / roomSearchController.ROOM_AMOUNT) + 1)]

        for(int i = 0; i < rage.length; i++) {
            int fr = from * i
            int t = to + ((to * i) + i)
            int diff = t - fr
            def str = "Rage no." + i + ": FROM: " + fr + " TO: " + t + " DIFF: " + diff
            rage[i] = str
        }

        return rage
    }

}
