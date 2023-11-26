package com.sheryians.major.repository;

import com.sheryians.major.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findByReceiverId(Integer id);
}
