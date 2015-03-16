
/*
* Project Name: kmfex_bnk
* File Name: SystemService.java
* Class Name: SystemService
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
import javax.jws.WebService;
import javax.xml.bind.JAXBException;

import com.hengxin.platform.common.exception.BizServiceException;


/**
 * Class Name: SystemService
 * Description: 系统类service
 * @author congzhou
 *
 */
@WebService
public interface SystemService {
    
    /**
     * 申请密钥
     * Description: TODO
     *
     * @return
     * @throws JAXBException 
     */
    @WebMethod
    public boolean applySessionKey() throws BizServiceException;
}
