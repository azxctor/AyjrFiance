/*
 * Project Name: kmfex-platform
 * File Name: InvestorApplicationRepository.java
 * Class Name: InvestorApplicationRepository
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


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hengxin.platform.member.domain.InvestorInfo;

public interface InvestorInfoRepository extends JpaRepository<InvestorInfo, String>,
        JpaSpecificationExecutor<InvestorInfo> {
    
     InvestorInfo findByUserId(String userId);
     
     @Query("SELECT e FROM InvestorInfo e WHERE e.authCenterId IN :ids")
     List<InvestorInfo> findByServiceCenterIds(@Param("ids") List<String> serviceCenterIds);
     
}
