package com.meksula.chat.domain.mailbox

import com.meksula.chat.repository.LetterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * @author
 * Karol Meksuła
 * 31-08-2018
 * */

@SpringBootTest
class BasicInternalMailTest extends Specification {
    final String SENDER_USERNAME = "gargamel99"
    final String ADDRESSEE_USERNAME = "papa_smerf"
    final long SENDER_ID = 33440
    final long ADDRESSEE_ID = 3049292
    String letterId

    @Autowired
    private LetterRepository letterRepository

    @Autowired
    private InternalMail internalMail

    def "method should load all user`s letters"() {
        setup:
        final int AMOUNT = 5
        saveLetters(AMOUNT)

        expect:
        def list = internalMail.getAllLettersInMailboxByUsername(ADDRESSEE_USERNAME)
        list.size() == 5

        for (Letter letter : list) {
            println(letter.toString())
        }
    }

    def saveLetters(final int amount) {
        for (int i = 0; i < amount; i++) {
            def letter = new Letter.LetterBuilder()
                    .senderId(SENDER_ID)
                    .senderUsername(SENDER_USERNAME)
                    .addresseeId(ADDRESSEE_ID)
                    .addresseeUsername(ADDRESSEE_USERNAME)
                    .unsealed(false) // not read
                    .title("Zniszczę waszą wioskę! " + i)
                    .content("Nadchodzi wasz kres, wy przeklęte smerfy!")
                    .build()
            println(letter.getId())

            if (i == 3) {
                this.letterId = letter.getId()
            }

            letterRepository.save(letter)
        }
    }

    def "method should load all sent messages"() {
        setup:
        final int AMOUNT = 6
        saveLetters(AMOUNT)

        expect:
        def list = internalMail.getAllSentMessages(SENDER_USERNAME)
        list.size() == AMOUNT

        for (Letter letter : list) {
            println(letter.toString())
        }
    }

    def "method should load last N messages"() {
        setup:
        final int AMOUNT = 10
        saveLetters(AMOUNT)

        when:
        def list = internalMail.getLastNReceivedLetters(ADDRESSEE_USERNAME, 3)

        then:
        list.size() == 3

        list.get(0).getTitle() == "Zniszczę waszą wioskę! 7"
        list.get(1).getTitle() == "Zniszczę waszą wioskę! 8"
        list.get(2).getTitle() == "Zniszczę waszą wioskę! 9"

        for (Letter letter : list) {
            println(letter.toString())
        }
    }

    def "method should load last 1 message / border case"() {
        setup:
        final int AMOUNT = 10
        saveLetters(AMOUNT)

        expect:
        def list = internalMail.getLastNReceivedLetters(ADDRESSEE_USERNAME, 1)
        list.size() == 1
        list.get(0).getTitle() == "Zniszczę waszą wioskę! 9"
    }

    def "method should return concrete letter by id"() {
        setup:
        final int AMOUNT = 10
        saveLetters(AMOUNT)

        expect:
        def letter = internalMail.getConcreteLetter(this.letterId)
        letter.getTitle() == "Zniszczę waszą wioskę! 3"
    }

    def "are new letters test"() {
        setup:
        final int AMOUNT = 10
        saveLetters(AMOUNT)

        expect:
        internalMail.isNewLetters(ADDRESSEE_USERNAME)
    }

    def "there is no new letters"() {
        setup:
        final int AMOUNT = 10
        saveLetters(AMOUNT)

        expect:
        def list = letterRepository.getAllByAddresseeUsername(ADDRESSEE_USERNAME)
        for (Letter letter : list) {
            letter.setUnsealed(true) //set letter as read
        }
        letterRepository.saveAll(list)
        !internalMail.isNewLetters(ADDRESSEE_USERNAME)
    }

    def cleanup() {
        letterRepository.deleteAll()
    }

}
