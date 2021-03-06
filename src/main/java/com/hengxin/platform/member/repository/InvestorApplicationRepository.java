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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.member.domain.BaseApplication;
import com.hengxin.platform.member.domain.InvestorApplication;
import com.hengxin.platform.member.enums.EApplicationStatus;

/**
 * Class Name: InvestorApplicationRepository
 * 
 * @author runchen
 * 
 */

public interface InvestorApplicationRepository extends PagingAndSortingRepository<BaseApplication, String>,
JpaSpecificationExecutor<BaseApplication> {

    Page<InvestorApplication> findByUserId(String userId, Pageable pageable);

    Page<InvestorApplication> findByUserIdAndStatusNot(String userId, EApplicationStatus status, Pageable pageable);

}
