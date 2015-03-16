
/*
 * Project Name: kmfex
 * File Name: UserTransferServiceImpl.java
 * Class Name: UserTransferServiceImpl
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

package com.hengxin.platform.fund.webservice.impl;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.UserRechargeReq;
import com.hengxin.platform.fund.dto.biz.req.UserWithdrawalReq;
import com.hengxin.platform.fund.dto.biz.rsp.FundTransferRsp;
import com.hengxin.platform.fund.service.UserRechargeService;
import com.hengxin.platform.fund.service.UserWithdrawalService;
import com.hengxin.platform.fund.webservice.UserTransferService;


/**
 * Class Name: UserTransferServiceImpl
 * Description: TODO
 * @author congzhou
 *
 */
@Service
@WebService(endpointInterface = "com.hengxin.platform.fund.webservice.UserTransferService")
public class UserTransferServiceImpl implements UserTransferService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTransferServiceImpl.class);
    @Autowired
    UserRechargeService userRechargeService;
    @Autowired
    UserWithdrawalService userWithdrawalService;

    @Override
    public FundTransferRsp doSignedUserOnBankRecharge(UserRechargeReq req) throws BizServiceException {
        FundTransferRsp rsp = new FundTransferRsp();
        rsp.setAmount(req.getAmount());
        rsp.setBankSerial(req.getBkSerial());
        rsp.setBnkAcctNo(req.getBnkAcctNo());
        rsp.setAccotNo(req.getAcctNo());
        rsp.setJnlNo(req.getJnlNo());
        try {
            rsp = userRechargeService.processSignedUserOnBankRecharge(req);
        } catch(BizServiceException e) {
            if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_NOT_EXIST.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2009.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_2009.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.FUND_ACCT_CAN_NOT_PAY.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2031.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_2031.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.USER_NOT_EXIST.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2085.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_2085.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_NO_EXIST.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_1383.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_1383.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_CONFILICT_WITH_ACCT.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_1385.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_1385.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.FUND_ACCT_REVERSE_FAILED.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_1222.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_1222.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_MARKET_NOT_OPEN.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2065.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2065.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_ALREADY_UNSIGNED.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2038.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2038.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ID_NOT_MATCH.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2093.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2093.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.NAME_NOT_MATCH.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2095.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2095.getMsg());
            }else {
                rsp.setRespCode(EBnkErrorCode.ERROR_2125.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2125.getMsg());
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            rsp.setRespCode(EBnkErrorCode.ERROR_2125.getMsg_id()); 
            rsp.setErrMsg(EBnkErrorCode.ERROR_2125.getMsg());
        }
        return rsp;
    }


    @Override
    public FundTransferRsp doSignUserWithdrawalOnBank(UserWithdrawalReq req) throws BizServiceException {
        FundTransferRsp rsp = new FundTransferRsp();
        rsp.setAccotNo(req.getAcctNo());
        rsp.setAmount(req.getAmount());
        rsp.setBnkAcctNo(req.getBnkAcctNo());
        rsp.setBankSerial(req.getBkSerial());
        rsp.setJnlNo(req.getJnlNo());
        try {
            rsp = userWithdrawalService.processSignUserWithdrawalOnBank(req);
        } catch (BizServiceException e) {
            if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_NOT_EXIST.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2009.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_2009.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.FUND_ACCT_CAN_NOT_PAY.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2031.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_2031.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.USER_NOT_EXIST.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2085.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_2085.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_NO_EXIST.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_1383.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_1383.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_CONFILICT_WITH_ACCT.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_1385.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_1385.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.FUND_ACCT_REVERSE_FAILED.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_1222.getMsg_id());
                rsp.setErrMsg(EBnkErrorCode.ERROR_1222.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_MARKET_NOT_OPEN.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2065.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2065.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_ALREADY_UNSIGNED.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2038.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2038.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ID_NOT_MATCH.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2093.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2093.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.NAME_NOT_MATCH.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2095.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2095.getMsg());
            }else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_PWD_NOT_MATCH.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2085.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2085.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.FUND_ACCT_REVERSE_FAILED.getErrorCode())){
                rsp.setRespCode(EBnkErrorCode.ERROR_2111.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2111.getMsg());
            }else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_NOT_ENOUGH_CASHABLE_BAL.getErrorCode())) {
                rsp.setRespCode(EBnkErrorCode.ERROR_2003.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2003.getMsg());
            }else {
                rsp.setRespCode(EBnkErrorCode.ERROR_2125.getMsg_id()); 
                rsp.setErrMsg(EBnkErrorCode.ERROR_2125.getMsg());
            }
        } catch (Exception e) {
            LOGGER.error("exception",e);
            rsp.setRespCode(EBnkErrorCode.ERROR_2125.getMsg_id()); 
            rsp.setErrMsg(EBnkErrorCode.ERROR_2125.getMsg());
        }
        return rsp;
    }

}
