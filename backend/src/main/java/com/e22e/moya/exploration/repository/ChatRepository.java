package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.chatting.Chat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

//    @Query(value = "SELECT * FROM Chat as c WHERE c.userId =: userId and c.npcPos.id =: npcPosId")
//    Optional<Chat> findByNpcPosIdAndUserId(Long npcPosId, Long userId);
}