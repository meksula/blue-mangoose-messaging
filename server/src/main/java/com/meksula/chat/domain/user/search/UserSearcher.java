package com.meksula.chat.domain.user.search;

import java.util.Map;
import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 06-08-2018
 * */

public interface UserSearcher {
    /**
     * Use this method if you want to find all matchig users in database.
     * This return Set of Maps. Map contains user's id (Long) as key,
     * and username (String) as value.
     * */
    Set<Map<Long,String>> findMatching(String phrase);
}
