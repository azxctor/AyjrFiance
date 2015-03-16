/*
 * Project Name: kmfex-platform
 * File Name: AgencyApplicationViewRepository.java
 * Class Name: AgencyApplicationViewRepository
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.member.domain.AgencyApplicationView;

/**
 * Class Name: AgencyApplicationViewRepository
 * 
 * @author shengzhou
 * 
 */

public interface AgencyApplicationViewRepository extends PagingAndSortingRepository<AgencyApplicationView, String> {

    Page<AgencyApplicationView> findAll(Specification<AgencyApplicationView> spec, Pageable pageable);
    
}
