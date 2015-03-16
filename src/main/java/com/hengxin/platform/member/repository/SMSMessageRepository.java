package com.hengxin.platform.member.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.member.domain.SMSMessage;

public interface SMSMessageRepository extends CrudRepository<SMSMessage, String> {

	List<SMSMessage> findByStatusAndSendTsLessThan(String status, Date sendTs);
	
	/**
	 * update msgs less than current date.
	 * @param status target status
	 * @param currentDate
	 */
    @Modifying
    @Query("UPDATE SMSMessage Set status = :status, lastMntOpid = 'sys', lastMntTs = :time WHERE status = 'N' AND sendTs < :time ")
    void updateAllMessagesStatus(@Param("status") String status, @Param("time") Date currentDate);
	
}
