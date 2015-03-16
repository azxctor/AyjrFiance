/*
 * Project Name: kmfex-platform
 * File Name: AgencyRepository.java
 * Class Name: AgencyRepository
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

package com.hengxin.platform.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hengxin.platform.member.domain.Agency;

/**
 * Class Name: AgencyRepository Description: TODO
 * 
 * @author chunlinwang
 * 
 */

public interface AgencyRepository extends JpaRepository<Agency, String> {

    Agency findByUserId(String userId);
    
    List<Agency> findByNameAndUserIdNot(String name, String userId);
    
    List<Agency> findByName(String name);
}
