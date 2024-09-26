package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.chatting.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}