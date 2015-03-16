
/*
 * Project Name: kmfex
 * File Name: UserProtocolServiceImpl.java
 * Class Name: UserProtocolServiceImpl
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

import com.hengxin.platform.bnkdocking.dto.biz.req.ProtocolReq;
import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.rsp.SubAcctAmtRsp;
import com.hengxin.platform.fund.exception.AcctNotExistException;
import com.hengxin.platform.fund.service.BankAcctAgreementManageService;
import com.hengxin.platform.fund.webservice.UserProtocolService;


/**
 * Class Name: UserProtocolServiceImpl
 * Description: TODO
 * @author congzhou
 *
 */
@Service
@WebService(endpointInterface = "com.hengxin.platform.fund.webservice.UserProtocolService")
public class UserProtocolServiceImpl implements UserProtocolService{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProtocolServiceImpl.class);

    @Autowired
    BankAcctAgreementManageService bankAcctAgreementManageService;
    
    private final static String[] searchStr= {EErrorCode.EXISTS_UN_END_RECHARGE_APPL.getErrorCode(),
        EErrorCode.EXISTS_UN_END_WDRW_APPL.getErrorCode(),
        EErrorCode.EXISTS_UN_END_TRSF_APPL.getErrorCode(),
        EErrorCode.EXISTS_UN_END_RVS_APPL.getErrorCode()};

    @Override
    public EBnkErrorCode signBankAcct(ProtocolReq protocolReq) {
        try{
            bankAcctAgreementManageService.signBankAcct(protocolReq);
        } catch (BizServiceException e) {
            if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_NOT_EXIST.getErrorCode())) {
                return EBnkErrorCode.ERROR_2009;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_NO_EXIST.getErrorCode())) {
                return EBnkErrorCode.ERROR_1383;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_CAN_NOT_SIGNED.getErrorCode())) {
                return EBnkErrorCode.ERROR_1381;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_ALREADY_SIGNED.getErrorCode())) {
                return EBnkErrorCode.ERROR_2037;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_PWD_NOT_MATCH.getErrorCode())) {
                return EBnkErrorCode.ERROR_2085;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ID_NOT_MATCH.getErrorCode())) {
                return EBnkErrorCode.ERROR_1309;
            } else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_MARKET_NOT_OPEN.getErrorCode())) {
                return EBnkErrorCode.ERROR_2065;
            } else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.NAME_NOT_MATCH.getErrorCode())) {
                return EBnkErrorCode.ERROR_2095;
            } else if(equalsWithAny(e.getError().getErrorCode(), searchStr)) {
                return EBnkErrorCode.ERROR_2133;
            } else {
                return EBnkErrorCode.ERROR_2125;
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return EBnkErrorCode.ERROR_2125;
        }
        return EBnkErrorCode.SUCCESS; 
    }

    @Override
    public EBnkErrorCode unSignBankAcct(ProtocolReq protocolReq) {
        try{
            bankAcctAgreementManageService.unSignBankAcct(protocolReq);
        } catch (BizServiceException e) {
            if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_NOT_EXIST.getErrorCode())) {
                return EBnkErrorCode.ERROR_2009;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_NO_EXIST.getErrorCode())) {
                return EBnkErrorCode.ERROR_1383;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_CAN_NOT_SIGNED.getErrorCode())) {
                return EBnkErrorCode.ERROR_1381;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.BANK_ACCT_ALREADY_UNSIGNED.getErrorCode())) {
                return EBnkErrorCode.ERROR_2038;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_PWD_NOT_MATCH.getErrorCode())) {
                return EBnkErrorCode.ERROR_2085;
            } else if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ID_NOT_MATCH.getErrorCode())) {
                return EBnkErrorCode.ERROR_1309;
            } else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_MARKET_NOT_OPEN.getErrorCode())) {
                return EBnkErrorCode.ERROR_2065;
            } else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.NAME_NOT_MATCH.getErrorCode())) {
                return EBnkErrorCode.ERROR_2095;
            } else if(equalsWithAny(e.getError().getErrorCode(), searchStr)) {
                return EBnkErrorCode.ERROR_2134;
            } else {
                return EBnkErrorCode.ERROR_2125;
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return EBnkErrorCode.ERROR_2125;
        }
        return EBnkErrorCode.SUCCESS; 
    }

    @Override
    public SubAcctAmtRsp querySubAcctAmt(String acctNo) throws BizServiceException, AcctNotExistException {
        SubAcctAmtRsp rsp = new SubAcctAmtRsp();
        try {
            rsp = bankAcctAgreementManageService.querySubAcctAmt(acctNo);
        } catch (BizServiceException e) {
            if (StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_NOT_EXIST.getErrorCode())) {
                rsp.setErrorCode(EBnkErrorCode.ERROR_2009);
            } else if(StringUtils.equalsIgnoreCase(e.getError().getErrorCode(),
                    EErrorCode.ACCT_MARKET_NOT_OPEN.getErrorCode())) {
                rsp.setErrorCode(EBnkErrorCode.ERROR_2065);
            } else {
                rsp.setErrorCode(EBnkErrorCode.ERROR_2125);
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            rsp.setErrorCode(EBnkErrorCode.ERROR_2125);
        }
        return rsp;
    }
    
    /**
     * 
    * Description: stringUtil method
    *
    * @param str
    * @param searchStr
    * @return
     */
    private boolean equalsWithAny(String str,String[] searchStr) {
        for(String s : searchStr) {
            if(StringUtils.equalsIgnoreCase(str, s))
                return true;
        }
        return false;
    }

}
