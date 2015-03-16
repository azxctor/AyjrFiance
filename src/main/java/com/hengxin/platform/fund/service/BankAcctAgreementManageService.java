
/*
* Project Name: kmfex
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
	
package com.hengxin.platform.fund.service;

import com.hengxin.platform.bnkdocking.dto.biz.req.ProtocolReq;
import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.rsp.BankAcctAmtRsp;
import com.hengxin.platform.fund.dto.biz.rsp.SubAcctAmtRsp;
import com.hengxin.platform.fund.exception.AcctNotExistException;


/**
 * Class Name: BankAcctAgreementManageService.
 * Description: TODO
 * @author congzhou
 *
 */

public interface BankAcctAgreementManageService {
    /**
     * 通过银商接口签约用户 .
     * 
     * @param acctNo
     * @param bankAcctNo
     * @param opratorId
     * @param workDate
     */
    EBnkErrorCode signBankAcct(ProtocolReq protocolReq);
    
    /**
     * 通过银商接口解约用户 .
     * 
     * @param acctNo
     * @param bankAcctNo
     * @param opratorId
     * @param workDate
     */
    EBnkErrorCode unSignBankAcct(ProtocolReq protocolReq);
    
    /**
     * 获取签约会员账户可用余额和可提现余额 .
     * 
     * @param acctNo
     * @return
     * @throws AmtParamInvalidException
     * @throws SubAcctNotExistException
     * @throws AcctNotExistException
     */
    SubAcctAmtRsp querySubAcctAmt(String acctNo)
            throws BizServiceException, AcctNotExistException;
    
    /**
     * 通过银商接口获取银行卡可用余额和可提现余额.
     * 
     * @param bankAcctNo
     * @param acctNo
     * @param bankSerial
     * @return
     * @throws BizServiceException
     */
    BankAcctAmtRsp queryBankAcctAmt(String bankAcctNo, String acctNo)
            throws BizServiceException;
    
    
    /**
    * Description: 通过银商接口获取交易所账户可用余额和可提现余额.
    *
    * @param bankAcctNo
    * @param acctNo
    * @return
    * @throws BizServiceException
    */
    	
    BankAcctAmtRsp queryExchangeAcctAmt(String bankAcctNo, String acctNo)throws BizServiceException;
}
