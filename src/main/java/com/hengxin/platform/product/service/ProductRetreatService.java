/*
 * Project Name: kmfex-platform
 * File Name: ProductRetreatService.java
 * Class Name: ProductRetreatService
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

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.service.BaseService;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.repository.ProductRepository;

/**
 * Class Name: ProductRetreatService
 * 
 * @author chunlinwang
 * 
 */
@Service
@Transactional
public class ProductRetreatService extends BaseService {

    @Autowired
    ProductService productService;

    @Autowired
    FinanceRevokeService revokeService;

    @Autowired
    private ProductRepository productRepository;

    public EProductStatus retreatProduct(String productId, EProductStatus productStatus, String comment,
            String currentUserId) {

        Product product = productRepository.findOne(productId);
        if (product == null) {
            return null;
        }
        EProductStatus status = product.getStatus();
        if (productStatus != status) {
            return null;
        }

        // EProductStatus result = null;
        //
        // if (status == EProductStatus.WAITTOEVALUATE) {
        // result = EProductStatus.WAITTOAPPROVE;
        //
        // } else if (status == EProductStatus.WAITTOFREEZE) {
        // result = EProductStatus.WAITTOEVALUATE;
        //
        // } else if (status == EProductStatus.WAITTOPUBLISH) {
        // result = EProductStatus.WAITTOFREEZE;
        //
        // } else {
        // // only in above 3 status can retreat.
        // return null;
        // }
        // added by martin for jbpm process
        EProductStatus result = productService.approveProduct(productId, null, productStatus, false);
        product.setStatus(result);
        // end
        productRepository.save(product);

        if (EProductStatus.WAITTOFREEZE == product.getStatus()) {
            revokeService.productThawDeposit(product, currentUserId);
        }

        actionHistoryUtil.save(EntityType.PRODUCT, productId, ActionType.RETREAT, null, comment);
        return result;
    }
}
