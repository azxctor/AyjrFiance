/*
 * Project Name: kmfex-platform
 * File Name: MemberRepository.java
 * Class Name: MemberRepository
 *
 * Copyright 2014 KMFEX Inc
 *
 * 
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.member.domain.ProductProviderApplication;
import com.hengxin.platform.member.enums.EApplicationStatus;

/**
 * Class Name: ProductProviderApplicationRepository
 * 
 * @author Ryan
 * 
 */

public interface ProductProviderApplicationRepository extends
PagingAndSortingRepository<ProductProviderApplication, String>,
JpaSpecificationExecutor<ProductProviderApplication> {

    Page<ProductProviderApplication> findByUserId(String userId, Pageable pageable);

    Page<ProductProviderApplication> findByUserIdAndStatusNot(String userId, EApplicationStatus status,
            Pageable pageable);

}
