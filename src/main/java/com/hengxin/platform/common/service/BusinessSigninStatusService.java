package com.hengxin.platform.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.batch.enums.ETaskGroupId;
import com.hengxin.platform.batch.service.JobWorkService;
import com.hengxin.platform.common.domain.BusinessSigninJournal;
import com.hengxin.platform.common.domain.BusinessSigninStatus;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.repository.BusinessSigninJournalRepository;
import com.hengxin.platform.common.repository.BusinessSigninStatusRepository;
import com.hengxin.platform.common.util.CacheUtil;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MessageUtil;
import com.hengxin.platform.common.util.PaginationUtil;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.domain.CreditorRightsTransfer;
import com.hengxin.platform.product.enums.ETransferStatus;
import com.hengxin.platform.product.repository.CreditorRightsTransferRepository;
import com.hengxin.platform.sms.dto.MessageInfoDto;

@Service
@Transactional
public class BusinessSigninStatusService {
    
    private static final String MARKET_TYPE = "MKT";
    @SuppressWarnings("unused")
	private static final String CMB_TYPE = "CMB";

	@Autowired
	transient BusinessSigninStatusRepository businessSigninStatusRepository;
	@Autowired
	transient BusinessSigninJournalRepository businessSigninJournalRepository;
	@Autowired
	transient JobWorkService jobWorkService;
	@Autowired
	transient CreditorRightsTransferRepository transferRepository;
	@Autowired
	transient MemberMessageService memberMessageService;

	/**
	 * Description: 获取开市闭市历史记录
	 * 
	 * @param workType
	 * @param searchDto
	 * @return
	 */

	public Page<BusinessSigninJournal> getSigninJournalByWorkType(
			final String workType, final DataTablesRequestDto searchDto) {
	    Pageable pageable;
	    if(StringUtils.equals(workType, MARKET_TYPE)) {
	        pageable = PaginationUtil.buildPageRequest(searchDto);
	    } else {
	        pageable = buildPageRequest(searchDto);
	    }
		Specification<BusinessSigninJournal> spec = new Specification<BusinessSigninJournal>() {
			@Override
			public Predicate toPredicate(Root<BusinessSigninJournal> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate
						.getExpressions();
				if (StringUtils.isNotBlank(workType)) {
					expressions.add(cb.like(
							cb.lower(root.<String> get("workType")), "%"
									+ workType.toLowerCase() + "%"));
				}
				return predicate;
			}
		};
		Page<BusinessSigninJournal> productPackageList = businessSigninJournalRepository
				.findAll(spec, pageable);
		return productPackageList;
	}

	
    /**
    * Description: TODO
    *
    * @param searchDto
    * @return
    */
    	
    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        for (Integer item : sortedColumns) {
            String sortColumn = dataProps.get(item);
            int indexOf = 0;
            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".text")) {
                indexOf = sortColumn.lastIndexOf(".text");
            } else if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".fullText")) {
                indexOf = sortColumn.lastIndexOf(".fullText");
            }
            if (indexOf > 0) {
                sortColumn = sortColumn.substring(0, indexOf);
            }
            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.fromString(sortDir), "createTime"));
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    /**
	 * 
	 * Description: 获取当前状态
	 * 
	 * @param workType
	 * @return
	 */
	public String getCurrentMarketStatus(String workType) {
		return businessSigninStatusRepository.findOne(workType).getStatus();
	}

	/**
	 * Description: 开市闭市操作保存
	 * 
	 * @param workType
	 * @param status
	 */
	@CacheEvict(value = CacheUtil.CACHE_NAME, key = "'BusinessSigninStatus_' + #workType")
	public List<MessageInfoDto> saveBusinessSigninStatus(String workType, String status,
			String currentUserId) {
		Date operateTime = new Date();
		List<MessageInfoDto> messageList = new ArrayList<MessageInfoDto>();
		if (StringUtils.equals("O", status)) {
			boolean exists = jobWorkService.existUnSucceedTasks(
					Arrays.asList(ETaskGroupId.REPAYMENT.getCode(),
					ETaskGroupId.BIZTASK.getCode()));
			if (exists) {
				throw new BizServiceException(
						EErrorCode.PROD_SIGNIN_OPEN_RECORD);
			}
		} else if (StringUtils.equals("C", status)) {

			messageList = sendCRTCloseMessage();

			// 闭市关闭所有债权转让记录
			transferRepository.updateOverdueTransfer();
		}

		Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
		BusinessSigninStatus bsPo = businessSigninStatusRepository
				.findOne(workType);
		bsPo.setStatus(status);
		bsPo.setWorkDate(currentWorkDate);
		bsPo.setLastMntOpid(currentUserId);
		businessSigninStatusRepository.save(bsPo);

		BusinessSigninJournal journalPo = new BusinessSigninJournal();
		journalPo.setWorkType(workType);
		journalPo.setWorkDate(currentWorkDate);
		journalPo.setOperateType(status);
		journalPo.setOperateDate(operateTime);
		journalPo.setCrateOperateId(currentUserId);
		journalPo.setCreateTime(operateTime);
		businessSigninJournalRepository.save(journalPo);
		return messageList;
	}

	/**
     * Description: 招行签到操作保存
     * 
     * @param workType
     * @param status
	 * @param date 
     */
    public void saveCmbStatus(String workType, String status,
            String currentUserId, String date) {
        Date operateTime = new Date();
        Date currentWorkDate = CommonBusinessUtil.getCurrentWorkDate();
        BusinessSigninJournal journalPo = new BusinessSigninJournal();
        journalPo.setSignDate(DateUtils.getDate(date, "yyyy-MM-dd"));
        journalPo.setWorkType(workType);
        journalPo.setWorkDate(currentWorkDate);
        journalPo.setOperateType(status);
        journalPo.setOperateDate(operateTime);
        journalPo.setCrateOperateId(currentUserId);
        journalPo.setCreateTime(operateTime);
        businessSigninJournalRepository.save(journalPo);
    }
    
	/**
	 * 发送债权转让闭市自动撤单短信
	 */
	private List<MessageInfoDto> sendCRTCloseMessage() {
		List<MessageInfoDto> messageList = new ArrayList<MessageInfoDto>();
		List<CreditorRightsTransfer> list = transferRepository
				.findByStatus(ETransferStatus.ACTIVE.getCode());
		String messageKey = "creditorRightsTransfer.close.message";
		for (CreditorRightsTransfer transfer : list) {
			memberMessageService.sendMessage(
					EMessageType.MESSAGE, messageKey,
				    transfer.getPositionLot().getPosition().getUserId(),
					new Object[0]);
			
			//message send
			MessageInfoDto messageInfoDto = new MessageInfoDto();
			messageInfoDto.setMessageType(EMessageType.SMS);
        	messageInfoDto.setMessageKey(messageKey);
        	messageInfoDto.setUserId(transfer.getPositionLot().getPosition().getUserId());
        	messageInfoDto.setContent(MessageUtil
					.getMessage("creditorRightsTransfer.close.message.content"));
        	messageList.add(messageInfoDto);
		}
		return messageList;
	}
}
