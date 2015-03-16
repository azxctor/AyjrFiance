/*
 * Project Name: kmfex-platform
 * File Name: ProductFeeRepository.java
 * Class Name: ProductFeeRepository
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

package com.hengxin.platform.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.product.domain.ProductFee;
import com.hengxin.platform.product.domain.ProductFeePk;

/**
 * Class Name: ProductFeeRepository Description: TODO
 *
 * @author yingchangwang
 *
 */

public interface ProductFeeRepository extends JpaRepository<ProductFee, ProductFeePk> {
	
    public List<ProductFee> findByProductId(String productId);

    ProductFee getFeeByProductIdAndFeeId(String productId, Integer feeId);
    
    List<ProductFee> getFeeByProductId(String productId);

    @Query(value = "select p.* from FP_PROD_FEE p, GL_FEE_CFG f where p.PROD_ID = ?1 and p.FEE_ID = f.FEE_ID and f.FEE_NAME = ?2", nativeQuery = true)
    ProductFee getFeeByProductIdAndFeeName(String productId, String feename);
}
