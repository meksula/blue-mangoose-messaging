package com.meksula.chat.domain.registration.verification;

import java.util.Random;

/**
 * @Author
 * Karol Meksuła
 * 22-07-2018
 * */

public class CodeGenerator {

    public static String generateCode(int signs) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < signs; i++) {
            boolean direction = random.nextBoolean();

            if (direction) {
                int index = 97 + random.nextInt(25);
                stringBuilder.append((char) index);
            } else {
                int number = random.nextInt(9);
                stringBuilder.append(number);
            }

        }

        return stringBuilder.toString();
    }

}
