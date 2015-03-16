/*
 * Project Name: kmfex-platform
 * File Name: ProductRepository.java
 * Class Name: ProductRepository
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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hengxin.platform.product.domain.ProductWarrantEnterprise;

/**
 * Class Name: ProductRepository Description: TODO
 * 
 * @author Ryan
 * 
 */

public interface ProductWarrantEnterpriseRepository extends
		JpaRepository<ProductWarrantEnterprise, String>,
		JpaSpecificationExecutor<ProductWarrantEnterprise> {
	List<ProductWarrantEnterprise> findByProductId(String productId);
	@Modifying
	@Query("delete from ProductWarrantEnterprise where productId = ?1")
	void deleteByProductId(String productId);

}
