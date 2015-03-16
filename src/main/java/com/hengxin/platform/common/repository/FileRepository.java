/*
 * Project Name: kmfex-platform
 * File Name: FileRepository.java
 * Class Name: FileRepository
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

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.entity.SysFilePo;

/**
 * Class Name: FileRepository
 * 
 * @author runchen
 * 
 */

@Transactional(readOnly = true)
public interface FileRepository extends PagingAndSortingRepository<SysFilePo, String> {

	SysFilePo findFileById(String id);

}
