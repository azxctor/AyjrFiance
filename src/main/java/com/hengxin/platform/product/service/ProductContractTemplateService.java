/*
 * Project Name: kmfex-platform
 * File Name: ProductContractTemplateService.java
 * Class Name: ProductContractTemplateService
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
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.product.domain.ProductContractTemplate;
import com.hengxin.platform.product.repository.ProductContractTemplateRepository;

/**
 * Class Name: ProductContractTemplateService Description: TODO
 * 
 * @author yingchangwang
 * 
 */
@Service
public class ProductContractTemplateService {

    @Autowired
    private ProductContractTemplateRepository contractTemplateRepository;

    @Transactional(readOnly = true)
    public ProductContractTemplate getProductContractTemplate(String templateId) {
        return contractTemplateRepository.findOne(templateId);
    }

}
