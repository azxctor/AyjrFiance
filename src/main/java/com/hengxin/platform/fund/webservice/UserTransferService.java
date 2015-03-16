
/*
* Project Name: kmfex
* File Name: UserTransferService.java
* Class Name: UserTransferService
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

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.dto.biz.req.UserRechargeReq;
import com.hengxin.platform.fund.dto.biz.req.UserWithdrawalReq;
import com.hengxin.platform.fund.dto.biz.rsp.FundTransferRsp;



/**
 * Class Name: UserTransferService
 * Description: TODO
 * @author congzhou
 *
 */
@WebService
public interface UserTransferService {

    /**
     * 处理签约会员在银行发起的充值请求 
     * 
     * @param req
     */
    @WebMethod
    public FundTransferRsp doSignedUserOnBankRecharge(@WebParam(name = "req")UserRechargeReq req)
            throws BizServiceException;
    
    /**
     * 处理签约用户在银行的提现操作 
     * 
     * @param appl
     */
    @WebMethod
    public FundTransferRsp doSignUserWithdrawalOnBank(@WebParam(name = "req")UserWithdrawalReq req)
            throws BizServiceException;


}
