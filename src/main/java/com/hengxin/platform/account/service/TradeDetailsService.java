/*
 * Project Name: kmfex-platform
 * File Name: TradeDetailsService.java
 * Class Name: TradeDetailsService
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

package com.hengxin.platform.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

import com.hengxin.platform.account.dto.TradeDetailsDto;
import com.hengxin.platform.account.dto.downstream.TradeDetailsSearchDto;
import com.hengxin.platform.account.enums.ETradeDirection;
import com.hengxin.platform.account.enums.ETradeType;
import com.hengxin.platform.common.dto.DataTablesRequestDto;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.entity.PkgTradeJnlPo;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.fund.util.AmtUtils;
import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.PackageTradeDetailView;
import com.hengxin.platform.product.repository.PackageTradeDetailViewRepository;

/**
 * Class Name: TradeDetailsService
 * 
 * @author congzhou
 * 
 */
@Service
public class TradeDetailsService {

    @Autowired
    PkgTradeJnlRepository pkgTradeJnlRepository;

    @Autowired
    PackageTradeDetailViewRepository packageTradeDetailViewRepository;

    /**
     * 
     * Description: 获取交易明细
     * 
     * @param searchDto
     * @param userId
     * @return
     */
    @Deprecated
    @Transactional(readOnly = true)
    public DataTablesResponseDto<TradeDetailsDto> getFundDetails(final TradeDetailsSearchDto searchDto,
            final String userId) {
        Specification<PkgTradeJnlPo> specification = new Specification<PkgTradeJnlPo>() {

            @Override
            public Predicate toPredicate(Root<PkgTradeJnlPo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.get("buyerUserId"), userId));
                if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("trdDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("trdDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (!EFundTrdType.ALL.equals(searchDto.getTrdType())) {
                    expressions.add(cb.equal(root.get("trdType"), searchDto.getTrdType()));
                }
                return predicate;
            }
        };
        Pageable pageable = this.buildPageRequest(searchDto);
        Page<PkgTradeJnlPo> pkgTradeJnlPos = pkgTradeJnlRepository.findAll(specification, pageable);
        return packDto(pkgTradeJnlPos);

    }

    /**
     * 
     * Description: 获取交易明细
     * 
     * @param searchDto
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesResponseDto<TradeDetailsDto> getTradeDetails(final TradeDetailsSearchDto searchDto,
            final String userId) {
        Specification<PackageTradeDetailView> specification = new Specification<PackageTradeDetailView>() {

            @Override
            public Predicate toPredicate(Root<PackageTradeDetailView> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.get("userId"), userId));
                if (null != searchDto.getFromDate()) {
                    try {
                        expressions.add(cb.greaterThanOrEqualTo(root.get("trdDt").as(Date.class),
                                DateUtils.getStartDate(searchDto.getFromDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (null != searchDto.getToDate()) {
                    try {
                        expressions.add(cb.lessThanOrEqualTo(root.get("trdDt").as(Date.class),
                                DateUtils.getEndDate(searchDto.getToDate())));
                    } catch (ParseException e) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                    }
                }
                if (!EFundTrdType.ALL.equals(searchDto.getTrdType())) {
                    expressions.add(cb.equal(root.get("trdType"), searchDto.getTrdType()));
                }
                return predicate;
            }
        };
        Pageable pageable = this.buildPageRequest(searchDto);
        Page<PackageTradeDetailView> tradeViews = packageTradeDetailViewRepository.findAll(specification, pageable);
        return packageDto(tradeViews);

    }

    /**
     * Description: 包转为Dto
     * 
     * @param tradeViews
     * @return
     */

    private DataTablesResponseDto<TradeDetailsDto> packageDto(Page<PackageTradeDetailView> tradeViews) {
        DataTablesResponseDto<TradeDetailsDto> result = new DataTablesResponseDto<TradeDetailsDto>();
        List<TradeDetailsDto> tradeDetailsDtos = new ArrayList<TradeDetailsDto>();
        for (PackageTradeDetailView view : tradeViews) {
            TradeDetailsDto tradeDetailsDto = new TradeDetailsDto();
            // 计算金额(万元，保留一位小数)
            tradeDetailsDto.setAmount(view.getAmount().toString());
            if (null != view.getTrdDt()) {
                tradeDetailsDto.setTrdDt(DateUtils.formatDate(view.getTrdDt(), "yyyy-MM-dd"));
            }
            tradeDetailsDto.setDirection(view.getDirection().getText());
            tradeDetailsDto.setPkgId(view.getPkgId());
            tradeDetailsDto.setPkgName(view.getProductName());
            tradeDetailsDto.setRiskLvl(view.getProductLevel());
            String term = view.getTermLength() + view.getTermType().getText();
            tradeDetailsDto.setTerm(term);
            tradeDetailsDto.setRate(AmtUtils.formateRate(view.getRate()));
            tradeDetailsDto.setTrdType(view.getTrdType());
            tradeDetailsDto.setStatus(convertStatus(view));
            tradeDetailsDto.setSelled(ETradeDirection.SELL.equals(view.getDirection()));
            tradeDetailsDto.setLotId(view.getLotId());
            tradeDetailsDto.setCreateTs(DateUtils.formatDate(view.getCreateTs(), "yyyy-MM-dd HH:mm:ss"));
            tradeDetailsDtos.add(tradeDetailsDto);
        }
        result.setData(tradeDetailsDtos);
        result.setTotalDisplayRecords(tradeViews.getTotalElements());
        result.setTotalRecords(tradeViews.getTotalElements());
        return result;
    }
    
    /**
     * Description: 转换融资包状态为交易状态
     * 
     * @param status
     * @return
     */

    private ETradeType convertStatus(PackageTradeDetailView view) {
        ETradeType result;
        if (EFundTrdType.BONDASSIGN.equals(view.getTrdType())) {
            if (ETradeDirection.BUY.equals(view.getDirection())) {
                result = ETradeType.BUY_SUCESS;
            } else {
                result = ETradeType.TRANSFERD;
            }
        } else {
            switch (view.getStatus()) {
            case ABANDON:
                result = ETradeType.CANCELD;
                break;
            case END:
                result = ETradeType.END;
                break;
            case PAYING:
                result = ETradeType.SIGN_SUCCESS;
                break;
            case SUBSCRIBE:
                result = ETradeType.WAIT_SIGN;
                break;
            case TRANSEND:
                if (ETradeDirection.BUY.equals(view.getDirection())) {
                    result = ETradeType.BUY_SUCESS;
                } else {
                    result = ETradeType.TRANSFERD;
                }
                break;
            case TRANSFERING:
                result = ETradeType.SIGN_SUCCESS;
                break;
            case WAIT_LOAD_APPROAL:
                result = ETradeType.SIGN_SUCCESS;
                break;
            case WAIT_LOAD_APPROAL_CONFIRM:
                result = ETradeType.SIGN_SUCCESS;
                break;
            case WAIT_SIGN:
                result = ETradeType.WAIT_SIGN;
                break;
            default:
                result = ETradeType.NULL;
                break;
            }
        }
        return result;
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
            if (sortColumn.equals("riskLvl")) {
                sortColumn = "garade";
            }

            String sortDir = sortDirections.get(0);
            sort = new Sort(Direction.fromString(sortDir), sortColumn);
            sort = sort.and(new Sort(Direction.fromString(sortDir), "id"));
        }

        PageRequest page = new PageRequest(requestDto.getDisplayStart() / requestDto.getDisplayLength(),
                requestDto.getDisplayLength(), sort);
        return page;
    }

    /**
     * 
     * 包装为返回Dto
     * 
     * @param pkgTradeJnlPos
     * @return
     */
    private DataTablesResponseDto<TradeDetailsDto> packDto(Page<PkgTradeJnlPo> pkgTradeJnlPos) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(1);
        DataTablesResponseDto<TradeDetailsDto> result = new DataTablesResponseDto<TradeDetailsDto>();
        List<TradeDetailsDto> tradeDetailsDtos = new ArrayList<TradeDetailsDto>();
        for (PkgTradeJnlPo pkgTradeJnlPo : pkgTradeJnlPos) {
            TradeDetailsDto tradeDetailsDto = new TradeDetailsDto();
            // 计算金额(万元，保留一位小数)
            //TODO 此金额的计算有问题？ 待确认
            BigDecimal amount = new BigDecimal(pkgTradeJnlPo.getUnit()).multiply(pkgTradeJnlPo.getLotBuyPrice())
                    .divide(new BigDecimal("10000")).setScale(1, RoundingMode.HALF_UP);
            tradeDetailsDto.setAmount(amount.toString());
            // tradeDetailsDto.setTrdDt(pkgTradeJnlPo.getTrdDt());
            String direction = pkgTradeJnlPo.getTrdType() == EFundTrdType.BONDSUBS ? "买入" : "卖出";
            tradeDetailsDto.setDirection(direction);
            tradeDetailsDto.setPkgId(pkgTradeJnlPo.getProductPackage().getId());
            tradeDetailsDto.setPkgName(pkgTradeJnlPo.getProductPackage().getProduct().getProductName());
            // tradeDetailsDto.setRiskLvl(pkgTradeJnlPo.getProductPackage().getProduct().getTotalGrage());
            tradeDetailsDto.setTerm(pkgTradeJnlPo.getProductPackage().getProduct().getTermLength().toString());
            tradeDetailsDto.setRate(percent.format(pkgTradeJnlPo.getProductPackage().getProduct().getRate()
                    .setScale(3, BigDecimal.ROUND_HALF_UP)));
            tradeDetailsDto.setTrdType(pkgTradeJnlPo.getTrdType());
            tradeDetailsDtos.add(tradeDetailsDto);
        }
        result.setData(tradeDetailsDtos);
        result.setTotalDisplayRecords(pkgTradeJnlPos.getTotalElements());
        result.setTotalRecords(pkgTradeJnlPos.getTotalElements());
        return result;
    }

}
