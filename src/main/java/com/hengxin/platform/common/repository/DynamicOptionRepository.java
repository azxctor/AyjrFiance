/*
 * Project Name: kmfex-platform
 * File Name: SysDictRepository.java
 * Class Name: SysDictRepository
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

package com.hengxin.platform.common.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.entity.DynamicOptionPo;

/**
 * Class Name: SysDictRepository
 * 
 * @author shmilywen
 * 
 */

@Transactional(readOnly = true)
public interface DynamicOptionRepository extends PagingAndSortingRepository<DynamicOptionPo, Long> {

    List<DynamicOptionPo> findByEnabled(String enabled);

    List<DynamicOptionPo> findByCategoryAndEnabled(String category, String enabled);

    List<DynamicOptionPo> findByCategoryAndEnabledAndParentCodeIsNull(String category, String enabled);

    List<DynamicOptionPo> findByParentCodeAndEnabled(String parentCode, String enabled);

    DynamicOptionPo findByCategoryAndCodeAndEnabled(String category, String code, String enabled);
}
