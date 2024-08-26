package com.shinhan.dongibuyeo.domain.chat.repository;


import com.shinhan.dongibuyeo.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Message, UUID> {

}
