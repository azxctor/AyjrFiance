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

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hengxin.platform.product.domain.Product;

/**
 * Class Name: ProductRepository Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

public interface ProductRepository extends PagingAndSortingRepository<Product, String>,
        JpaSpecificationExecutor<Product> {

    public Product findByProductId(String productId);

    @Modifying
    @Query("delete from Product where productId = ?1")
    void deleteByProductId(String productId);
    
    List<Product> findByProductLevel(String productLevel);

}
