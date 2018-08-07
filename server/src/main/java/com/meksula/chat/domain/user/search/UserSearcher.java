package com.meksula.chat.domain.user.search;

import com.meksula.chat.domain.user.ContactFind;

import java.util.Set;

/**
 * @Author
 * Karol Meksuła
 * 06-08-2018
 * */

public interface UserSearcher {
    /**
     * Use this method if you want to find all matchig users in database.
     * */
    Set<ContactFind> findMatching(String phrase);
}
