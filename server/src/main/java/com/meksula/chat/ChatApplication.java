package com.meksula.chat;

import com.meksula.chat.config.SecurityCustomConfiguration;
import com.meksula.chat.domain.chat.ResourcesCollector;
import org.springframework.beans.factory.annotation.Autowired;
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
