package com.hengxin.platform.fund.service.impl;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.domain.BankAcct.CreateBankAcct;
import com.hengxin.platform.fund.domain.BankAcct.UpdateBankAcct;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.service.AcctEscrowedModeService;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.fund.service.FundAcctCashPoolChangeService;
import com.hengxin.platform.fund.service.FundUnEndApplService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * BankAcctService.
 *
 */
@Service
@Qualifier("bankAcctService")
public class BankAcctServiceImpl extends BaseService implements BankAcctService {

    @Autowired
    private AcctRepository acctRepository;
    @Autowired
    private BankAcctRepository bankAcctRepository;
	@Autowired
	private FundUnEndApplService fundUnEndApplService;
	@Autowired
	private AcctEscrowedModeService acctEscrowedModeService;
    @Autowired
    private FundAcctCashPoolChangeService fundAcctCashPoolChangeService;

    @Override
    public void createBankAcct(BankAcct bnkAcct) throws BizServiceException {
        validate(bnkAcct, CreateBankAcct.class);
        BankAcctPo oldBankAcctPo = bankAcctRepository.findBankAcctByBnkAcctNo(bnkAcct.getOldBnkAcctNo());
        ECashPool fromPool = null;
        ECashPool toPool = null;
        if (oldBankAcctPo != null) {
            if (StringUtils.equals(bnkAcct.getUserId(), oldBankAcctPo.getUserId())) {
                bankAcctRepository.delete(oldBankAcctPo);
                bankAcctRepository.flush();
                bankAcctRepository.save(ConverterService.convert(bnkAcct, oldBankAcctPo));
                fromPool = acctEscrowedModeService.getCashPool(oldBankAcctPo.getSignedFlg(),
                        oldBankAcctPo.getBnkCd());
                toPool = acctEscrowedModeService.getCashPool(bnkAcct.getSignedFlg(), bnkAcct.getBnkCd());
            } 
            else {
                EErrorCode errorCode = EErrorCode.BANK_ACCT_ALREADY_USED_BY_OTHERS;
                errorCode.setArgs(new Object[] { bnkAcct.getOldBnkAcctNo() });
                throw new BizServiceException(errorCode);
            }
        } else {
            bankAcctRepository.save(ConverterService.convert(bnkAcct, BankAcctPo.class));
            toPool = acctEscrowedModeService.getCashPool(bnkAcct.getSignedFlg(), bnkAcct.getBnkCd());
        }

        this.updateAcctCashPool(bnkAcct.getUserId(), fromPool, toPool, bnkAcct.getCreateOpid());
    }

    private void updateAcctCashPool(String userId, ECashPool fromPool, ECashPool toPool, String currOpId)
            throws BizServiceException {
        AcctPo acctPo = acctRepository.findByUserId(userId);
        if(acctPo == null){
            return;
        }
        // 判断是否允许变更资金池
        fundUnEndApplService.checkUnEndAppl(userId);
        
        String eventId = IdUtil.produce();
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        fundAcctCashPoolChangeService.changePoolTreasurerAcctBal(eventId, acctPo.getAcctNo(), fromPool, toPool,
                "会员银行卡信息变更，资金池变更", userId, currOpId, workDate);
    }

