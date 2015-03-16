package com.hengxin.platform.member.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.constant.ApplicationConstant;
import com.hengxin.platform.member.domain.AutoSubscribeParam;
import com.hengxin.platform.member.repository.AutoSubscribeParamRepository;

@Service
public class AutoSubscribeParamService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AutoSubscribeParamService.class);
	
	@Autowired
	private AutoSubscribeParamRepository autoSubscriberRepository;
	
	/**
	 * Get latest Auto Subscribe info.
	 * @param userId
	 * @return collection include pending and using data.
	 */
	@Transactional(readOnly = true)
	public List<AutoSubscribeParam> getAutoSubscriberParam(final String userId) {
		LOGGER.info("getAutoSubscriberParam() invoked");
		Pageable page = new PageRequest(0, 2, Direction.DESC, "lastMntTs");
//		Sort sort = new Sort(Direction.DESC, "lastMntTs");
        Specification<AutoSubscribeParam> specification = new Specification<AutoSubscribeParam>() {
            @Override
            public Predicate toPredicate(Root<AutoSubscribeParam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<String> get("userId"), userId));
                return predicate;
            }
        };
		Page<AutoSubscribeParam> lists = autoSubscriberRepository.findAll(specification, page);
		return lists.hasContent() ? lists.getContent() : Collections.<AutoSubscribeParam> emptyList();
	}
	
	/**
	 * Get latest Auto Subscribe info.
	 * @param userId
	 * @return null when never subscribe.
	 */
	@Transactional(readOnly = true)
	public AutoSubscribeParam getAutoSubscriberParam(final String userId, final String activeFlag) {
		LOGGER.info("getAutoSubscriberParam() invoked");
		Pageable page = new PageRequest(0, 1, Direction.DESC, "lastMntTs");
//		Sort sort = new Sort(Direction.DESC, "lastMntTs");
        Specification<AutoSubscribeParam> specification = new Specification<AutoSubscribeParam>() {
            @Override
            public Predicate toPredicate(Root<AutoSubscribeParam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<String> get("userId"), userId));
                if (activeFlag != null) {
                	expressions.add(cb.equal(root.<String> get("activeFlag"), activeFlag));	
				}
                return predicate;
            }
        };
		Page<AutoSubscribeParam> lists = autoSubscriberRepository.findAll(specification, page);
		if (lists.hasContent()) {
			return lists.getContent().get(0);
		}
		return null;
	}
	
	@Transactional
	public void saveAutoSubscriberParam(AutoSubscribeParam entity, String userId) {
		LOGGER.info("saveAutoSubscriberParam() invoked");
		Date currentDate = new Date();
    	if (entity.getId() == null || entity.getId().isEmpty()) {
    		entity.setUserId(userId);
    		entity.setLastMntTs(currentDate);
    		entity.setLastMntOpId(userId);
    		entity.setCreateTs(currentDate);
    		entity.setCreatorOpId(userId);
    		entity.setVersion(0L);
    		entity.setTerminationFlag(ApplicationConstant.DISABLE_CODE);
    	} else {
    		entity.setLastMntTs(currentDate);
    		entity.setLastMntOpId(userId);
    		entity.setTerminationFlag(ApplicationConstant.DISABLE_CODE);
    	}
		autoSubscriberRepository.save(entity);
	}

	/**
	 * process all pending data automatically.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void launchPendingSubscribeParams() {
		LOGGER.info("launchParamStatus() invoked");
		/** Launch positive data. **/
		List<AutoSubscribeParam> pendingParams = this.loadPendingData(ApplicationConstant.DISABLE_CODE, ApplicationConstant.DISABLE_CODE);
		for (AutoSubscribeParam pendingParam : pendingParams) {
			String userId = pendingParam.getUserId();
			AutoSubscribeParam param = this.getAutoSubscriberParam(userId, ApplicationConstant.ENABLE_CODE);
			if (param == null) {
				/** Process subscribe param at first time. **/
				pendingParam.setActiveFlag(ApplicationConstant.ENABLE_CODE);
				pendingParam.setTerminationFlag(ApplicationConstant.DISABLE_CODE);
				this.autoSubscriberRepository.save(pendingParam);
			} else {
				/** Merge subscriber param to original data, remove pending data. **/
				LOGGER.debug("Id {}", param.getId());
				param.setLastMntTs(new Date());
				param.setLastMntOpId(pendingParam.getLastMntOpId());
				param.setMinDateRange(pendingParam.getMinDateRange());
				param.setMaxDateRange(pendingParam.getMaxDateRange());
				param.setMinAPRForDate(pendingParam.getMinAPRForDate());
				param.setMinMonthRange(pendingParam.getMinMonthRange());
				param.setMaxMonthRange(pendingParam.getMaxMonthRange());
				param.setMinAPRForMonth(pendingParam.getMinAPRForMonth());
				param.setRiskParam(pendingParam.getRiskParam());
				param.setRepayment(pendingParam.getRepayment());
				param.setRiskGuarantee(pendingParam.getRiskGuarantee());
				param.setMinBalance(pendingParam.getMinBalance());
				param.setMaxSubscribeAmount(pendingParam.getMaxSubscribeAmount());
				param.setTerminationFlag(ApplicationConstant.DISABLE_CODE);
				this.autoSubscriberRepository.delete(pendingParam);
				this.autoSubscriberRepository.save(param);
			}
		}
		/** Launch negative data. **/
		List<AutoSubscribeParam> pendingRelieveParams = this.loadPendingData(ApplicationConstant.ENABLE_CODE, ApplicationConstant.PENDING_CODE);
		for (AutoSubscribeParam pendingParam : pendingRelieveParams) {
			/** Keep original score.... **/
			pendingParam.setActiveFlag(ApplicationConstant.DISABLE_CODE);
			pendingParam.setTerminationFlag(ApplicationConstant.ENABLE_CODE);
			this.autoSubscriberRepository.save(pendingParam);
//			this.autoSubscriberRepository.delete(pendingParam);
		}
		LOGGER.debug("launchParamStatus() completed");
	}
	
	private List<AutoSubscribeParam> loadPendingData(String activeFlag, String termFlag) {
		List<AutoSubscribeParam> pendingParams = this.autoSubscriberRepository.findByActiveFlagAndTerminationFlag(activeFlag, termFlag);
		LOGGER.debug("Pending data number {}", pendingParams.size());
		return pendingParams;
	}
	
	@Transactional
	public void relieveParamStatus(String userId) {
		AutoSubscribeParam entity = this.getAutoSubscriberParam(userId, ApplicationConstant.ENABLE_CODE);
		if (entity == null) {
			LOGGER.warn("No active data, it should not allow user to relieve param");
		} else {
			Date currentDate = new Date();
			/** It still can be work at current date. **/
			entity.setActiveFlag(ApplicationConstant.ENABLE_CODE);
			entity.setTerminationFlag(ApplicationConstant.PENDING_CODE);
			entity.setLastMntTs(currentDate);
			entity.setLastMntOpId(userId);
			this.autoSubscriberRepository.save(entity);
		}
		/** Delete pending data since user already relieve subscribed. **/
		AutoSubscribeParam pendingEntity = this.getAutoSubscriberParam(userId, ApplicationConstant.DISABLE_CODE);
		if (pendingEntity != null) {
			this.autoSubscriberRepository.delete(pendingEntity);
		}
	}
	
}
