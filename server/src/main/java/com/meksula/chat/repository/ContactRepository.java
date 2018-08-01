package com.meksula.chat.repository;

import com.meksula.chat.domain.user.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author
 * Karol Meksuła
 * 01-08-2018
 * */

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
