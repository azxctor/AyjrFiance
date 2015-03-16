package com.hengxin.platform.common.service;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.NoticeDto;
import com.hengxin.platform.common.entity.Notice;
import com.hengxin.platform.member.dto.MessageCriteria;

 
public interface NoticeService {
	public Notice findOne(String id);
	public DataTablesResponseDto<NoticeDto> getNotices(final String userId);
	public DataTablesResponseDto<NoticeDto> getNoticeDias(String userId,String showMethod,int length);
    public DataTablesResponseDto<NoticeDto> getNotices(final MessageCriteria searchCriteria);
}
