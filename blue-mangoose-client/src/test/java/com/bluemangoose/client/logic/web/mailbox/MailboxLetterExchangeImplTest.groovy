package com.bluemangoose.client.logic.web.mailbox

import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 12-09-2018
 * */

class MailboxLetterExchangeImplTest extends Specification {
    private MailboxLetterExchangeImpl mailboxLetterExchange = new MailboxLetterExchangeImpl()

    def "topic shorten info fetch test"() {
        expect:
        mailboxLetterExchange.getTopicsShortInfo()
    }

}
