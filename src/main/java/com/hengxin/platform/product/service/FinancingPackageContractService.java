/*
 * Project Name: kmfex-platform
 * File Name: FinancingPackageContractService.java
 * Class Name: FinancingPackageContractService
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.fund.entity.PkgTradeJnlPo;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.product.dto.FinancingPackageContractRequestDto;

/**
 * Class Name: FinancingPackageContractService
 * 
 * @author yingchangwang
 * 
 */
@Service
public class FinancingPackageContractService {

    @Autowired
    private PkgTradeJnlRepository pkgTradeJnlRepository;

    /**
     * 
    * Description: 分页查询融资包合同列表
    *
    * @param dto
    * @return page
     */
    public Page<PkgTradeJnlPo> getContranctListForPackage(FinancingPackageContractRequestDto dto) {
        final String packageId = dto.getPackageId();
        Pageable pageable = buildPageRequest(dto);

        Specification<PkgTradeJnlPo> spec = new Specification<PkgTradeJnlPo>() {
            @Override
            public Predicate toPredicate(Root<PkgTradeJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                root.join("productPackage", JoinType.INNER);
                root.join("positionLot", JoinType.INNER);
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<String> get("pkgId"), packageId));
                expressions.add(cb.greaterThan(root.<Integer> get("unit"), 0));
                expressions.add(cb.equal(root.<EFundTrdType> get("trdType"), EFundTrdType.BONDSUBS));
                return predicate;
            }
        };

        Page<PkgTradeJnlPo> findAll = pkgTradeJnlRepository.findAll(spec, pageable);

        return findAll;
    }

    /**
     * 
     * Description: 查询融资包合同列表
     * 
     * @param packageId
     * @return list
     */
    public List<PkgTradeJnlPo> getAllContranctListForPackage(final String packageId) {
        Specification<PkgTradeJnlPo> spec = new Specification<PkgTradeJnlPo>() {
            @Override
            public Predicate toPredicate(Root<PkgTradeJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                root.join("productPackage", JoinType.INNER);
                root.join("positionLot", JoinType.INNER);
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<String> get("pkgId"), packageId));
                expressions.add(cb.greaterThan(root.<Integer> get("unit"), 0));
                expressions.add(cb.equal(root.<EFundTrdType> get("trdType"), EFundTrdType.BONDSUBS));
                return predicate;
            }
        };

        return pkgTradeJnlRepository.findAll(spec);
    }

    /**
     * 构建分页对象
     * 
     * @param requestDto
     * @return
     */
    public static Pageable buildPageRequest(DataTablesRequestDto requestDto) {

        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();
        Order defaultOrder = null;
        for (Integer item : sortedColumns) {
            if (item == 0) {
                break;
            }
            String sortColumn = dataProps.get(item);
            int indexOf = 0;
            if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".text")) {
                indexOf = sortColumn.lastIndexOf(".text");
            } else if (StringUtils.isNotBlank(sortColumn) && sortColumn.endsWith(".fullText")) {
                indexOf = sortColumn.lastIndexOf(".fullText");
            }
            if (indexOf > 0) {
                sortColumn = sortColumn.substring(0, indexOf);
            }
            if ("signedAmt".equalsIgnoreCase(sortColumn)) {
                sortColumn = "unit";
            }
            if ("function".equalsIgnoreCase(sortColumn)) {
                sortColumn = "positionLot.contractId";
            }
            String sortDir = sortDirections.get(0);
            defaultOrder = new Order(Direction.fromString(sortDir), sortColumn);
        }
        sort = new Sort(defaultOrder);

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

}
