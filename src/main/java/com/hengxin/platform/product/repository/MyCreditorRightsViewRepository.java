/*
 * Project Name: kmfex-platform
 * File Name: MyCreditorRightsViewRepository.java
 * Class Name: MyCreditorRightsViewRepository
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.product.domain.MyCreditorRightsView;

/**
 * Class Name: MyCreditorRightsViewRepository Description: TODO
 * 
 * @author yingchangwang
 * 
 */

public interface MyCreditorRightsViewRepository extends PagingAndSortingRepository<MyCreditorRightsView, String>,
        JpaSpecificationExecutor<MyCreditorRightsView> {
    public Page<MyCreditorRightsView> findAll(Specification<MyCreditorRightsView> spec, Pageable pageable);

}
