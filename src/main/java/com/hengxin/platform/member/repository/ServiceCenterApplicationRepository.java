/*
 * Project Name: kmfex-platform
 * File Name: ServiceCentreRepository.java
 * Class Name: ServiceCentreRepository
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.member.domain.ServiceCenterApplication;
import com.hengxin.platform.member.enums.EApplicationStatus;

/**
 * Class Name: ServiceCentreRepository Description: TODO
 * 
 * @author chunlinwang
 * 
 */
public interface ServiceCenterApplicationRepository extends
	PagingAndSortingRepository<ServiceCenterApplication, String>,
	JpaSpecificationExecutor<ServiceCenterApplication> {

    Page<ServiceCenterApplication> findByUserId(String userId, Pageable pageable);

    Page<ServiceCenterApplication> findByUserIdAndStatusNot(String userId, EApplicationStatus status, Pageable pageable);

}
