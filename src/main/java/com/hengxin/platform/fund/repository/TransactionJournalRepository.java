package com.hengxin.platform.fund.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hazelcast.util.StringUtil;
import com.hengxin.platform.fund.entity.TransactionJournalPo;
import com.hengxin.platform.fund.enums.EFundUseType;
import com.hengxin.platform.fund.util.DateUtils;

/**
 * @author qimingzou
 *
 */
public interface TransactionJournalRepository extends JpaRepository<TransactionJournalPo, String>, JpaSpecificationExecutor<TransactionJournalPo> {
	public BigDecimal[] getTransactionJournalNumSumArr(String authzdCtrId, String keyWord, EFundUseType useType, Date beginDate, Date endDate, String agent, String agentName);
}

class TransactionJournalRepositoryImpl {
	@PersistenceContext
	private EntityManager em;

	public BigDecimal[] getTransactionJournalNumSumArr(String authzdCtrId, String keyWord, EFundUseType useType, Date beginDate, Date endDate, String agent, String agentName) {
		StringBuilder sql = new StringBuilder("select nvl(sum(case jnl.pay_recv_flg when 'R' then jnl.trx_amt else 0 end),0) receiveamt").append(",nvl(sum(case jnl.pay_recv_flg when 'P' then jnl.trx_amt else 0 end),0) payamt from v_invstr_trx_jnl jnl");
		StringBuilder wsql = new StringBuilder();

		List<String> params = new ArrayList<String>();

		wsql.append(" where jnl.owner_authzd_ctr_id = ? ");
		params.add(authzdCtrId);

		if (!StringUtil.isNullOrEmpty(keyWord)) {
			wsql.append(" and (jnl.acct_no like ? or jnl.name like ?) ");
			params.add("%" + keyWord.trim() + "%");
			params.add("%" + keyWord.trim() + "%");
		}

		if (useType != null && useType != EFundUseType.INOUTALL) {
			wsql.append(" and jnl.use_type = ? ");
			params.add(useType.getCode());
		}

		if (!StringUtil.isNullOrEmpty(agent)) {
			wsql.append(" and jnl.agent like ? ");
			params.add("%" + agent.trim() + "%");
		}

		if (!StringUtil.isNullOrEmpty(agentName)) {
			wsql.append(" and jnl.agent_name like ? ");
			params.add("%" + agentName.trim() + "%");
		}

		if (beginDate != null && endDate != null) {
			wsql.append(" and to_char(jnl.trx_dt,'yyyymmdd') between ? and ? ");
			params.add(DateUtils.formatDate(beginDate, "yyyyMMdd"));
			params.add(DateUtils.formatDate(endDate, "yyyyMMdd"));
		} else {
			if (beginDate != null) {
				wsql.append(" and to_char(jnl.trx_dt,'yyyymmdd') >= ? ");
				params.add(DateUtils.formatDate(beginDate, "yyyyMMdd"));
			}
			if (endDate != null) {
				wsql.append(" and to_char(jnl.trx_dt,'yyyymmdd') <= ? ");
				params.add(DateUtils.formatDate(endDate, "yyyyMMdd"));
			}
		}

		sql.append(wsql);
		Query query = em.createNativeQuery(sql.toString());

		for (int inx = 0; inx < params.size(); inx++) {
			query.setParameter(inx + 1, params.get(inx));
		}

		Object[] obj = (Object[]) query.getSingleResult();
		BigDecimal[] result = new BigDecimal[obj.length];
		for (int iter = 0; iter < obj.length; iter++) {
			result[iter] = new BigDecimal(obj[iter].toString());
		}
		return result;
	}

}