/*
 * Project Name: kmfex-platform
 * File Name: FeeGroupService.java
 * Class Name: FeeGroupService
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.product.domain.Fee;
import com.hengxin.platform.product.domain.FeeGroup;
import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.repository.FeeGroupRepository;
import com.hengxin.platform.product.repository.ProductFeeRepository;

/**
 * Class Name: FeeGroupService Description: TODO
 *
 * @author yingchangwang
 *
 */

@Service
public class FeeGroupService {
    @Autowired
    private FeeGroupRepository feeGroupRepository;

    @Autowired
    private ProductFeeRepository productFeeRepository;

    @Transactional(readOnly = true)
    public List<Fee> getFeeListByGroupId(String groupId) {
        FeeGroup feeGroup = feeGroupRepository.findOne(groupId);
        if (feeGroup != null && feeGroup.getFeeList() != null) {
            return feeGroup.getFeeList();
        }
        return null;
    }

    public List<ProductFee> getFeeListByProductId(String productId) {
        return productFeeRepository.findByProductId(productId);
    }

}
