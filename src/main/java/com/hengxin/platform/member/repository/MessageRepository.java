package com.hengxin.platform.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.member.domain.Message;
import com.hengxin.platform.member.enums.EMessageType;

public interface MessageRepository extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {
    
    public List<Message> findByUserId(String userId);

    @Query("select count(a) from Message a where a.userId = ?1 and a.read=?2 and a.type!=?3")
    public int findByUserIdAndRead(String userId, boolean read,EMessageType type);

    @Query("select count(a) from Message a where a.userId = ?1 and a.messageId >= ?2 and a.type = (select type from Message where messageId = ?2)")
    public int findMessagePageByUserIdAndMessageId(String userId, String messageId);
}
