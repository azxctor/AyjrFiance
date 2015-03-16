package com.hengxin.platform.fund.service.atom;

import java.math.BigDecimal;
import java.util.List;

import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.fund.domain.Position;
import com.hengxin.platform.fund.dto.biz.req.ManualPaymentReq;
import com.hengxin.platform.fund.dto.biz.req.atom.PositionChangeReq;
import com.hengxin.platform.fund.entity.PositionLotPo;
import com.hengxin.platform.fund.entity.PositionPo;

/**
 * 头寸. 
 *
 */
public interface PositionService {

	/**
	 * 债权头寸变更
	 * 
	 * @param positionChangeReq
	 * @throws AmtParamInvalidException
	 */
	String positionChange(PositionChangeReq positionChangeReq)
			throws BizServiceException;
	
	void manualPayment(ManualPaymentReq manualPaymentReq);

	List<Position> getPositonsByPkgId(String pkgId);

	List<String> getProductPkgBuyersByPkgId(String pkgId);

	List<PositionPo> savePositions(List<PositionPo> positions);

	List<PositionPo> getByPkgId(String pkgId);
	
	List<PositionLotPo> getValidPostionLotsByPostionId(String positionId);
	
	BigDecimal getUserReceivableTotalPrincipal(String userId);
}
