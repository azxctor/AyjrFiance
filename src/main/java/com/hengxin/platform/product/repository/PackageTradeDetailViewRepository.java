
/*
* Project Name: kmfex
* File Name: PackageTradeDetailViewRepository.java
* Class Name: PackageTradeDetailViewRepository
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
	
package com.hengxin.platform.product.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.product.domain.PackageTradeDetailView;


/**
 * Class Name: PackageTradeDetailViewRepository
 * Description: TODO
 * @author congzhou
 *
 */
@Repository
public interface PackageTradeDetailViewRepository extends PagingAndSortingRepository<PackageTradeDetailView, String>,
JpaSpecificationExecutor<PackageTradeDetailView> {

}
