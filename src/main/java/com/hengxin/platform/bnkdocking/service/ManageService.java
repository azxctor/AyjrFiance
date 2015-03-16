
/*
* Project Name: kmfex_bnk
* File Name: ManageService.java
* Class Name: ManageService
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

import com.hengxin.platform.common.exception.BizServiceException;


/**
 * Class Name: ManageService
 * Description: 管理类service
 * @author congzhou
 *
 */
@WebService
public interface ManageService {

    /**
     * 交易日初签到
     *
     * @return
     * @throws JAXBException 
     */
    @WebMethod
    public boolean dailySign(@WebParam(name="date")String date) throws BizServiceException;

}
