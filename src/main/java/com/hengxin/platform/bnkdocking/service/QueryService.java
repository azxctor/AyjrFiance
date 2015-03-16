
/*
* Project Name: kmfex-platform-trunk
* File Name: QueryService.java
* Class Name: QueryService
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
	
package com.hengxin.platform.bnkdocking.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;

import com.hengxin.platform.bnkdocking.dto.biz.req.QueryReq;
import com.hengxin.platform.bnkdocking.dto.biz.rsp.QueryRsp;
import com.hengxin.platform.common.exception.BizServiceException;


/**
 * Class Name: QueryService
 * Description: 查询类service
 * @author congzhou
 *
 */
@WebService
public interface QueryService {
    /**
     * 查询银行账户余额
    * Description: TODO
    *
    * @param queryReq
    * @return
    * @throws JAXBException
     */
    @WebMethod
    QueryRsp queryBankAccountAmount(@WebParam(name="queryReq")QueryReq queryReq) throws BizServiceException; 
    
    /**
     * 查询交易账户余额
    * Description: TODO
    *
    * @param queryReq
    * @return
    * @throws JAXBException
     */
    @WebMethod
    QueryRsp queryExchangeAccountAmount(@WebParam(name="queryReq")QueryReq queryReq) throws BizServiceException;
    
}
