package com.meksula.chat.repository;

import com.meksula.chat.domain.room.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author
 * Karol Meksuła
 * 26-07-2018
 * */

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
