package com.hengxin.platform.product.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.NumberUtil;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.fund.repository.PositionLotRepository;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.CreditorTransferPackageRule;
import com.hengxin.platform.product.domain.CreditorTransferRule;
import com.hengxin.platform.product.domain.PaymentSchedule;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.CreditorTransferInitDto;
import com.hengxin.platform.product.dto.CreditorTransferMaintainDto;
import com.hengxin.platform.product.dto.CreditorTransferRuleDto;
import com.hengxin.platform.product.enums.EPayStatus;
import com.hengxin.platform.product.repository.CreditorTransferPackageRuleRepository;
import com.hengxin.platform.product.repository.CreditorTransferRuleRepository;
import com.hengxin.platform.product.repository.PaymentScheduleRepository;
import com.hengxin.platform.security.SecurityContext;

/**
 * CreditorTransferMaintainServie.
 *
 */
@Service
public class CreditorTransferMaintainServie extends BaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreditorTransferMaintainServie.class);
	
    @Autowired
    private CreditorTransferService creditorTransferService;

    @Autowired
    private CreditorTransferPackageRuleRepository creditorTransferPackageRuleRepository;

    @Autowired
    private CreditorTransferRuleRepository creditorTransferRuleRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private SecurityContext securityContext;
    
    @Autowired
    private PkgTradeJnlRepository pkgTradeJnlRepository;
    
    @Autowired
    private PositionLotRepository positionLotRepository;
    
    /**
     * 
     * Description: get data from table for setting the selected
     * 
     * @return
     */
    @Transactional(readOnly = true)
    public CreditorTransferMaintainDto getCreditorTransferMaintainDto() {
        CreditorTransferMaintainDto ctDto = new CreditorTransferMaintainDto();
        List<CreditorTransferPackageRule> ctps = creditorTransferPackageRuleRepository.findByPackageIdNotNull();
        List<CreditorTransferInitDto> ctis = new ArrayList<CreditorTransferInitDto>();
        for (CreditorTransferPackageRule creditorTransferPackageRule : ctps) {
            CreditorTransferInitDto cti = new CreditorTransferInitDto();
            cti.setId(creditorTransferPackageRule.getPackageId());
            ctis.add(cti);
        }
        ctDto.setPackageListSelected(ctis);
        List<CreditorTransferPackageRule> ctpsType = creditorTransferPackageRuleRepository.findByPackageTypeNotNull();
        List<CreditorTransferInitDto> ctisType = new ArrayList<CreditorTransferInitDto>();
        for (CreditorTransferPackageRule creditorTransferPackageRule : ctpsType) {
            CreditorTransferInitDto cti = new CreditorTransferInitDto();
            cti.setId(creditorTransferPackageRule.getPackageType());
            ctisType.add(cti);
        }
        ctDto.setRuleListSelected(ctisType);
        Iterable<CreditorTransferRule> creditorTransferRules = this.creditorTransferRuleRepository.findAll();
        CreditorTransferRuleDto cTransferRule = null;
        for (CreditorTransferRule creditorTransferRule : creditorTransferRules) {
            cTransferRule = ConverterService.convert(creditorTransferRule, CreditorTransferRuleDto.class);
            break;
        }
        if (cTransferRule == null) {
            cTransferRule = new CreditorTransferRuleDto();
            cTransferRule.setOverdueFlag("N");
        }
        ctDto.setCreditorTransferRuleDto(cTransferRule);
        return ctDto;
    }

    /**
     * 
     * Description: save to tabale
     * 
     * @param ctmt
     */
    @Transactional
    public void saveCreditorTransferMaintain(CreditorTransferMaintainDto ctmt) {
        if (ctmt == null) {
            return;
        }
        List<CreditorTransferRule> cRules = this.creditorTransferRuleRepository.findByRuleIdNotNull();
        Date date = new Date();
        Date cDate = null;
        String cOpId = null;
        if (cRules != null && cRules.size() > 0) {
            cOpId = cRules.get(0).getCreateOpid();
            cDate = cRules.get(0).getCreateTs();
        }
        this.creditorTransferPackageRuleRepository.deleteAll();
        this.creditorTransferRuleRepository.deleteAll();
        String opid = this.securityContext.getCurrentUserId();
        CreditorTransferRule creditorTransferRule = ConverterService.convert(ctmt.getCreditorTransferRuleDto(),
                CreditorTransferRule.class);
        if (creditorTransferRule != null) {
            if (cRules != null && cRules.size() > 0) {
                creditorTransferRule.setLastMntOpid(opid);
                creditorTransferRule.setLastMntTs(date);
                creditorTransferRule.setCreateOpid(cOpId);
                creditorTransferRule.setCreateTs(cDate);
            } else {
                creditorTransferRule.setCreateOpid(opid);
                creditorTransferRule.setCreateTs(date);
                creditorTransferRule.setLastMntOpid(opid);
                creditorTransferRule.setLastMntTs(date);
            }
            creditorTransferRule.setRuleId(1);
            creditorTransferRuleRepository.save(creditorTransferRule);
        }
        int count = 1;
        if (ctmt.getPackageListSelected() != null && ctmt.getPackageListSelected().size() > 0) {
            count = saveSelecdRule(ctmt.getPackageListSelected(), opid, 1, false, count, date, cOpId, cDate);
        }
        if (ctmt.getRuleListSelected() != null && ctmt.getRuleListSelected().size() > 0) {
            saveSelecdRule(ctmt.getRuleListSelected(), opid, 1, true, count, date, cOpId, cDate);
        }
    }

    /**
     * 
     * Description: save select rule or package in the table
     * 
     * @param creditorTransferInitDtos
     * @param opid
     * @param ruleId
     * @param flag
     * @param count
     * @param date
     * @param cOpid
     * @param cDate
     * @return
     */
    private int saveSelecdRule(List<CreditorTransferInitDto> creditorTransferInitDtos, String opid, Integer ruleId,
            boolean flag, int count, Date date, String cOpid, Date cDate) {
        for (CreditorTransferInitDto creditorTransferInitDto : creditorTransferInitDtos) {
            CreditorTransferPackageRule cTPRule = new CreditorTransferPackageRule();
            if (StringUtils.isNotBlank(cOpid) && cDate != null) {
                cTPRule.setCreateOpid(cOpid);
                cTPRule.setCreateTs(cDate);
                cTPRule.setLastMntOpid(opid);
                cTPRule.setLastMntTs(date);
            } else {
                cTPRule.setCreateOpid(opid);
                cTPRule.setCreateTs(date);
                cTPRule.setLastMntOpid(opid);
                cTPRule.setLastMntTs(date);
            }
            if (!flag) {
                cTPRule.setAllowType("N");
                cTPRule.setPackageId(creditorTransferInitDto.getId());
            } else {
                cTPRule.setAllowType("Y");
                cTPRule.setPackageType(creditorTransferInitDto.getId());
            }
            cTPRule.setRuleId(ruleId);
            cTPRule.setSquenceId(count);
            this.creditorTransferPackageRuleRepository.save(cTPRule);
            count++;
        }
        return count;
    }

    /**
     * Description: 判断包是否能转让
     * 
     * @param lotId
     * @return
     */
    public boolean canPackageTransfer(String lotId) {
        ProductPackage productPackage = this.creditorTransferService.getPackageInfoByLotId(lotId);
        if (productPackage == null) {
            return false;
        }
        boolean flag = true;
        // check 禁止转让的融资包
        String packageId = productPackage.getId();
        if (packageId != null) {
            CreditorTransferPackageRule notAllowPackage = creditorTransferPackageRuleRepository
                    .findByPackageId(packageId);
            if (notAllowPackage != null) {
                return false;
            }
        }
        // 融资包类型
        Product product = this.creditorTransferService.getProductByLotId(lotId);
        String type = null;
        if (product.getWarrantyType() != null && NumberUtil.isPositive(product.getTotalGrage())) {
            type = product.getProductLevel().getCode() + product.getWarrantyType().getCode();
        }
        if (type != null && type.length() == 2) {
            List<CreditorTransferPackageRule> allowTypeList = creditorTransferPackageRuleRepository
                    .findByPackageTypeAndAllowType(type, "Y");
            if (allowTypeList == null || allowTypeList.isEmpty()) {
                return false;
            }
        }
        // 违约情况
        List<CreditorTransferRule> transferRuleList = creditorTransferRuleRepository.findByRuleIdNotNull();
        if (transferRuleList != null && !transferRuleList.isEmpty()) {
            CreditorTransferRule creditorTransferRule = transferRuleList.get(0);
            // 融资期限小于（包含）n天的融资包 
            if (product.getTermToDays() <= creditorTransferRule.getMinTermDays()) {
            	return false;
            }
            List<PaymentSchedule> allowPaymentList = paymentScheduleRepository
                    .getByPackageIdOrderBySequenceIdDesc(packageId);
            if ("N".equalsIgnoreCase(creditorTransferRule.getOverdueFlag())) {
                BigDecimal forFeit = null;
                for (PaymentSchedule paymentSchedule : allowPaymentList) {
                    forFeit = paymentSchedule.getPrincipalForfeit().add(paymentSchedule.getInterestForfeit())
                            .add(paymentSchedule.getFeeForfeit());
                    if (forFeit.compareTo(BigDecimal.valueOf(0)) > 0) {
                        return false;
                    }
                }
            }
            if ("Y".equalsIgnoreCase(creditorTransferRule.getOverdueFlag())) {
                Integer maxOverdueDate = creditorTransferRule.getMaxOverdueDate();
                Integer maxOverdueCount = creditorTransferRule.getMaxOverdueCount();

                int count = 0;
                BigDecimal forFeit = null;
                for (PaymentSchedule paymentSchedule : allowPaymentList) {
                    forFeit = paymentSchedule.getPrincipalForfeit().add(paymentSchedule.getInterestForfeit())
                            .add(paymentSchedule.getFeeForfeit());
                    if (forFeit.compareTo(BigDecimal.valueOf(0)) > 0) {
                        count++;
                    }
                }

                if (count >= maxOverdueCount) {
                    return false;
                }

                int countDay = this.getOverdueDays(allowPaymentList);
                if (countDay >= maxOverdueDate) {
                    return false;
                }
            }
        }
        
        // 债权持有期限不满5个交易日（含5日）的融资包
        Date pkgTradeDate = positionLotRepository.findOne(lotId).getSettleDt();
		if (pkgTradeDate != null
				&& DateUtils.betweenDays(pkgTradeDate,
				CommonBusinessUtil.getCurrentWorkDate()) <= CommonBusinessUtil.getInverstorHoldDays()) {
        	return false;
        }
        
        // 闭市
		if (!CommonBusinessUtil.isMarketOpen()) {
			return false;
		}
		
		// 还款日当天
		if (paymentScheduleRepository.findOneByPackageIdAndPaymentDate(
				packageId, CommonBusinessUtil.getCurrentWorkDate()).size() > 0) {
			return false;
		}
		
		// 累计持有人数超过200
		Integer nuyerCount = pkgTradeJnlRepository.getBuyerCount(productPackage.getId());
		if (nuyerCount != null && nuyerCount >= 200) {
			return false;
		}
		
		// 存在代追偿记录
		if (!paymentScheduleRepository.getByPackageIdAndStatusOrderBySequenceIdAsc(packageId, EPayStatus.BADDEBT).isEmpty()) {
			return false;
		}
		
        return flag;
    }

    /**
     * 
     * Description:
     * 
     * @param financingPackage
     * @return
     */
    private int getOverdueDays(List<PaymentSchedule> paymentList) {
        int countDay = 0;
        Date date = null;
        Date paymentDate = null;
        try {
            date = DateUtils.getEndDate(CommonBusinessUtil.getCurrentWorkDate());
        } catch (ParseException e) {
        	LOGGER.error("EX {}", e);
        }

        for (PaymentSchedule paymentSchedule : paymentList) {
            if (paymentSchedule != null && paymentSchedule.getPaymentDate() != null) {
                try {
                    Date tmp = DateUtils.getStartDate(paymentSchedule.getPaymentDate());
                    paymentDate = DateUtils.getStartDate(tmp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (paymentDate.before(date)) {
                    if (paymentSchedule.getStatus() == EPayStatus.CLEARED
                            || paymentSchedule.getStatus() == EPayStatus.COMPENSATING
                            || paymentSchedule.getStatus() == EPayStatus.OVERDUE
                            || paymentSchedule.getStatus() == EPayStatus.COMPENSATORY) {
                        countDay = DateUtils.betweenDays(paymentDate, date);
                        break;
                    }
                }
            }
        }
        return countDay;
    }

}
