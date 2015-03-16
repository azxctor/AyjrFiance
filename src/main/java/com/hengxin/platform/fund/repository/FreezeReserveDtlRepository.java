package com.hengxin.platform.fund.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.entity.FreezeReserveDtlPo;

@Repository
public interface FreezeReserveDtlRepository extends
		PagingAndSortingRepository<FreezeReserveDtlPo, String>,
		JpaSpecificationExecutor<FreezeReserveDtlPo> {

	FreezeReserveDtlPo findFreezeReserveDtlByJnlNo(String jnlNo);	
	
	@Query(value = "select nvl(sum(trx_amt),0.00),count(1) "
		     + "  from ac_fnr_dtl tl,"
		     + "       ac_acct ct,"
		     + "       um_user us"
		     + " where tl.acct_no = ct.acct_no"
		     + "   and ct.user_id = us.user_id"
		     + "   and tl.acct_no = nvl(?1, tl.acct_no)"
		     + "   and us.name = nvl(?2, us.name)"
		     + "   and tl.status = nvl(?3, tl.status)"
		     + "   and tl.use_type = nvl(?4, tl.use_type)"
		     + "   and (trim(?5) is null or to_char(tl.effect_dt,'yyyy-MM-dd')>=?5)"
		     + "   and (trim(?6) is null or to_char(tl.effect_dt,'yyyy-MM-dd')<=?6)"
		     + "" , nativeQuery = true)
	List<Object[]> getSumData(String acctNo, String userName, String status,
			String useType, String startDate, String endDate);
}
