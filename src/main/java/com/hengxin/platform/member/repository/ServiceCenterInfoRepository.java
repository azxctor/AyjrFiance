/*
 * Project Name: kmfex-platform
 * File Name: ServiceCenterInfoRepository.java
 * Class Name: ServiceCenterInfoRepository
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

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.member.domain.ServiceCenterInfo;

/**
 * Class Name: ServiceCenterInfoRepository Description: TODO
 * 
 * @author chunlinwang
 * 
 */

public interface ServiceCenterInfoRepository extends PagingAndSortingRepository<ServiceCenterInfo, String>, JpaSpecificationExecutor<ServiceCenterInfo> {
	
    ServiceCenterInfo findByUserId(String userId);
    
    List<ServiceCenterInfo> findAll();
}