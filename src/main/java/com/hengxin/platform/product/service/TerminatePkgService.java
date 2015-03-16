/*
 * Project Name: kmfex-platform
 * File Name: TerminatePkgService.java
 * Class Name: TerminatePkgService
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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.enums.ActionResult;
import com.hengxin.platform.common.enums.ActionType;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.enums.EntityType;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.service.ActionHistoryService;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.exception.PkgTerminateOrCancelInMarketOpenException;
import com.hengxin.platform.product.repository.ProductPackageRepository;
import com.hengxin.platform.product.repository.ProductRepository;

/**
 * 终止融资包服务 Class Name: TerminatePkgService
 * 
 * @author tingwang
 * 
 */
@Service
public class TerminatePkgService {

    @Autowired
    ProductPackageRepository productPackageRepository;
    @Autowired
    ActionHistoryService actionHistoryService;
    @Autowired
    PackageJobService packageJobService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProdPkgMsgRemindService prodPkgMsgRemindService;

    /**
     * 申购中：终止融资包
     * 
     * @param packageId
     * @param operatorId
     * @param operateDate
     */
    @Transactional
    public void terminateProductPackageInApply(String packageId, String operatorId, boolean checkMarketOpen) {
        // 判断时间点是否为闭市后开市前
        if (CommonBusinessUtil.isMarketOpen() && checkMarketOpen) {
            throw new PkgTerminateOrCancelInMarketOpenException("只能在闭市后开市前进行操作");
        }
        if (StringUtils.isBlank(packageId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        if (StringUtils.isBlank(operatorId)) {
            throw new BizServiceException(EErrorCode.TECH_PARAM_REQUIRED);
        }
        ProductPackage productPackage = productPackageRepository.getProductPackageById(packageId);
        if (productPackage == null) {
            throw new BizServiceException(EErrorCode.TECH_DATA_NOT_EXIST);
        }
        if (!StringUtils.equals(productPackage.getStatus().getCode(), EPackageStatus.SUBSCRIBE.getCode())) {
            throw new BizServiceException(EErrorCode.PROD_PKG_STATUS_IS_NOT_SUBSCRIBE);
        }
        if (productPackage.getPackageQuota().compareTo(productPackage.getSupplyAmount()) == 0) {
            throw new BizServiceException(EErrorCode.PROD_PKG_TERMINATE_WHEN_SUPPLY_IS_FULL);
        }

        // 更新融资包状态为待签约
        productPackage.setLastOperatorId(operatorId);
        Date currentDate = new Date();
        productPackage.setLastTime(currentDate);
//        productPackage.setLastSubsTime(currentDate);

        packageJobService.endPackage(productPackage, EPackageStatus.WAIT_SIGN);

        // 记录日志
        actionHistoryService.save(EntityType.PRODUCTPACKAGE, packageId, ActionType.PKGTERMINATION,
                ActionResult.PRODUCT_PACKAGE_TERMINATION);

        // 发送提醒
        Product product = productRepository.findOne(productPackage.getProductId());
        prodPkgMsgRemindService.pkgChangesToWaitSign(packageId, product.getApplUserId(),
                ProdPkgMsgRemindService.OPSUBSTERMINATE);

    }
}
