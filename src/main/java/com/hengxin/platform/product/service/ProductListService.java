package com.hengxin.platform.product.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.member.domain.ProductProviderInfo;
import com.hengxin.platform.product.domain.Product;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.product.dto.ProductQueryConditionDto;
import com.hengxin.platform.product.enums.EPackageStatus;
import com.hengxin.platform.product.enums.EProductActionType;
import com.hengxin.platform.product.enums.EProductStatus;
import com.hengxin.platform.product.enums.EResultType;
import com.hengxin.platform.product.repository.ProductRepository;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.entity.UserPo;

@Service
public class ProductListService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SecurityContext securityContext;

    /**
     * 通过条件查询产品list
     * 
     * @param queryConditionDto
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductList(final ProductQueryConditionDto queryConditionDto) throws BizServiceException {
        String userId = this.securityContext.getCurrentUserId();
        if (queryConditionDto == null || StringUtils.isBlank(userId)) {
            return null;
        }

        return this.getProductByQueryConditon(queryConditionDto, userId);
    }

    /**
     * 
     * Description: build specification and query
     * 
     * @param queryDto
     * @param userId
     * @return
     * @throws ServiceException
     */
    private Page<Product> getProductByQueryConditon(final ProductQueryConditionDto queryDto, final String userId)
            throws BizServiceException {
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                EProductActionType actionType = queryDto.getProductActionType();
                if (queryDto.getResultType() == null) {
                    if (actionType == EProductActionType.PUBLISH) {
                        EProductStatus pStatus = null;
                        EPackageStatus pacStatus = null;
                        try {
                            pStatus = EProductStatus.valueOf(queryDto.getComString());
                        } catch (Exception e) {
                            pacStatus = EPackageStatus.valueOf(queryDto.getComString());
                        }
                        if (pStatus != null) {
                            expressions.add(cb.equal(root.<EProductStatus> get("status"), pStatus));
                        }
                        if (pacStatus != null) {
                            Subquery<Product> sq = query.subquery(Product.class);
                            Root<Product> pRoot = sq.from(Product.class);
                            Join<Product, ProductPackage> packageAIP = pRoot.join("productPackages", JoinType.INNER);
                            sq.select(pRoot).where(
                                    cb.and(cb.equal(pRoot.get("productId"), root.<String> get("productId"))),
                                    cb.equal(packageAIP.<EPackageStatus> get("status"), pacStatus));
                            expressions.add(cb.exists(sq));
                        }
                    }
                    if (actionType == EProductActionType.CHECK) {
                        expressions.add(cb.equal(root.get("applUserId").as(String.class), userId));
                    } else if (actionType == EProductActionType.COMMIT) {
                        expressions.add(cb.equal(root.get("warrantId").as(String.class), userId));
                    }
                    if (queryDto.getProductStatus() != null && queryDto.getProductStatus() != EProductStatus.NULL) {
                        if (queryDto.getProductStatus() == EProductStatus.WAITTOAPPROVE
                                && (actionType == EProductActionType.CHECK || actionType == EProductActionType.COMMIT)) {
                            Predicate disjunction = cb.disjunction();
                            List<Expression<Boolean>> expressions2 = disjunction.getExpressions();
                            expressions2.add(cb.equal((root.<EProductStatus> get("status")),
                                    EProductStatus.WAITTOAPPROVE));
                            expressions2.add(cb.equal((root.<EProductStatus> get("status")),
                                    EProductStatus.WAITTOEVALUATE));
                            expressions2
                                    .add(cb.equal(root.<EProductStatus> get("status"), EProductStatus.WAITTOFREEZE));
                            expressions.add(cb.or(disjunction));
                        } else {
                            expressions.add(cb.equal(root.<EProductStatus> get("status"), queryDto.getProductStatus()));
                        }
                    }
                } else if (queryDto.getResultType() == EResultType.UNRESOLOVING) {
                    if (actionType == EProductActionType.APPROVE) {
                        expressions.add(cb.equal(root.<EProductStatus> get("status"), EProductStatus.WAITTOAPPROVE));
                    } else if (actionType == EProductActionType.EVALUATE) {
                        expressions.add(cb.equal(root.<EProductStatus> get("status"), EProductStatus.WAITTOEVALUATE));
                    } else if (actionType == EProductActionType.FREEZE) {
                        expressions.add(cb.equal(root.<EProductStatus> get("status"), EProductStatus.WAITTOFREEZE));
                    } else if (actionType == EProductActionType.PUBLISH) {
                        expressions.add(cb.equal(root.<EProductStatus> get("status"), EProductStatus.WAITTOPUBLISH));
                    }
                } else if (queryDto.getResultType() == EResultType.RESOLOVED) {
                    Date date = new Date();
                    DateFormat start = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStartString = start.format(date);
                    DateFormat end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateEndString = end.format(date);
                    Date dateStart;
                    Date dateEnd;
                    try {
                        dateStart = start.parse(dateStartString);
                        dateEnd = end.parse(dateEndString);
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.TECH_DATA_INVALID, "时间转化出错");
                    }
                    if (actionType == EProductActionType.APPROVE) {
                        expressions.add(cb.between(root.<Date> get("apprDate"), dateStart, dateEnd));
                    } else if (actionType == EProductActionType.EVALUATE) {
                        expressions.add(cb.between(root.<Date> get("evaluateDate"), dateStart, dateEnd));
                    } else if (actionType == EProductActionType.FREEZE) {
                        expressions.add(cb.between(root.<Date> get("freezeDate"), dateStart, dateEnd));
                    } else if (actionType == EProductActionType.PUBLISH) {
                        expressions.add(cb.between(root.<Date> get("publishDate"), dateStart, dateEnd));
                    }
                }
                String key = queryDto.getSearchKeyString();
                if (StringUtils.isNotBlank(key)) {
                    Predicate disjunction = cb.disjunction();
                    List<Expression<Boolean>> expressions2 = disjunction.getExpressions();
                    expressions2.add(cb.like(cb.lower(root.<UserPo> get("user").<String> get("name")), "%"
                            + key.trim().toLowerCase() + "%"));
                    expressions2.add(cb.like(cb.lower(root.<String> get("productName")), "%" + key.trim().toLowerCase()
                            + "%"));
                    expressions2.add(cb.like(
                            cb.lower(root.<ProductProviderInfo> get("productProviderInfo").<UserPo> get("userPo")
                                    .<String> get("name")), "%" + key.trim().toLowerCase() + "%"));

                    expressions.add(cb.or(disjunction));
                }
                return predicate;
            }
        };

        Pageable pageable = this.buildPageRequest(queryDto);
        Page<Product> proPage = this.productRepository.findAll(specification, pageable);
        return proPage;
    }

    private Pageable buildPageRequest(DataTablesRequestDto requestDto) {
        Sort sort = null;
        List<Integer> sortedColumns = requestDto.getSortedColumns();
        List<String> sortDirections = requestDto.getSortDirections();
        List<String> dataProps = requestDto.getDataProps();

        for (Integer item : sortedColumns) {
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
            if (sortColumn.equals("finaceTerm")) {
                sortColumn = "termToDays";
            }
            if (sortColumn.equals("applyDate")) {
                sortColumn = "applDate";
            }
            if (sortColumn.equals("applyDate")) {
                sortColumn = "applDate";
            }
            if (sortColumn.equals("appliedQuotaUnit")) {
                sortColumn = "appliedQuota";
            }
            if (sortColumn.equals("guaranteeInstitutionShow")) {
                sortColumn = "userPoWrtrShow.name";
            }

            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

}
