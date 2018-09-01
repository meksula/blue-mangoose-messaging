package com.meksula.chat.repository;

import com.meksula.chat.domain.mailbox.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author
 * Karol Meksuła
 * 01-09-2018
 * */

public interface TopicRepository extends JpaRepository<Topic, String> {
}
