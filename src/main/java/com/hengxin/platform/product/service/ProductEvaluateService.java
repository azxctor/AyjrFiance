/*
 * Project Name: kmfex-platform
 * File Name: ProductEvaluateService.java
 * Class Name: ProductEvaluateService
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.enums.EBizRole;

/**
 * Class Name: ProductEvaluateService
 * 
 * @author chunlinwang
 * 
 */

@Service
@Transactional
public class ProductEvaluateService extends BaseService {

    @Autowired
    ProductService productService;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private MemberMessageService memberMessageService;

    @Transactional
    public void evaluateProduct(final Product productDto) {
        String productId = productDto.getProductId();
        if (StringUtils.isEmpty(productId)) {
            return;
        }

        Product product = productRepository.findOne(productId);
        if (product == null) {
            return;
        }
        // added by martin for jbpm process
        product.setProductLevel(productDto.getProductLevel());
        product.setFinancierLevel(productDto.getFinancierLevel());
        product.setWarrantorLevel(productDto.getWarrantorLevel());
        EProductStatus productStatus = productService.approveProduct(productId, null, product.getStatus(), true);
        product.setStatus(productStatus);
        // end
        product.setEvaluateDate(new Date());
        product.setLastMntOpid(productDto.getLastMntOpid());
        product.setLastMntTs(new Date());
        // 此属性移至担保机构信息维护处
        //product.setWrtrCreditFile(productDto.getWrtrCreditFile());

        productRepository.save(product);
        actionHistoryUtil.save(EntityType.PRODUCT, productId, ActionType.EVUALUATE, null);

        sendEvaluateProductMessage(product);
    }
    
    /**
     * 评级成功后 发送待冻结待办事项
     * @param req
     */
	private void sendEvaluateProductMessage(Product product) {
		String content = product.getProductName();
		String messageKey = "product.evaluation.message";
		List<EBizRole> roleList = Arrays.asList(EBizRole.PLATFORM_RISK_CONTROL_SUPERVISOR);
		memberMessageService.sendMessage(EMessageType.TODO, messageKey, roleList, content);
	}
}
