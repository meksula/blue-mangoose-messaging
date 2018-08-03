package com.meksula.chat.domain.files

import com.meksula.chat.domain.user.ApplicationUser
import com.meksula.chat.domain.user.ChatUser
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

/**
 * @Author
 * Karol Meksuła
 * 03-08-2018
 * */

class DefaultFileExchangeTest extends Specification {
    private FileExchange fileExchange
    private String username = "admin"

    void setup() {
        fileExchange = new DefaultFileExchange()
    }

    def 'file save and read test'() {
        setup:
        ApplicationUser applicationUser = new ChatUser()
        applicationUser.setUsername(username)
        MultipartFile multipartFile = new MockMultipartFile(applicationUser.getUsername(), new byte[100])

        expect:
        fileExchange.uploadPicture(multipartFile, applicationUser)

        byte[] bytes = fileExchange.getPicture(username)
        bytes.length == 100
    }

    void cleanup() {
        File file = new File(fileExchange.getPicturePath() + username)
        file.delete()
    }

}
