package com.meksula.chat.repository;

import com.meksula.chat.domain.user.social.ContactAddNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * @Author
 * Karol Meksuła
 * 08-08-2018
 * */

@Transactional
public interface ContactAddNotificationRepository extends JpaRepository<ContactAddNotification, Long> {
}
