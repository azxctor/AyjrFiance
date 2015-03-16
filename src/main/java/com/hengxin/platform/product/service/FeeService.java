/*
 * Project Name: kmfex-platform
 * File Name: FeeService.java
 * Class Name: FeeService
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

package com.hengxin.platform.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.product.domain.Fee;
import com.hengxin.platform.product.repository.FeeRepository;

/**
 * Class Name: FeeService Description: TODO
 *
 * @author yingchangwang
 *
 */

@Service
public class FeeService {
    @Autowired
    private FeeRepository feeRepository;

    /**
     *
     * Description: TODO
     *
     * @param feeId
     * @return
     */
    public Fee getFeeById(Integer feeId) {
        return feeRepository.findOne(feeId);
    }
}
