
/*
* Project Name: kmfex
* File Name: UserProtocolService.java
* Class Name: UserProtocolService
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
	
package com.hengxin.platform.fund.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.hengxin.platform.bnkdocking.dto.biz.req.ProtocolReq;
import com.hengxin.platform.bnkdocking.enums.EBnkErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.rsp.SubAcctAmtRsp;
import com.hengxin.platform.fund.exception.AcctNotExistException;


/**
 * Class Name: UserProtocolService
 * Description: TODO
 * @author congzhou
 *
 */
@WebService
public interface UserProtocolService {
    
    /**
     * 通过银商接口签约用户 
     * 
     * @param acctNo
     * @param bankAcctNo
     * @param opratorId
     * @param workDate
     */
    @WebMethod
    public EBnkErrorCode signBankAcct(@WebParam(name="protocolReq")ProtocolReq protocolReq);
    
    /**
     * 通过银商接口解约用户 Description: TODO
     * 
     * @param acctNo
     * @param bankAcctNo
     * @param opratorId
     * @param workDate
     */
    @WebMethod
    public EBnkErrorCode unSignBankAcct(@WebParam(name="protocolReq")ProtocolReq protocolReq);
    
    /**
     * 获取签约会员账户可用余额和可提现余额 Description: TODO
     * 
     * @param acctNo
     * @return
     * @throws AmtParamInvalidException
     * @throws SubAcctNotExistException
     * @throws AcctNotExistException
     */
    @WebMethod
    public SubAcctAmtRsp querySubAcctAmt(@WebParam(name="acctNo")String acctNo)
            throws BizServiceException, AcctNotExistException;

}
