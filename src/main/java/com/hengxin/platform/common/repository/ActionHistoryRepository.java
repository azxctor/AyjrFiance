/*
 * Project Name: kmfex-platform
 * File Name: UserActionHistoryRepository.java
 * Class Name: UserActionHistoryRepository
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

package com.hengxin.platform.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hengxin.platform.common.entity.ActionHistoryPo;

/**
 * Class Name: UserActionHistoryRepository
 * 
 * @author chunlinwang
 * 
 */

public interface ActionHistoryRepository extends JpaRepository<ActionHistoryPo, String> {

    Page<ActionHistoryPo> findAll(Specification<ActionHistoryPo> spec, Pageable pageable);

}
