/*
 * Project Name: kmfex-platform
 * File Name: MemberLessViewRepository.java
 * Class Name: MemberLessViewRepository
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

import com.hengxin.platform.member.domain.MemberLessView;

/**
 * Class Name: MemberLessViewRepository
 * 
 * @author ycc
 * 
 */

public interface MemberLessViewRepository extends PagingAndSortingRepository<MemberLessView, String>{

   public Page<MemberLessView> findAll(Specification<MemberLessView> spec, Pageable pageable);
    
}

