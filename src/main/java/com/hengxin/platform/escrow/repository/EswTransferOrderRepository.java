package com.hengxin.platform.escrow.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.escrow.entity.EswTransferOrderPo;
import com.hengxin.platform.escrow.enums.EOrderStatusEnum;
import com.hengxin.platform.fund.util.DateUtils;

@Repository
public interface EswTransferOrderRepository extends JpaRepository<EswTransferOrderPo, String>, JpaSpecificationExecutor<EswTransferOrderPo> {

	List<EswTransferOrderPo> findByTrxDateAndStatusOrderByCreateTsAsc(Date trxDate, EOrderStatusEnum status);
   
	BigDecimal getPayTransferOrderSumAmt(String payerAcctNo, String payeeAcctNo, Date trxDate, EOrderStatusEnum status);

	BigDecimal getRecTransferOrderSumAmt(String payerAcctNo, String payeeAcctNo, Date trxDate, EOrderStatusEnum status);
}

class EswTransferOrderRepositoryImpl {
	@PersistenceContext
	private EntityManager em;

	public BigDecimal getPayTransferOrderSumAmt(String payerAcctNo, String payeeAcctNo, Date trxDate, EOrderStatusEnum status) {
		StringBuilder sql = new StringBuilder(" select nvl(sum(ad.TRX_AMT),0.00) from AC_ESW_TSFR_ORD ad ");
		StringBuilder wsql = new StringBuilder();

		List<String> params = new ArrayList<String>();
		String supportSettleCurrentAcctNo = CommonBusinessUtil.getSupportSettleCurrentAcctNo();
		wsql.append(" where ad.PAYER_NO != " + supportSettleCurrentAcctNo+ " ");
		
		if (StringUtils.isNotBlank(payerAcctNo)) {
			wsql.append(" and ad.PAYER_NO = ? ");
			params.add(payerAcctNo.trim());
		}

		if (StringUtils.isNotBlank(payeeAcctNo)) {
			wsql.append(" and ad.PAYEE_NO = ? ");
			params.add(payeeAcctNo.trim());
		}

		if (null != trxDate) {
			String trxDateStr = DateUtils.formatDate(trxDate, "yyyy-MM-dd");
			wsql.append(" and ad.TRX_DT = to_date(?,'yyyy-MM-dd') ");
			params.add(trxDateStr);
		}

		if (null != status) {
			wsql.append(" and ad.STATUS = ? ");
			params.add(status.getCode());
		}
		
		sql.append(wsql);
		Query query = em.createNativeQuery(sql.toString());
		for (int inx = 0; inx < params.size(); inx++) {
			query.setParameter(inx + 1, params.get(inx));
		}
		
		@SuppressWarnings("unchecked")
		List<Object> list = query.getResultList();
		BigDecimal result = (BigDecimal) list.get(0);
		return result;
	}

	public BigDecimal getRecTransferOrderSumAmt(String payerAcctNo, String payeeAcctNo, Date trxDate, EOrderStatusEnum status) {
		StringBuilder sql = new StringBuilder(" select nvl(sum(ad.TRX_AMT),0.00) from AC_ESW_TSFR_ORD ad ");
		StringBuilder wsql = new StringBuilder();

		List<String> params = new ArrayList<String>();
		String supportSettleCurrentAcctNo = CommonBusinessUtil.getSupportSettleCurrentAcctNo();
		wsql.append(" where ad.PAYEE_NO != " + supportSettleCurrentAcctNo + " ");
		
		if (StringUtils.isNotBlank(payerAcctNo)) {
			wsql.append(" and ad.PAYER_NO = ? ");
			params.add(payerAcctNo.trim());
		}

		if (StringUtils.isNotBlank(payeeAcctNo)) {
			wsql.append(" and ad.PAYEE_NO = ? ");
			params.add(payeeAcctNo.trim());
		}

		if (null != trxDate) {
			String trxDateStr = DateUtils.formatDate(trxDate, "yyyy-MM-dd");
			wsql.append(" and ad.TRX_DT = to_date(?,'yyyy-MM-dd') ");
			params.add(trxDateStr);
		}

		if (null != status) {
			wsql.append(" and ad.STATUS = ? ");
			params.add(status.getCode());
		}
		
		sql.append(wsql);
		Query query = em.createNativeQuery(sql.toString());
		for (int inx = 0; inx < params.size(); inx++) {
			query.setParameter(inx + 1, params.get(inx));
		}
		
		@SuppressWarnings("unchecked")
		List<Object> list = query.getResultList();
		BigDecimal result = (BigDecimal) list.get(0);
		return result;
	}
}
