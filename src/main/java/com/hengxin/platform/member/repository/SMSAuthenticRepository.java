
/*
* Project Name: kmfex
* File Name: SMSAuthenticRepository.java
* Class Name: SMSAuthenticRepository
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
	
package com.hengxin.platform.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengxin.platform.member.domain.SMSAuthentic;


/**
 * Class Name: SMSAuthenticRepository
 * Description: TODO
 * @author congzhou
 *
 */

public interface SMSAuthenticRepository extends JpaRepository<SMSAuthentic, String>, JpaSpecificationExecutor<SMSAuthentic>{

    SMSAuthentic findByMobile(String mobile);
}
