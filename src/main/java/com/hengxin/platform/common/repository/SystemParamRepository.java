/*
 * Project Name: kmfex-platform
 * File Name: ProductRepository.java
 * Class Name: ProductRepository
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

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.common.domain.SystemParam;

/**
 * Class Name: ProductSystemParamRepository Description: TODO
 * 
 * @author Ryan
 * 
 */

public interface SystemParamRepository extends PagingAndSortingRepository<SystemParam, String>,
        JpaSpecificationExecutor<SystemParam> {


}
