package com.hengxin.platform.netting.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hengxin.platform.common.entity.id.IdUtil;
import com.hengxin.platform.common.enums.EErrorCode;
import com.hengxin.platform.common.exception.ShowStackTraceException;
import com.hengxin.platform.fund.enums.ECashPool;
import com.hengxin.platform.fund.util.DateUtils;

@Service
public class NettingOrderService {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(NettingOrderService.class);

	private static final String SUCCEED = "0";
					
	@PersistenceContext(unitName = "default")
	private EntityManager em;
	
	@Transactional
	public void doNetting(Date trxDate, String currOpId){
		String eventId = IdUtil.produce();
		String servProv = ECashPool.ESCROW_EBC.getCode();
		String dateStr = DateUtils.formatDate(trxDate, "yyyy-MM-dd");
		StoredProcedureQuery procedure = em
				.createStoredProcedureQuery("pkg_fund_netting.recv_pay_order_netting");
		procedure.registerStoredProcedureParameter(1, String.class,
				ParameterMode.IN);
		procedure.setParameter(1, eventId);
		procedure.registerStoredProcedureParameter(2, Date.class,
				ParameterMode.IN);
		procedure.setParameter(2, trxDate);
		procedure.registerStoredProcedureParameter(3, String.class,
				ParameterMode.IN);
		procedure.setParameter(3, servProv);
		procedure.registerStoredProcedureParameter(4, String.class,
				ParameterMode.IN);
		procedure.setParameter(4, currOpId);
		procedure.registerStoredProcedureParameter(5, String.class,
				ParameterMode.OUT);
		procedure.registerStoredProcedureParameter(6, String.class,
				ParameterMode.OUT);
		procedure.execute();
		String retCode = (String) procedure.getOutputParameterValue(5);
		String retMsg = (String) procedure.getOutputParameterValue(6);
		LOG.debug("执行结果, retCode=" + retCode + ",retMsg=" + retMsg);
		if (StringUtils.equals(SUCCEED, retCode)) {
			LOG.debug("轧差处理成功， trxDate is {}", dateStr);
		}else{
			LOG.debug("轧差处理失败， trxDate is {}", dateStr);
			throw new ShowStackTraceException(EErrorCode.COMM_INTERNAL_ERROR,"轧差失败:"+retMsg);
		}
	}
	
}
