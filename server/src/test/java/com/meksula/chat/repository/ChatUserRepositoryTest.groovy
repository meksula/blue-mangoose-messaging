package com.meksula.chat.repository

import com.meksula.chat.domain.user.ChatUser
import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.config.MysqldConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.sql.Connection
import java.sql.DriverManager

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig

@SpringBootTest
class ChatUserRepositoryTest extends Specification {

    @Autowired
    private ChatUserRepository chatUserRepository

    private ChatUser chatUser1
    private ChatUser chatUser2

    void setup() {

        MysqldConfig config = aMysqldConfig(v5_7_10)
                .withPort(3306)
                .withUser("root", "")
                .build()

        EmbeddedMysql mysqld = anEmbeddedMysql(config)
                .addSchema("gitbucket")
                .start()

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gitbucket", "root", "")

        chatUser1 = new ChatUser()
        chatUser1.password = "i23dm23mid"
        chatUser1.email = "karol.mail@gmail.com"

        chatUser2 = new ChatUser()
        chatUser1.password = "i23dm23mid"
        chatUser1.email = "karol.mail@gmail.com"

        chatUserRepository.save(chatUser1)
        chatUserRepository.save(chatUser2)
    }

    def 'entities should be saved'() {
        expect:
        chatUserRepository.findAll().size() == 2
    }


    def cleanup() {
        chatUserRepository.deleteAll()
    }

}
