
/*
* Project Name: kmfex-platform
* File Name: FeeRepository.java
* Class Name: FeeRepository
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

import org.springframework.data.jpa.repository.JpaRepository;

import com.hengxin.platform.product.domain.Fee;


/**
 * Class Name: FeeRepository
 * Description: TODO
 * @author yingchangwang
 *
 */

public interface FeeRepository extends JpaRepository<Fee, Integer> {

}