    @Override
    public void updateBankAcct(BankAcct bnkAcct) throws BizServiceException {
        validate(bnkAcct, UpdateBankAcct.class);
        BankAcctPo oldBankAcctPo = bankAcctRepository.findBankAcctByBnkAcctNo(bnkAcct.getOldBnkAcctNo());
        ECashPool fromPool = null;
        ECashPool toPool = null;
        boolean escrowMode = acctEscrowedModeService.isEscrowedMode();
        if (oldBankAcctPo != null) {
            if (!StringUtils.equals(bnkAcct.getBnkAcctNo(), bnkAcct.getOldBnkAcctNo())) {
                BankAcctPo othersBankAcctPo = bankAcctRepository.findBankAcctByBnkAcctNo(bnkAcct.getBnkAcctNo());
                if (othersBankAcctPo != null) {
                    EErrorCode errorCode = EErrorCode.BANK_ACCT_ALREADY_USED_BY_OTHERS;
                    errorCode.setArgs(new Object[] { bnkAcct.getBnkAcctNo() });
                    throw new BizServiceException(errorCode);
                }
                bankAcctRepository.delete(oldBankAcctPo);
                bankAcctRepository.flush();
                // 若卡号变更，则变为非签约
                bnkAcct.setSignedFlg(EFlagType.NO.getCode());
            }
            // 若资金池变更，则变为非签约
            if (!StringUtils.equals(bnkAcct.getBnkCd(), oldBankAcctPo.getBnkCd())) {
                bnkAcct.setSignedFlg(EFlagType.NO.getCode());
            }
            AcctPo acctPo = acctRepository.findByUserId(bnkAcct.getUserId());
            if (acctPo!=null&&StringUtils.isNotBlank(acctPo.getCashPool())){
            	fromPool = EnumHelper.translate(ECashPool.class, acctPo.getCashPool());
            }
            else {
	            fromPool = acctEscrowedModeService
	                    .getCashPool(oldBankAcctPo.getSignedFlg(), oldBankAcctPo.getBnkCd());
            }
            toPool = acctEscrowedModeService.getCashPool(bnkAcct.getSignedFlg(), bnkAcct.getBnkCd());
            if(escrowMode){
            	bnkAcct.setSignedFlg(EFlagType.YES.getCode());
            }
            bankAcctRepository.save(ConverterService.convert(bnkAcct, oldBankAcctPo));
        } 
        else {
        	/** 如果遗漏账号, 没有原银行卡号, 默认非签约. **/
            if (bnkAcct.getSignedFlg() == null) {
            	bnkAcct.setSignedFlg(EFlagType.NO.getCode());
			}
            if(escrowMode){
            	bnkAcct.setSignedFlg(EFlagType.YES.getCode());
            }
            bankAcctRepository.save(ConverterService.convert(bnkAcct, BankAcctPo.class));
            toPool = acctEscrowedModeService.getCashPool(bnkAcct.getSignedFlg(), bnkAcct.getBnkCd());
        }

        this.updateAcctCashPool(bnkAcct.getUserId(), fromPool, toPool, bnkAcct.getCreateOpid());
    }
    
    @Override
    public List<BankAcct> findBankAcctByUserIdWihoutCheck(String userId) {
        if (!StringUtils.isNotBlank(userId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED, "用户编号不能为空");
        }
        List<BankAcct> list = new ArrayList<BankAcct>();
        List<BankAcctPo> poList = bankAcctRepository.findBankAcctByUserId(userId);
        if (poList != null && !poList.isEmpty()) {
            for (BankAcctPo po : poList) {
                list.add(ConverterService.convert(po, BankAcct.class));
            }
        }
        return list;
    }

    @Override
    public List<BankAcct> findBankAcctByUserId(String userId) {
        if (!StringUtils.isNotBlank(userId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED, "用户编号不能为空");
        }
        List<BankAcct> list = new ArrayList<BankAcct>();
        List<BankAcctPo> poList = bankAcctRepository.findBankAcctByUserId(userId);
        if(null == poList || poList.size() == 0){
        	throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST, "用户银行资料信息不存在");
        }
        for (BankAcctPo po : poList) {
            list.add(ConverterService.convert(po, BankAcct.class));
        }
        return list;
    }

    @Override
    public List<BankAcct> findBankAcctByUserIdAndSignedFlg(String userId, String signedFlg) {
        if (StringUtils.isBlank(userId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED, "用户编号不能为空");
        }
        if (StringUtils.isBlank(signedFlg)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED, "用户签约类型不能为空");
        }
        List<BankAcct> list = new ArrayList<BankAcct>();
        List<BankAcctPo> poList = bankAcctRepository.findByUserIdAndSignedFlg(userId, signedFlg);
        for (BankAcctPo po : poList) {
            list.add(ConverterService.convert(po, BankAcct.class));
        }
        return list;
    }

    @Override
    public BankAcct findBankAcctByBnkAcctNo(String bankAcctNo) {
        if (StringUtils.isBlank(bankAcctNo)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED, "银行卡编号不能为空");
        }
        BankAcctPo bankAcct = bankAcctRepository.findBankAcctByBnkAcctNo(bankAcctNo);
        if(null == bankAcct ){
        	throw new BizServiceException(EErrorCode.ACCT_BANKINFO_NOT_EXIST, "用户银行资料信息不存在");
        }
        return ConverterService.convert(bankAcct, BankAcct.class);
    }

}
