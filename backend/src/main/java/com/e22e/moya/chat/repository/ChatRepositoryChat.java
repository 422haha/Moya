package com.e22e.moya.chat.repository;

import com.e22e.moya.common.entity.chatting.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepositoryChat extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE c.npcPos.id = :npcPosId AND c.userId = :userId")
    Optional<Chat> findByNpcPosIdAndUserId(@Param("npcPosId") Long npcPosId, @Param("userId") Long userId);
}