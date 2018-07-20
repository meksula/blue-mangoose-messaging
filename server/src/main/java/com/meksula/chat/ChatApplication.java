package com.meksula.chat;

import com.meksula.chat.config.SecurityCustomConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Author
 * Karol Meksuła
 * 20-07-2018
 * */

@Import({SecurityCustomConfiguration.class})
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
