
/*
* Project Name: kmfex-platform
* File Name: TransferService.java
* Class Name: TransferService
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

import com.hengxin.platform.bnkdocking.dto.biz.req.TransferReq;
import com.hengxin.platform.bnkdocking.dto.biz.rsp.TransferRsp;
import com.hengxin.platform.common.exception.BizServiceException;


/**
 * Class Name: TransferService
 * Description: 转账类service
 * @author congzhou
 *
 */
@WebService
public interface TransferService {

    /**
     * 
    * 银行转交易所(交易所发起)
    * @param transferReq
    * @return
    * @throws JAXBException
     */
    @WebMethod
    public TransferRsp bankToExchangeFE(@WebParam(name="transferReq")TransferReq transferReq) throws BizServiceException;
    
    
    /**
     * 交易所转银行(交易所发起)
    *
    * @param transferReq
    * @return
    * @throws JAXBException
     */
    @WebMethod
    public TransferRsp exchangeToBankFE(@WebParam(name="transferReq")TransferReq transferReq) throws BizServiceException;

}

