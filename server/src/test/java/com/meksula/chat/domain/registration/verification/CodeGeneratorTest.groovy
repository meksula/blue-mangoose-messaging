package com.meksula.chat.domain.registration.verification

import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 22-07-2018
 * */

class CodeGeneratorTest extends Specification {

    def 'CodeGenerator should return correct String'() {
        setup:
        def code = CodeGenerator.generateCode(10)

        expect:
        code.length() == 10
        print(code)
    }

}
