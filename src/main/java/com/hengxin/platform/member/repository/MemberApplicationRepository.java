/*
 * Project Name: kmfex-platform
 * File Name: MemberApplicationRepository.java
 * Class Name: MemberApplicationRepository
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.enums.EApplicationStatus;

/**
 * Class Name: MemberApplicationRepository
 * 
 * @author runchen
 * 
 */
@Repository
public interface MemberApplicationRepository extends PagingAndSortingRepository<MemberApplication, String>,
        JpaSpecificationExecutor<MemberApplication> {

    List<MemberApplication> findByUserId(String userId);

    Page<MemberApplication> findByUserId(String userId, Pageable pageable);

    Page<MemberApplication> findByUserIdAndStatusNot(String userId, EApplicationStatus status, Pageable pageable);

    Page<MemberApplication> findByStatus(EApplicationStatus status, Specification<MemberApplication> spec,
            Pageable pageable);

    Page<MemberApplication> findByPersonIdCardNumberAndStatusNotAndStatusNot(String personIdCardNumber,
            EApplicationStatus uncommitStatus, EApplicationStatus rejectStatus, Pageable pageable);

    Page<MemberApplication> findByPersonIdCardNumberAndUserIdNotAndStatusNotAndStatusNot(String personIdCardNumber,
            String userId, EApplicationStatus uncommitStatus, EApplicationStatus rejectStatus, Pageable pageable);

    Page<MemberApplication> findByBankAccountAndStatusNotAndStatusNot(String bankAccout,
            EApplicationStatus uncommitStatus, EApplicationStatus rejectStatus, Pageable pageable);

    Page<MemberApplication> findByBankAccountAndUserIdNotAndStatusNotAndStatusNot(String bankAccout, String userId,
            EApplicationStatus uncommitStatus, EApplicationStatus rejectStatus, Pageable pageable);

    Page<MemberApplication> findByOrgLegalPersonIdCardNumberAndUserIdNot(String orgLegalPersonIdCardNumber,
            String userId, EApplicationStatus uncommitStatus, EApplicationStatus rejectStatus, Pageable pageable);
}
