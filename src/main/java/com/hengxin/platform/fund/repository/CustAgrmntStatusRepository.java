
/*
* Project Name: kmfex-platform
* File Name: CustAgrmntStatusRepository.java
* Class Name: CustAgrmntStatusRepository
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
	
package com.hengxin.platform.fund.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.ReconAgrmntStePo;


/**
 * Class Name: CustAgrmntStatusRepository
 * Description: TODO
 * @author congzhou
 *
 */
@Repository
public interface CustAgrmntStatusRepository extends PagingAndSortingRepository<ReconAgrmntStePo,String>,JpaSpecificationExecutor<ReconAgrmntStePo>{

}
