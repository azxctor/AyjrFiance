
/*
* Project Name: kmfex-platform-trunk
* File Name: ProtocolService.java
* Class Name: ProtocolService
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

import com.hengxin.platform.bnkdocking.dto.biz.req.ProtocolReq;
import com.hengxin.platform.bnkdocking.dto.biz.rsp.ProtocolRsp;
import com.hengxin.platform.common.exception.BizServiceException;


/**
 * Class Name: ProtocolService
 * Description: 协议类service
 * @author congzhou
 *
 */
@WebService
public interface ProtocolService {
    /**
     * 指定银商转账银行请求
     * Description: TODO
     *
     * @return
     * @throws JAXBException 
     */
    @WebMethod
    ProtocolRsp assignTransferBank(@WebParam(name="protocolReq")ProtocolReq protocolReq) throws BizServiceException;
}


