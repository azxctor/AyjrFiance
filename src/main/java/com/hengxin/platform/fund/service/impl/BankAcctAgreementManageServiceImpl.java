/*
 * Project Name: kmfex-platform
 * File Name: BankAcctAgreementManageService.java
 * Class Name: BankAcctAgreementManageService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.fund.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.bnkdocking.dto.biz.req.ProtocolReq;
import com.hengxin.platform.bnkdocking.dto.biz.req.QueryReq;
import com.hengxin.platform.bnkdocking.dto.biz.rsp.QueryRsp;
import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;
import com.hengxin.platform.bnkdocking.service.QueryService;
import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.EnumHelper;
import com.hengxin.platform.fund.dto.biz.rsp.BankAcctAmtRsp;
import com.hengxin.platform.fund.dto.biz.rsp.SubAcctAmtRsp;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.BankAcctPo;
import com.hengxin.platform.fund.enums.EAcctType;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.BankAcctRepository;
import com.hengxin.platform.fund.service.AcctService;
import com.hengxin.platform.fund.service.BankAcctAgreementManageService;
import com.hengxin.platform.fund.service.FundAcctBalService;
import com.hengxin.platform.fund.service.FundAcctCashPoolChangeService;
import com.hengxin.platform.fund.service.FundUnEndApplService;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.domain.FinancierInfo;
import com.hengxin.platform.member.domain.Member;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.service.UserService;

/**
 * Class Name: BankAcctAgreementManageService Description: TODO
 * 
 * @author tingwang
 * 
 */
