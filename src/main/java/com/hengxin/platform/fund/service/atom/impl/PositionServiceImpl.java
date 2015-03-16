package com.hengxin.platform.fund.service.atom.impl;

import static com.hengxin.platform.common.enums.EErrorCode.TECH_DATA_INVALID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.domain.Position;
import com.hengxin.platform.fund.dto.biz.req.ManualPaymentReq;
import com.hengxin.platform.fund.dto.biz.req.atom.PositionChangeReq;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.fund.entity.PkgTradeJnlPo;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;
import com.hengxin.platform.fund.enums.EFundTrdType;
import com.hengxin.platform.fund.repository.AcctRepository;
import com.hengxin.platform.fund.repository.PkgTradeJnlRepository;
import com.hengxin.platform.fund.repository.PositionLotRepository;
import com.hengxin.platform.fund.repository.PositionRepository;
import com.hengxin.platform.fund.service.atom.PositionService;
import com.hengxin.platform.fund.util.AmtUtils;

@Service
@Qualifier("positionService")
public class PositionServiceImpl implements PositionService {

    private final static Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionLotRepository positionLotRepository;

    @Autowired
    private PkgTradeJnlRepository pkgTradeJnlRepository;

    @Autowired
    private AcctRepository acctRepository;

    @Override
    public String positionChange(PositionChangeReq positionChangeReq) throws BizServiceException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(positionChangeReq.toString());
        }

        // 通过买方会员编号和融资包编号获取头寸
        PositionPo buyerPositionPo = positionRepository.findPositionByUserIdAndPkgId(
                positionChangeReq.getBuyerUserId(), positionChangeReq.getPkgId());

        if (EFundTrdType.BONDSUBS.getCode().equals(positionChangeReq.getTrdType().getCode())) {
            // 申购
            return purchaseCR(positionChangeReq, buyerPositionPo);

        } else if (EFundTrdType.BONDASSIGN.getCode().equals(positionChangeReq.getTrdType().getCode())) {
            // 通过卖方会员编号和融资包编号获取头寸
            PositionPo sellerPositionPo = positionRepository.findPositionByUserIdAndPkgId(
                    positionChangeReq.getSellerUserId(), positionChangeReq.getPkgId());
            // 债权转让
            transferCR(positionChangeReq, sellerPositionPo);

            positionChangeReq.setSettleDt(positionChangeReq.getWorkDate());
            positionChangeReq.setCurrOpId(positionChangeReq.getBuyerUserId());
            positionChangeReq.setUnitFaceValue(sellerPositionPo.getUnitFaceValue());
            positionChangeReq.setSubsDt(positionChangeReq.getWorkDate());
            return purchaseCR(positionChangeReq, buyerPositionPo);
        } else {
            // 提前还款
            prepayment(positionChangeReq, buyerPositionPo);
            return null;
        }
    }

    /**
     * 提前还款更新头寸信息及头寸份额信息
     * 
     * @param positionChangeReq
     * @param currDate
     * @param positionPo
     * @throws AmtParamInvalidException
     */
    private void prepayment(PositionChangeReq positionChangeReq, PositionPo positionPo) throws BizServiceException {
        // 通过份额ID获取对应头寸份额信息
        PositionLotPo positionLotPo = positionLotRepository.findPositionLotByLotId(positionChangeReq.getLotId());

        // 更新头寸信息
        PositionPo targetPositionPo = ConverterService.convert(positionPo, PositionPo.class);
        // 计算提前还款后份数
        Integer newUnit = AmtUtils.processNegativeAmt(
                BigDecimal.valueOf(targetPositionPo.getUnit()).subtract(BigDecimal.valueOf(positionLotPo.getUnit())),
                BigDecimal.ZERO).intValue();
        targetPositionPo.setUnit(newUnit);
        targetPositionPo.setLastMntOpid(positionChangeReq.getCurrOpId());
        targetPositionPo.setLastMntTs(new Date());
        // 更新提前还款后头寸信息
        positionRepository.save(targetPositionPo);

        // 设置头寸份额信息
        PositionLotPo targetPositionLotPo = ConverterService.convert(positionLotPo, PositionLotPo.class);
        targetPositionLotPo.setUnit(Integer.valueOf(0));
        BigDecimal paymentAmt = AmtUtils.processNegativeAmt(positionChangeReq.getPaymentAmt(), BigDecimal.ZERO);
        BigDecimal oldAccumCrAmt = AmtUtils.processNegativeAmt(positionLotPo.getAccumCrAmt(), BigDecimal.ZERO);
        targetPositionLotPo.setAccumCrAmt(oldAccumCrAmt.add(paymentAmt));
        targetPositionLotPo.setLastMntOpid(positionChangeReq.getCurrOpId());
        targetPositionLotPo.setLastMntTs(new Date());
        positionLotRepository.save(targetPositionLotPo);
    }

    /**
     * 卖方转让债权
     * 
     * @param positionChangeReq
     * @param currDate
     * @throws AmtParamInvalidException
     */
    private void transferCR(PositionChangeReq positionChangeReq, PositionPo sellerPositionPo)
            throws BizServiceException {

        if (null == sellerPositionPo) {
            throw new BizServiceException(TECH_DATA_INVALID, "会员编号为" + positionChangeReq.getSellerUserId() + "，融资包编号为"
                    + positionChangeReq.getPkgId() + "的头寸不存在");
        }

        // 通过份额ID获取对应头寸份额信息
        PositionLotPo sellerPositionLotPo = positionLotRepository.findPositionLotByLotId(positionChangeReq.getLotId());

        if (null == sellerPositionLotPo) {
            throw new BizServiceException(TECH_DATA_INVALID, "头寸份额ID为" + positionChangeReq.getLotId() + "的头寸份额数据不存在");
        }

        if (positionChangeReq.getUnit().intValue() != sellerPositionLotPo.getUnit().intValue()) {
            throw new BizServiceException(TECH_DATA_INVALID, "买方会员申购份数与卖方份额ID为" + positionChangeReq.getLotId()
                    + "的头寸份额数据不一致");
        }

        // 更新卖方头寸信息
        PositionPo sellerNewPosition = ConverterService.convert(sellerPositionPo, PositionPo.class);
        // 计算卖方转让后份数
        Integer newUnit = AmtUtils.processNegativeAmt(
                BigDecimal.valueOf(sellerNewPosition.getUnit()).subtract(
                        BigDecimal.valueOf(sellerPositionLotPo.getUnit())), BigDecimal.ZERO).intValue();
        sellerNewPosition.setUnit(newUnit);
        sellerNewPosition.setLastMntOpid(positionChangeReq.getCurrOpId());
        sellerNewPosition.setLastMntTs(new Date());
        // 更新转让后头寸信息
        positionRepository.save(sellerNewPosition);

        // 计算卖方头寸份额信息
        PositionLotPo sellerNewPositionLot = ConverterService.convert(sellerPositionLotPo, PositionLotPo.class);
        sellerNewPositionLot.setUnit(Integer.valueOf(0));
        sellerNewPositionLot.setAccumCrAmt(AmtUtils.processNegativeAmt(sellerNewPositionLot.getAccumCrAmt(),
                BigDecimal.ZERO).add(positionChangeReq.getLotBuyPrice()));
        sellerNewPositionLot.setLastMntOpid(positionChangeReq.getCurrOpId());
        sellerNewPositionLot.setLastMntTs(new Date());
        // 更新卖方转让后头寸份额信息
        positionLotRepository.save(sellerNewPositionLot);
    }

    /**
     * 买方购买债权
     * 
     * @param positionChangeReq
     * @param currDate
     * @param positionPo
     * @return
     */
    private String purchaseCR(PositionChangeReq req, PositionPo buyerPosition) {
        PositionPo targetPositionPo;
        PositionLotPo newLot;
        PkgTradeJnlPo pkgTradeJnlPo;
        // 设置头寸信息
        if (null == buyerPosition) {
            targetPositionPo = ConverterService.convert(req, PositionPo.class);
            targetPositionPo.setUserId(req.getBuyerUserId());
            targetPositionPo.setCreateOpid(req.getCurrOpId());
            targetPositionPo.setCreateTs(new Date());
        } else {
            targetPositionPo = ConverterService.convert(buyerPosition, PositionPo.class);
            targetPositionPo.setUserId(req.getBuyerUserId());
            targetPositionPo.setLastMntOpid(req.getCurrOpId());
            targetPositionPo.setLastMntTs(new Date());
            targetPositionPo.setUnit(buyerPosition.getUnit() + req.getUnit());
        }
        // 新增或更新头寸信息
        PositionPo retPosition = positionRepository.save(targetPositionPo);

        // 设置头寸份额信息
        newLot = ConverterService.convert(req, PositionLotPo.class);
        newLot.setLotId(null);
        newLot.setPositionId(retPosition.getPositionId());
        newLot.setCreateOpid(req.getCurrOpId());
        newLot.setCreateTs(new Date());
        newLot.setAccumCrAmt(BigDecimal.ZERO);
        // 添加合同编号
        AcctPo acct = acctRepository.findByUserId(retPosition.getUserId());
        newLot.setContractId(generateCntrctId(retPosition.getPkgId(), acct.getAcctNo()));

        PositionLotPo sellerPositionLotPo = null;
        if (EFundTrdType.BONDASSIGN.getCode().equals(req.getTrdType().getCode())) {

            // 通过份额ID获取对应头寸份额信息
            sellerPositionLotPo = positionLotRepository.findPositionLotByLotId(req.getLotId());
            newLot.setCrId(sellerPositionLotPo.getCrId());
            newLot.setContractId(sellerPositionLotPo.getContractId());
        } else {

            // new CrId
            String newCrId = this.generateCrId(retPosition.getPkgId(), "");
            newLot.setCrId(newCrId);
        }

        // 新增头寸份额信息
        PositionLotPo retLot = positionLotRepository.save(newLot);

        // 设置融资包交易日志
        pkgTradeJnlPo = ConverterService.convert(req, PkgTradeJnlPo.class);
        pkgTradeJnlPo.setLotId(retLot.getLotId());
        pkgTradeJnlPo.setCrId(retLot.getCrId());
        pkgTradeJnlPo.setTrdDt(req.getWorkDate());
        pkgTradeJnlPo.setCreateOpid(req.getCurrOpId());
        pkgTradeJnlPo.setCreateTs(new Date());

        // 新增融资包交易日志
        PkgTradeJnlPo retPkgTradeJnlPo = pkgTradeJnlRepository.save(pkgTradeJnlPo);
        return retPkgTradeJnlPo.getJnlNo();
    }

    private String generateCrId(String pkgId, String seqStr) {
        return pkgId + "-" + seqStr;
    }

    private String generateCntrctId(String pkgId, String acctNo) {
        StringBuffer strs = new StringBuffer();
        strs.append(pkgId);
        strs.append("-");
        strs.append(acctNo);
        return strs.toString();
    }

    /**
     * 手工还款
     * 
     * @param manualPaymentReq
     */
    public void manualPayment(ManualPaymentReq manualPaymentReq) {
        // 通过买方会员编号和融资包编号获取头寸
        PositionPo positionPo = positionRepository.findPositionByUserIdAndPkgId(manualPaymentReq.getBuyerUserId(),
                manualPaymentReq.getPkgId());

        // 通过份额ID获取对应头寸份额信息
        PositionLotPo positionLotPo = positionLotRepository.findPositionLotByLotId(manualPaymentReq.getLotId());

        // 头寸份额信息
        PositionLotPo targetPositionLotPo = ConverterService.convert(positionLotPo, PositionLotPo.class);

        // 头寸信息
        PositionPo targetPositionPo = ConverterService.convert(positionPo, PositionPo.class);

        if (manualPaymentReq.isLast()) {
            // 计算还款后份数
            Integer newUnit = AmtUtils.processNegativeAmt(
                    BigDecimal.valueOf(targetPositionPo.getUnit())
                            .subtract(BigDecimal.valueOf(positionLotPo.getUnit())), BigDecimal.ZERO).intValue();
            targetPositionPo.setUnit(newUnit);
            targetPositionPo.setLastMntOpid(manualPaymentReq.getCurrOpId());
            targetPositionPo.setLastMntTs(new Date());
            // 更新提前还款后头寸信息
            positionRepository.save(targetPositionPo);

            targetPositionLotPo.setUnit(Integer.valueOf(0));
        }

        BigDecimal paymentAmt = AmtUtils.processNegativeAmt(manualPaymentReq.getPaymentAmt(), BigDecimal.ZERO);
        BigDecimal oldAccumCrAmt = AmtUtils.processNegativeAmt(positionLotPo.getAccumCrAmt(), BigDecimal.ZERO);
        targetPositionLotPo.setAccumCrAmt(oldAccumCrAmt.add(paymentAmt));
        targetPositionLotPo.setLastMntOpid(manualPaymentReq.getCurrOpId());
        targetPositionLotPo.setLastMntTs(new Date());
        positionLotRepository.save(targetPositionLotPo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hengxin.platform.fund.service.atom.PositionService#getPositonsByPkgId (java.lang.String)
     */

    @Override
    public List<Position> getPositonsByPkgId(String pkgId) {
        List<Position> list = new ArrayList<Position>();
        List<PositionPo> pos = positionRepository.getPositionByPkgId(pkgId);
        if (pos != null && !pos.isEmpty()) {
            for (PositionPo po : pos) {
                list.add(ConverterService.convert(po, Position.class));
            }
        }
        return list;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hengxin.platform.fund.service.atom.PositionService# getProductPkgBuyersByPkgId(java.lang.String)
     */

    @Override
    public List<String> getProductPkgBuyersByPkgId(String pkgId) {
        return positionRepository.getUsersByPkgId(pkgId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hengxin.platform.fund.service.atom.PositionService#savePositions(java .util.List)
     */

    @Override
    public List<PositionPo> savePositions(List<PositionPo> positions) {
        return positionRepository.save(positions);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hengxin.platform.fund.service.atom.PositionService#getByPkgId(java. lang.String)
     */

    @Override
    public List<PositionPo> getByPkgId(String pkgId) {
        return positionRepository.getPositionByPkgId(pkgId);
    }

    @Override
    public List<PositionLotPo> getValidPostionLotsByPostionId(String positionId) {
        return positionLotRepository.findByPositionIdAndUnitGreaterThan(positionId, Integer.valueOf(0));
    }

    @Override
    public BigDecimal getUserReceivableTotalPrincipal(String userId) {
        BigDecimal crAmt = positionRepository.getUserReceivableTotalPrincipalByUserId(userId);
        return AmtUtils.processNullAmt(crAmt, BigDecimal.ZERO);
    }
}
