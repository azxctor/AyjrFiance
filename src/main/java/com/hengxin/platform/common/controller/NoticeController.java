
 
	
package com.hengxin.platform.common.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.dto.NoticeDto;
import com.hengxin.platform.common.entity.Notice;
import com.hengxin.platform.common.service.NoticeService;
import com.hengxin.platform.member.dto.MessageCriteria;
import com.hengxin.platform.product.ProductUtil;
import com.hengxin.platform.security.SecurityContext;


/**
 * Class Name: NoticeController Description: TODO
 *  
 */
@Controller
public class NoticeController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);
    
    @Autowired
    private NoticeService noticeService; 
    
    @Autowired
    private SecurityContext securityContext;
    
    @ResponseBody
    @RequestMapping(value = "/notice/{noticeId}", method = RequestMethod.GET)
    public NoticeDto getMessageWithMessageId(@PathVariable String noticeId, Model model) {
        Notice po = noticeService.findOne(noticeId);
        NoticeDto dto = new NoticeDto(); 
    	dto.setContent(po.getContent());
    	dto.setTitle(po.getTitle());
    	dto.setNoticeId(po.getNoticeId()); 
        dto.setCreateTs(ProductUtil.formatDate(po.getCreateTs(),"yyyy-MM-dd HH:mm:ss"));
        return dto;
    }

    /**
     * 分页列表
     * @param searchCriteria
     * @return
     */
    @ResponseBody 
    @RequestMapping(value = "/notice/list", method = RequestMethod.POST)
    public DataTablesResponseDto<NoticeDto> getMessages(@RequestBody MessageCriteria searchCriteria) {
        searchCriteria.setUserId(securityContext.getCurrentUserId());
        List<String> lis = new ArrayList<String>();
        lis.add("createTs");
        searchCriteria.setDataProp(lis);
        List<String> sort = new ArrayList<String>();
        sort.add("desc");
        searchCriteria.setSortDirections(sort);
        searchCriteria.setReadable(false);
        DataTablesResponseDto<NoticeDto> result = noticeService.getNotices(searchCriteria);
        return result;
    } 
     
    /**
     * 滚动
     * @return
     */
    @ResponseBody 
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/notice/listTop", method = RequestMethod.GET)
    public DataTablesResponseDto<NoticeDto> getMessages() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getSelfNoticeList() start: " );
        } 
        DataTablesResponseDto<NoticeDto> result=new DataTablesResponseDto<NoticeDto>();
		try { 
			result = (DataTablesResponseDto<NoticeDto>)SecurityUtils.getSubject().getSession().getAttribute("userNotices");
		} catch (Exception e) { 
			LOGGER.debug("getListTop() notices is null" ); 
		}
		if(null==result){
			result=new DataTablesResponseDto<NoticeDto>();
		}
        return result;
    }
    
    /**
     * 独立弹窗列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/notice/listOnlyList", method = RequestMethod.GET)
    public DataTablesResponseDto<NoticeDto> getOnlyList() {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("getOnlyList() start: " );
    	} 
    	DataTablesResponseDto<NoticeDto> result=new DataTablesResponseDto<NoticeDto>();
    	try {  
    		result = noticeService.getNoticeDias(securityContext.getCurrentUserId(),"O",10);
    	} catch (Exception e) { 
    		LOGGER.debug("getListTop() notices is null" ); 
    	}
    	if(null==result){
    		result=new DataTablesResponseDto<NoticeDto>();
    	}
    	return result;
    }
    /**
     * 滚动弹窗列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/notice/listMoreList", method = RequestMethod.GET)
    public DataTablesResponseDto<NoticeDto> getMList() {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.debug("getOnlyList() start: " );
    	} 
    	DataTablesResponseDto<NoticeDto> result=new DataTablesResponseDto<NoticeDto>();
    	try {  
    		result = noticeService.getNoticeDias(securityContext.getCurrentUserId(),"M",100);
    	} catch (Exception e) { 
    		LOGGER.debug("getListTop() notices is null" ); 
    	}
    	if(null==result){
    		result=new DataTablesResponseDto<NoticeDto>();
    	}
    	return result;
    }
}