@Service
public class BankAcctAgreementManageServiceImpl implements BankAcctAgreementManageService{

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAcctAgreementManageServiceImpl.class);
    
	@Autowired
	AcctService acctService;
	@Autowired
    UserService userService;
	@Autowired
	QueryService queryService;
	@Autowired
	AcctRepository acctRepository;
	@Autowired
	BankAcctRepository bankAcctRepository;
	@Autowired
	FundAcctBalService fundAcctBalService;
	@Autowired
    MemberService memberService;
	@Autowired
	FundUnEndApplService fundUnEndApplService;
	@Autowired
	FundAcctCashPoolChangeService fundAcctCashPoolChangeService;

	/**
	 * 通过银商接口签约用户 Description: TODO
	 * 
	 * @param acctNo
	 * @param bankAcctNo
	 * @param opratorId
	 * @param workDate
	 */
	@Transactional
	public EBnkErrorCode signBankAcct(ProtocolReq protocolReq) {
	    
		String acctNo = protocolReq.getFundAccount();
		String bankAcctNo = protocolReq.getBankAccount();
		String opratorId = CommonBusinessUtil.getBankInterfaceAccountNo();
	    Date workDate = CommonBusinessUtil.getCurrentWorkDate();
	    
	    if(!CommonBusinessUtil.isMarketOpen()) {
	        throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
	    }
		
        AcctPo acct = acctRepository.findAcctByAcctNoAndAcctType(acctNo,
				EAcctType.DEBT.getCode());
		if (acct == null) {
		    throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
		}
		
		String userId = acct.getUserId();
		User user = userService.getUserByUserId(userId);
		if (user == null) {
            throw new BizServiceException(EErrorCode.USER_NOT_EXIST);
        }
		LOGGER.debug("userId:"+user.getUserId());
		LOGGER.debug("password:"+protocolReq.getPinBlock());
		if (!userService.isValidPassward(protocolReq.getPinBlock(), user.getUserId())) {
            throw new BizServiceException(EErrorCode.ACCT_PWD_NOT_MATCH);
        }
		
		if(StringUtils.equals(protocolReq.getiDType(), "P01")) {
            if(!user.getType().equals(EUserType.PERSON)) {
                throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
            }
        } else {
            if(!user.getType().equals(EUserType.ORGANIZATION)) {
                throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
            }
        }
		
		// 判断是否存在未结束的申请，存在则抛出异常
		fundUnEndApplService.checkUnEndAppl(userId);
		
		String iDNo = null;
		switch (user.getType()) {
        case NULL:
            break;
        case ORGANIZATION:
            Agency agency = memberService.getAgencyByUserId(user.getUserId());
            if(null != agency) {
                iDNo = agency.getOrgNo();
            } else {
                FinancierInfo fncr = memberService.getFinancierById(user
                        .getUserId());
                iDNo = fncr.getOrgNumber();
            }
            break;
        case PERSON:
            Member member = memberService.getMemberByUserId(user.getUserId());
            iDNo = member.getPersonIdCardNumber();
            break;
        default:
            break;
        }
		if(!StringUtils.equals(protocolReq.getiDNo(), iDNo)) {
		    throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
		}
		
		BankAcctPo bankAcct = bankAcctRepository.findByBnkAcctNoAndUserId(
				bankAcctNo, acct.getUserId());
		if (bankAcct == null) {
		    throw new BizServiceException(EErrorCode.BANK_ACCT_NO_EXIST);
		}
		if(!StringUtils.equals(protocolReq.getCustName(), bankAcct.getBnkAcctName())) {
            throw new BizServiceException(EErrorCode.NAME_NOT_MATCH);
        }
		if(!StringUtils.equals(bankAcct.getBnkCd(), EBankType.CMB.getCode())) {
		    throw new BizServiceException(EErrorCode.BANK_CAN_NOT_SIGNED);
        }
		if (StringUtils
				.equals(bankAcct.getSignedFlg(), EFlagType.YES.getCode())) {
		    throw new BizServiceException(EErrorCode.BANK_ACCT_ALREADY_SIGNED);
		}
		bankAcct.setSignedFlg(EFlagType.YES.getCode());
		bankAcct.setSignedDt(workDate);
		bankAcct.setLastMntOpid(opratorId);
		bankAcct.setLastMntTs(new Date());
		bankAcctRepository.save(bankAcct);
		
		ECashPool fromPool = EnumHelper.translate(ECashPool.class, acct.getCashPool());
		ECashPool toPool = ECashPool.CMB_SPECIAL;
		fundAcctCashPoolChangeService.changePoolTreasurerAcctBal(
				IdUtil.produce(), acct.getAcctNo(), fromPool, 
				toPool, "会员第三方存款协议签约，原资金池为"+fromPool.getText()
				+", 新资金池为"+toPool.getText(), 
				acct.getUserId(), opratorId, workDate);
		return EBnkErrorCode.SUCCESS;
	}

	/**
	 * 通过银商接口解约用户 Description: TODO
	 * 
	 * @param acctNo
	 * @param bankAcctNo
	 * @param opratorId
	 * @param workDate
	 */
	@Transactional
	public EBnkErrorCode unSignBankAcct(ProtocolReq protocolReq) {
	    
	    String acctNo = protocolReq.getFundAccount();
        String bankAcctNo = protocolReq.getBankAccount();
        String opratorId = CommonBusinessUtil.getBankInterfaceAccountNo();
        Date workDate = CommonBusinessUtil.getCurrentWorkDate();
        
        if(!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
        }
        
		AcctPo acct = acctRepository.findAcctByAcctNoAndAcctType(acctNo,
				EAcctType.DEBT.getCode());
		if (acct == null) {
            throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
        }
		
		String userId = acct.getUserId();
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            throw new BizServiceException(EErrorCode.USER_NOT_EXIST);
        }
        if (!userService.isValidPassward(protocolReq.getPinBlock(), user.getUserId())) {
            throw new BizServiceException(EErrorCode.ACCT_PWD_NOT_MATCH);
        }
        
        if(StringUtils.equals(protocolReq.getiDType(), "P01")) {
            if(!user.getType().equals(EUserType.PERSON)) {
                throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
            }
        } else {
            if(!user.getType().equals(EUserType.ORGANIZATION)) {
                throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
            }
        }
		
		// 判断是否存在未结束的申请，存在则抛出异常
		fundUnEndApplService.checkUnEndAppl(userId);
		
        String iDNo = null;
        switch (user.getType()) {
        case NULL:
            break;
        case ORGANIZATION:
            Agency agency = memberService.getAgencyByUserId(user.getUserId());
            if(null != agency) {
                iDNo = agency.getOrgNo();
            } else {
                FinancierInfo fncr = memberService.getFinancierById(user
                        .getUserId());
                iDNo = fncr.getOrgNumber();
            }
            break;
        case PERSON:
            Member member = memberService.getMemberByUserId(user.getUserId());
            iDNo = member.getPersonIdCardNumber();
            break;
        default:
            break;
        }
        if(!StringUtils.equals(protocolReq.getiDNo(), iDNo)) {
            throw new BizServiceException(EErrorCode.ID_NOT_MATCH);
        }
		
        BankAcctPo bankAcct = bankAcctRepository.findByBnkAcctNoAndUserId(
                bankAcctNo, acct.getUserId());
        if (bankAcct == null) {
            throw new BizServiceException(EErrorCode.BANK_ACCT_NO_EXIST);
        }
        if(!StringUtils.equals(protocolReq.getCustName(), bankAcct.getBnkAcctName())) {
            throw new BizServiceException(EErrorCode.NAME_NOT_MATCH);
        }
        if(!StringUtils.equals(bankAcct.getBnkCd(), EBankType.CMB.getCode())) {
            throw new BizServiceException(EErrorCode.BANK_CAN_NOT_SIGNED);
        }
        if (StringUtils
                .equals(bankAcct.getSignedFlg(), EFlagType.NO.getCode())) {
            throw new BizServiceException(EErrorCode.BANK_ACCT_ALREADY_UNSIGNED);
        }
		bankAcct.setSignedFlg(EFlagType.NO.getCode());
		bankAcct.setTerminDt(workDate);
		bankAcct.setLastMntOpid(opratorId);
		bankAcct.setLastMntTs(workDate);
		bankAcctRepository.save(bankAcct);
		
		ECashPool fromPool = EnumHelper.translate(ECashPool.class, acct.getCashPool());
		ECashPool toPool = ECashPool.CMB_COMMON;		
		if(StringUtils.equalsIgnoreCase(EBankType.ICBC.getCode(), bankAcct.getBnkCd())){
			toPool = ECashPool.ICBC_COMMON;
		}
		fundAcctCashPoolChangeService.changePoolTreasurerAcctBal(
				IdUtil.produce(), acct.getAcctNo(), fromPool,
				toPool, "会员第三方存款协议解约，原资金池为"+fromPool.getText()
				+", 新资金池为"+toPool.getText(), 
				acct.getUserId(), opratorId, workDate);
		return EBnkErrorCode.SUCCESS;
	}

	/**
	 * 获取签约会员账户可用余额和可提现余额 Description: TODO
	 * 
	 * @param acctNo
	 * @return
	 * @throws AmtParamInvalidException
	 * @throws SubAcctNotExistException
	 * @throws AcctNotExistException
	 */
	@Transactional
	public SubAcctAmtRsp querySubAcctAmt(String acctNo)
			throws BizServiceException {
	    if(!CommonBusinessUtil.isMarketOpen()) {
            throw new BizServiceException(EErrorCode.ACCT_MARKET_NOT_OPEN);
        }
		SubAcctAmtRsp acctAmtRsp = new SubAcctAmtRsp();
		AcctPo acct = acctRepository.findAcctByAcctNoAndAcctType(acctNo,
				EAcctType.DEBT.getCode());
		if (acct == null) {
		    throw new BizServiceException(EErrorCode.ACCT_NOT_EXIST);
		}
		BigDecimal avlAmt = fundAcctBalService.getUserCurrentAcctAvlBal(acct.getUserId());
		acctAmtRsp.setAvailableAmt(avlAmt);
		BigDecimal cashableAmt = fundAcctBalService.getUserCashableAmt(acct.getUserId());
		acctAmtRsp.setCashableAmt(cashableAmt);
		acctAmtRsp.setErrorCode(EBnkErrorCode.SUCCESS);
		return acctAmtRsp;
	}

	/**
	 * 通过银商接口获取银行卡可用余额和可提现余额 Description: TODO
	 * 
	 * @param bankAcctNo
	 * @param acctNo
	 * @param bankSerial
	 * @return
	 * @throws JAXBException
	 * @throws BizServiceException
	 */
	public BankAcctAmtRsp queryBankAcctAmt(String bankAcctNo, String acctNo)
			throws BizServiceException {
		QueryReq queryReq = new QueryReq();
		queryReq.setBankAccount(bankAcctNo);
		queryReq.setFundAccount(acctNo);
		QueryRsp rsp = queryService.queryBankAccountAmount(queryReq);
		BankAcctAmtRsp amtRsp = new BankAcctAmtRsp();
		if (rsp.getErrMsg() == null || StringUtils.isBlank(rsp.getErrMsg())) {
			BigDecimal fundUse = BigDecimal.ZERO;
			BigDecimal fundbal = BigDecimal.ZERO;
			if (rsp.getFundUse() != null) {
				fundUse = AmtUtils.processNegativeAmt(
						BigDecimal.valueOf(Double.valueOf(rsp.getFundUse())),
						BigDecimal.ZERO);
			}
			if (rsp.getFundBal() != null) {
				fundbal = AmtUtils.processNegativeAmt(
						BigDecimal.valueOf(Double.valueOf(rsp.getFundBal())),
						BigDecimal.ZERO);
			}
			amtRsp.setAvailableAmt(fundUse);
			amtRsp.setCashableAmt(fundbal);
		} else {
			amtRsp.setErrMsg(rsp.getErrMsg());
		}
		return amtRsp;
	}

    	
    @Override
    public BankAcctAmtRsp queryExchangeAcctAmt(String bankAcctNo, String acctNo) throws BizServiceException {
        QueryReq queryReq = new QueryReq();
        queryReq.setBankAccount(bankAcctNo);
        queryReq.setFundAccount(acctNo);
        QueryRsp rsp = queryService.queryExchangeAccountAmount(queryReq);
        BankAcctAmtRsp amtRsp = new BankAcctAmtRsp();
        if (StringUtils.equals(rsp.getRespCode(), "0000")) {
            BigDecimal fundUse = BigDecimal.ZERO;
            BigDecimal fundbal = BigDecimal.ZERO;
            if (rsp.getFundUse() != null) {
                fundUse = AmtUtils.processNegativeAmt(
                        BigDecimal.valueOf(Double.valueOf(rsp.getFundUse())),
                        BigDecimal.ZERO);
            }
            if (rsp.getFundBal() != null) {
                fundbal = AmtUtils.processNegativeAmt(
                        BigDecimal.valueOf(Double.valueOf(rsp.getFundBal())),
                        BigDecimal.ZERO);
            }
            amtRsp.setAvailableAmt(fundUse);
            amtRsp.setCashableAmt(fundbal);
        } else {
            amtRsp.setErrMsg(rsp.getErrMsg());
        }
        return amtRsp;
    }

}
