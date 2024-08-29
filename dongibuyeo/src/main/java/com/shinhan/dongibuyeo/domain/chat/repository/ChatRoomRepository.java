package com.shinhan.dongibuyeo.domain.chat.repository;

import com.shinhan.dongibuyeo.domain.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<Room, UUID> {

    Optional<Room> findByName(String name);

    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.messages rm WHERE r.name = :name")
    Optional<Room> findByNameAndChats(String name);
}
