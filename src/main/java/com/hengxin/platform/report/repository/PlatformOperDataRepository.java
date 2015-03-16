package com.hengxin.platform.report.repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hengxin.platform.fund.util.DateUtils;
import com.hengxin.platform.product.domain.ProductPackage;
import com.hengxin.platform.report.dto.PlatformOperDataDto;

@Repository
public interface PlatformOperDataRepository extends JpaRepository<ProductPackage, String>{

	public PlatformOperDataDto getQuarterOperData(String searchDate);

}

class PlatformOperDataRepositoryImpl{
	@PersistenceContext
	private EntityManager em;

	/**
	 *1、成交金额
  		2、融资项目数
   		2.1 单笔0-100万的融资项目数
   		2.2 单笔100-300万的融资项目数
   		2.3 单笔大于300万的融资项目数
  	  4、项目平均融资额
  	  5、平均借款金额
  	  6、单一项目最高融资额
  	  7、单一项目平均持有人数
	 * @return
	 */
	public Object[] getOperBaseData(){
		String sql ="SELECT SUM(PKG_QUOTA) AS QUOTASUM, "
				+ "COUNT(PKG_ID) AS PKGAMT,COUNT (CASE WHEN PKG_QUOTA <= 1000000 THEN 1 ELSE NULL END) LESS100W,"
				+ "COUNT (CASE WHEN (PKG_QUOTA <= 3000000 AND PKG_QUOTA > 1000000) THEN 1 ELSE NULL END) LESS300W, "
				+ "COUNT (CASE WHEN (PKG_QUOTA > 3000000) THEN 1 ELSE NULL END) MORE300W, TRUNC( SUM(PKG_QUOTA)/COUNT(PKG_ID), 2) AS AVGQUOTA, "
				+ "TRUNC( SUM(PKG_QUOTA)/SUM(SUBS_USER_CT), 2) AS AVGLEND, MAX(PKG_QUOTA) AS MAXQUOTA, TRUNC( SUM(SUBS_USER_CT)/COUNT(PKG_ID), 2) AS AVGUSER  "
				+ "FROM  FP_PKG WHERE STATUS IN ('E','PN','WL','LC') ";
		Query query = em.createNativeQuery(sql.toString());
		Object[] obj = (Object[])query.getSingleResult();
		return obj;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getOperBaseDataMon(String startDateStr , String endDateStr){
		StringBuilder sql =new StringBuilder("SELECT nvl(SUM(PKG_QUOTA), 0.00) AS QUOTASUM, "
				+ "COUNT(PKG_ID) AS PKGAMT,COUNT (CASE WHEN PKG_QUOTA <= 1000000 THEN 1 ELSE NULL END) LESS100W,"
				+ "COUNT (CASE WHEN (PKG_QUOTA <= 3000000 AND PKG_QUOTA > 1000000) THEN 1 ELSE NULL END) LESS300W, "
				+ "COUNT (CASE WHEN (PKG_QUOTA > 3000000) THEN 1 ELSE NULL END) MORE300W, TRUNC( nvl(SUM(PKG_QUOTA)/COUNT(PKG_ID), 0.00), 2) AS AVGQUOTA, "
				+ "TRUNC( nvl(SUM(PKG_QUOTA)/SUM(SUBS_USER_CT), 0.00), 2) AS AVGLEND, nvl(MAX(PKG_QUOTA), 0.00) AS MAXQUOTA, TRUNC( nvl(SUM(SUBS_USER_CT)/COUNT(PKG_ID), 0.00), 2) AS AVGUSER  "
				+ "FROM  FP_PKG WHERE STATUS IN ('E','PN','WL','LC') ");
		List<String> params = new ArrayList<String>();
		if (StringUtils.isNotBlank(startDateStr)){
			sql.append(" and SIGNING_DT >= to_date(?, 'yyyy-MM-dd HH24:mi:ss') ");
			params.add(startDateStr);
		}if(StringUtils.isNotBlank(endDateStr)){
			sql.append(" and SIGNING_DT <= to_date(?, 'yyyy-MM-dd HH24:mi:ss') ");
			params.add(endDateStr);
		}
		Query query = em.createNativeQuery(sql.toString());
		for (int inx = 0; inx < params.size(); inx++) {
			query.setParameter(inx + 1, params.get(inx));
		}
		List<Object[]> objs = query.getResultList();
		return objs;
	}
	
	/**
	 * 3、投资人借出笔数
	单笔0-100万的融资项目数
	单笔0-100万的融资项目数
	单笔>300万的融资项目数
	 * @return
	 */
	public List<Object[]> getLendAmt(){
		String sql="select count(*), COUNT (CASE WHEN UNIT*LOT_BUY_PRICE <= 1000000 THEN 1  ELSE NULL END) less100w, "
				+ "COUNT (CASE WHEN (UNIT*LOT_BUY_PRICE <= 3000000 and UNIT*LOT_BUY_PRICE > 1000000) THEN 1 ELSE NULL END) less300w, "
				+ "COUNT (CASE WHEN (UNIT*LOT_BUY_PRICE > 3000000) THEN 1 ELSE NULL END) more300w  "
				+ "from ac_pkg_trade_jnl where TRD_TYPE = '01'";
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> lendObjs = query.getResultList();
		return lendObjs;
	}
	
	public List<Object[]> getLendAmtMon(String startDateStr , String endDateStr){
		StringBuilder sql= new StringBuilder("select count(*), COUNT (CASE WHEN UNIT*LOT_BUY_PRICE <= 1000000 THEN 1  ELSE NULL END) less100w, "
				+ "COUNT (CASE WHEN (UNIT*LOT_BUY_PRICE <= 3000000 and UNIT*LOT_BUY_PRICE > 1000000) THEN 1 ELSE NULL END) less300w, "
				+ "COUNT (CASE WHEN (UNIT*LOT_BUY_PRICE > 3000000) THEN 1 ELSE NULL END) more300w  "
				+ "from ac_pkg_trade_jnl where TRD_TYPE = '01'");
		List<String> params = new ArrayList<String>();	
		if(StringUtils.isNotBlank(startDateStr)){
			sql.append(" and TRD_DT >= to_date(?,'yyyy-MM-dd HH24:mi:ss') ");
			params.add(startDateStr);
		}
		if(StringUtils.isNotBlank(endDateStr)){
			sql.append(" and TRD_DT <= to_date(?,'yyyy-MM-dd HH24:mi:ss') ");
			params.add(endDateStr);
		}	
		Query query = em.createNativeQuery(sql.toString());
		for (int inx = 0; inx < params.size(); inx++) {
			query.setParameter(inx + 1, params.get(inx));
		}
		@SuppressWarnings("unchecked")
		List<Object[]> objs = query.getResultList();
		return objs;
	}
	

	/**
	 * 8、累计融资人数（人）
		企业数
		个人数
	 * @return
	 */
	public List<Object[]> getFinanerAmt(){
		String sql="select count(user_id),nvl(sum (case when (user_type = 'O') then 1 else null "
				+ "end), 0.00) corp, nvl(sum (case when (user_type = 'P') then 1 else null end), 0.00) per "
				+ "from (select distinct pkg.fncr_id as user_id, u.user_type as user_type "
				+ "from fp_pkg pkg left join um_user u on ( pkg.fncr_id = u.user_id)) ";
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> finanerAmts = query.getResultList();
		return finanerAmts;
	}
	
	public List<Object[]> getFinanerAmtMon(String startDateStr , String endDateStr){
		StringBuilder sql= new StringBuilder("select count(user_id),nvl(sum (case when (user_type = 'O') then 1 else null "
				+ "end), 0.00) corp, nvl(sum (case when (user_type = 'P') then 1 else null end), 0.00) per "
				+ "from (select distinct pkg.fncr_id as user_id, u.user_type as user_type "
				+ "from fp_pkg pkg left join um_user u on ( pkg.fncr_id = u.user_id) ");
		List<String> params = new ArrayList<String>();
		sql.append("where 1=1 ");
		if(StringUtils.isNotBlank(startDateStr)){
			sql.append(" and pkg.SIGNING_DT >= to_date(?, 'yyyy-MM-dd HH24:mi:ss') ");
			params.add(startDateStr);
		}
		if(StringUtils.isNotBlank(endDateStr)){
			sql.append(" and pkg.SIGNING_DT <= to_date(?, 'yyyy-MM-dd HH24:mi:ss') ");
			params.add(endDateStr);
		}
		sql.append(" )");
		Query query = em.createNativeQuery(sql.toString());
		for (int inx = 0; inx < params.size(); inx++) {
			query.setParameter(inx + 1, params.get(inx));
		}
		@SuppressWarnings("unchecked")
		List<Object[]> objs = query.getResultList();
		return objs;
	}
	
	/**
	 * 9、累计投资人数
	 */
	public Integer getInvestorAmt(){
		String sql = "select count(DISTINCT jnl.BUYER_USER_ID) FROM ac_pkg_trade_jnl jnl where jnl.TRD_TYPE = '01'";
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object> investorrAmt = query.getResultList();
		return Integer.valueOf(investorrAmt.get(0).toString());
	}
	
	public Integer getInvestorAmtMon(String startDateStr , String endDateStr){
		StringBuilder sql = new StringBuilder("select count(DISTINCT jnl.BUYER_USER_ID) FROM ac_pkg_trade_jnl jnl where jnl.TRD_TYPE = '01'");
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(startDateStr)){
			sql.append(" and TRD_DT >= to_date(?,'yyyy-MM-dd HH24:mi:ss') ");
			params.add(startDateStr);
		}
		if(StringUtils.isNotBlank(endDateStr)){
			sql.append(" and TRD_DT <= to_date(?,'yyyy-MM-dd HH24:mi:ss') ");
			params.add(endDateStr);
		}
		
		Query query = em.createNativeQuery(sql.toString());
		for (int inx = 0; inx < params.size(); inx++) {
			query.setParameter(inx + 1, params.get(inx));
		}
		@SuppressWarnings("unchecked")
		List<Object> obj = query.getResultList();
		return Integer.valueOf(obj.get(0).toString());
	}
	/**
	 * 10、融资人年化平均综合成本
	 */
	public BigDecimal getFinanAvgRate(){
		String sql="select to_char(sum(total)/count(*),'0.99999999') as avgrate from  "
				+ "(select vm.pkg_id , (sum (fee_rt)*12 + min(rate) ) as total "
				+ "from (select pkg.pkg_id , prod.rate, fee.fee_id ,fee.fee_rt from fp_pkg pkg ,fp_prod prod, fp_prod_fee fee  "
				+ "where pkg.prod_id = prod.prod_id and prod.prod_id = fee.prod_id order by pkg_id asc) vm group by vm.pkg_id )vmv";
		Query query = em.createNativeQuery(sql.toString());
		Object investorrAmt = (Object) query.getSingleResult();
		return BigDecimal.valueOf(Double.valueOf(investorrAmt.toString()));
	}
	//TODO  
	public BigDecimal getFinanAvgRateMon(String startDateStr , String endDateStr){
		String sql=" SELECT NVL(TO_CHAR(SUM(total)/COUNT(*),'0.99999999'), 0.00) AS avgrate "
				+" FROM "
				+" (SELECT vm.pkg_id , "
				+" (SUM (fee_rt)*12 + MIN(rate)) AS total "
				+" FROM "
				+" (SELECT pkg.pkg_id , "
				+" prod.rate,"
				+" fee.fee_id , "
				+" fee.fee_rt "
				+" FROM fp_pkg pkg , "
				+" fp_prod prod, "
				+" fp_prod_fee fee "
				+" WHERE pkg.prod_id = prod.prod_id "
				+" AND prod.prod_id = fee.prod_id "
				+" AND pkg.SIGNING_DT >= to_date(?,'yyyy-MM-dd HH24:mi:ss') "
				+" AND pkg.SIGNING_DT <= to_date(?,'yyyy-MM-dd HH24:mi:ss') "
				+" ORDER BY pkg_id ASC "
				+" ) vm "
				+" GROUP BY vm.pkg_id "
				+" )vmv ";
		
		List<Object[]> obj = createSQlQuery(startDateStr, endDateStr, sql);
		return (BigDecimal)obj.get(0)[0];
	}
	
	/**
	 * 11、投资人年化收益
	 */
	//TODO 
	public BigDecimal getInvesProfitPerYear(){
		String sql= "";
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object> obj = query.getResultList();
		return (BigDecimal)obj.get(0);
	}
	
	private BigDecimal getInvesProfitPerYearMon(String startDateStr, String endDateStr) {
		String sql= "";
		List<Object[]> obj = createSQlQuery(startDateStr, endDateStr, sql);
		return (BigDecimal)obj.get(0)[0];
	}
	
	/**
	 * 12 平均借款期限
		小于3个月
		小于6个月
		小于12个月
		大于12个月
	 */
	public List<Object[]> getAvgLendTime(){
		String sql= "SELECT NVL(TRUNC( SUM(DAYS)/COUNT(*)/30, 2),0), NVL(SUM (CASE WHEN (DAYS <= 90) THEN 1 ELSE NULL END),0) AS LESS3, "
				+ "NVL(SUM (CASE WHEN (DAYS > 90 AND DAYS <= 180) THEN 1 ELSE NULL END),0) LESS6, NVL(SUM (CASE WHEN (DAYS > 180 AND DAYS <= 360) THEN 1 "
				+ "ELSE NULL END),0) LESS12, NVL(SUM (CASE WHEN (DAYS > 360) THEN 1 ELSE NULL  END),0) MORE12 FROM(SELECT  PROD.TERM_DAYS DAYS  "
				+ "FROM FP_PKG PKG LEFT JOIN FP_PROD PROD ON PKG.PROD_ID = PROD.PROD_ID WHERE PKG.STATUS IN ('E','PN','WL','LC'))";
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> objs = query.getResultList();
		return objs;
	}
	
	public List<Object[]> getAvgLendTimeMon(String startDateStr , String endDateStr){
		String sql= "SELECT NVL(TRUNC( SUM(DAYS)/COUNT(*)/30, 2),0), NVL(SUM (CASE WHEN (DAYS <= 90) THEN 1 ELSE NULL END),0) AS LESS3, "
				+ "NVL(SUM (CASE WHEN (DAYS > 90 AND DAYS <= 180) THEN 1 ELSE NULL END),0) LESS6, NVL(SUM (CASE WHEN (DAYS > 180 AND DAYS <= 360) THEN 1 "
				+ "ELSE NULL END),0) LESS12, NVL(SUM (CASE WHEN (DAYS > 360) THEN 1 ELSE NULL  END),0) MORE12 FROM(SELECT  PROD.TERM_DAYS DAYS  "
				+ "FROM FP_PKG PKG LEFT JOIN FP_PROD PROD ON PKG.PROD_ID = PROD.PROD_ID WHERE PKG.STATUS IN ('E','PN','WL','LC')) and pkg.SIGNING_DT >= to_date(?,'yyyy-MM-dd HH24:mi:ss')  "
				+ "and pkg.SIGNING_DT <= to_date(?,'yyyy-MM-dd HH24:mi:ss'))";
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> objs = query.getResultList();
		return objs;
	}
	
	/**
	 * 13、借款种类分布
 		有担保机制
 		其他风险防
 		无担保机制的
	 */
	public List<Object[]> getBorrowSpeciesDistribution(){
		String sql ="select nvl(trunc( sum (CASE WHEN (wrty in('A','B')) THEN 1  ELSE NULL  END)/count(*),2),0) has,  "
				+ "nvl(trunc( sum (CASE WHEN (wrty = 'C') THEN 1 ELSE NULL END)/count(*),2),0) other, nvl(trunc( sum (CASE WHEN (wrty = 'D') THEN 1 "
				+ " ELSE NULL END)/count(*),2),0) nohas from(select  prod.WRTY_TYPE wrty from FP_PKG pkg left "
				+ "join fp_prod prod on pkg.prod_id = prod.prod_id where pkg.status in ('E','PN','WL','LC'))";
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> objs = query.getResultList();
		return objs;
	}

	public List<Object[]> getBorrowSpeciesDistributionMon(String startDateStr , String endDateStr){
		String sql = "select nvl(trunc( sum (CASE WHEN (wrty in('A','B')) THEN 1  "
				+ "ELSE NULL END)/count(*),2),0) has, nvl(trunc( sum (CASE WHEN (wrty = 'C') THEN 1 ELSE NULL "
				+ "END)/count(*),2),0) other, nvl(trunc( sum (CASE WHEN (wrty = 'D') THEN 1 ELSE NULL END)/count(*),2),0) nohas "
				+ "from(select  prod.WRTY_TYPE wrty from FP_PKG pkg left join fp_prod prod on pkg.prod_id = prod.prod_id where pkg.status in ('E','PN','WL','LC') and pkg.SIGNING_DT >= to_date(?,'yyyy-MM-dd HH24:mi:ss')  "
				+ "and pkg.SIGNING_DT <= to_date(?,'yyyy-MM-dd HH24:mi:ss'))";
		return createSQlQuery(startDateStr, endDateStr, sql);
	}

	public List<Object[]> createSQlQuery(String startDateStr, String endDateStr, String sql) {
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, startDateStr);
		query.setParameter(2, endDateStr);
		@SuppressWarnings("unchecked")
		List<Object[]> objs = query.getResultList();
		return objs;
	}
	
	/**
	 * 运营累计数据
	 */
	public PlatformOperDataDto getQuarterOperData(String searchDateStr) {
		Date searchDate = DateUtils.getDate(searchDateStr, "yyyy-MM-dd");
		String startDateStr = getFirstDay(searchDate);
		String endDateStr = getLastDay(searchDate);
		PlatformOperDataDto dto = new PlatformOperDataDto();
		
		//第一个到第六个字段（不包第三个）
		Object[] obj = getOperBaseData();
		dto.setQuotoAmt((BigDecimal)obj[0]);		//1、成交金额
		dto.setPkgAmt(Integer.valueOf(obj[1].toString()));				//2、融资项目数
		dto.setPkgAmtLessOne(Integer.valueOf(obj[2].toString()));		//单笔0-100万的融资项目数
		dto.setPkgAmtLessThree(Integer.valueOf(obj[3].toString()));		//单笔0-300万的融资项目数
		dto.setPkgAmtMoreThree(Integer.valueOf(obj[4].toString()));		//单笔>300万的融资项目数
		dto.setAvgPkgAmt((BigDecimal)obj[5]);		//4.项目平均融资额
		dto.setAvgLendAmt((BigDecimal)obj[6]);		//5、平均借款金额
		dto.setMaxPkgAmt((BigDecimal)obj[7]);		//6、单一项目最高融资额
		
		List<Object[]> objMons = getOperBaseDataMon(startDateStr, endDateStr);
		Object[] objMon = objMons.get(0);
		dto.setQuotoAmtMon((BigDecimal)objMon[0]);		//1、成交金额
		dto.setPkgAmtMon(Integer.valueOf(objMon[1].toString()));				//2、融资项目数
		dto.setPkgAmtLessOneMon(Integer.valueOf(objMon[2].toString()));		//单笔0-100万的融资项目数
		dto.setPkgAmtLessThreeMon(Integer.valueOf(objMon[3].toString()));		//单笔0-300万的融资项目数
		dto.setPkgAmtMoreThreeMon(Integer.valueOf(objMon[4].toString()));		//单笔>300万的融资项目数
		dto.setAvgPkgAmtMon((BigDecimal)objMon[5]);		//4.项目平均融资额
		dto.setAvgLendAmtMon((BigDecimal)objMon[6]);		//5、平均借款金额
		dto.setMaxPkgAmtMon((BigDecimal)objMon[7]);		//6、单一项目最高融资额
		
		//3、投资人借出笔数
		List<Object[]> lendObjs = getLendAmt();
		Object[] lendObj = lendObjs.get(0);
		dto.setLendAmt(Integer.valueOf(lendObj[0].toString()));
		dto.setLendAmtLessOne(Integer.valueOf(lendObj[1].toString()));
		dto.setLendAmtLessThree(Integer.valueOf(lendObj[2].toString()));
		dto.setLendAmtMoreThree(Integer.valueOf(lendObj[3].toString()));
		
		List<Object[]> lendObjMons = getLendAmtMon(startDateStr, endDateStr);
		Object[] lendObjMon = lendObjMons.get(0);
		dto.setLendAmt(Integer.valueOf(lendObjMon[0].toString()));
		dto.setLendAmtLessOne(Integer.valueOf(lendObjMon[1].toString()));
		dto.setLendAmtLessThree(Integer.valueOf(lendObjMon[2].toString()));
		dto.setLendAmtMoreThree(Integer.valueOf(lendObjMon[3].toString()));
		
		//8、累计融资人数
		List<Object[]> obj8s = getFinanerAmt();
		Object[] obj8 = obj8s.get(0);
		dto.setFinancingAmt(Integer.valueOf(obj8[0].toString()));
		dto.setFinancingComAmt(Integer.valueOf(obj8[1].toString()));
		dto.setFinancingPerAmt(Integer.valueOf(obj8[2].toString()));
		
		List<Object[]> objMon8s = getFinanerAmtMon(startDateStr, endDateStr);
		Object[] objMon8 = objMon8s.get(0);
		dto.setFinancingAmt(Integer.valueOf(objMon8[0].toString()));
		dto.setFinancingComAmt(Integer.valueOf(objMon8[1].toString()));
		dto.setFinancingPerAmt(Integer.valueOf(objMon8[2].toString()));
		
		//9、累计投资人数
		dto.setInvestorAmt(getInvestorAmt());
		dto.setInvestorAmtMon(getInvestorAmtMon(startDateStr, endDateStr));
		
		//10、融资人年化平均综合成本
		dto.setPkgCostPerYear(getFinanAvgRate());
		dto.setPkgCostPerYearMon(getFinanAvgRateMon(startDateStr, endDateStr));
		
		//11、投资人年化平均收益
		dto.setInvesProfitPerYear(getInvesProfitPerYear());
		dto.setInvesProfitPerYearMon(getInvesProfitPerYearMon(startDateStr, endDateStr));
		
		//12、平均借款期限
		List<Object[]> obj12s = getAvgLendTime();
		Object[] obj12 = obj12s.get(0);
		dto.setAvgMonth((BigDecimal)obj12[0]);
		dto.setAvgMonthAmtLessThree(Integer.valueOf(obj12[1].toString()));
		dto.setAvgMonthAmtLessSix(Integer.valueOf(obj12[2].toString()));
		dto.setAvgMonthAmtLessTwelve(Integer.valueOf(obj12[3].toString()));
		dto.setAvgMonthAmtMoreTwelve(Integer.valueOf(obj12[4].toString()));
		
		List<Object[]> objMon12s = getAvgLendTimeMon(startDateStr, endDateStr);
		Object[] objMon12 = objMon12s.get(0);
		dto.setAvgMonth((BigDecimal)objMon12[0]);
		dto.setAvgMonthAmtLessThree(Integer.valueOf(objMon12[1].toString()));
		dto.setAvgMonthAmtLessSix(Integer.valueOf(objMon12[2].toString()));
		dto.setAvgMonthAmtLessTwelve(Integer.valueOf(objMon12[3].toString()));
		dto.setAvgMonthAmtMoreTwelve(Integer.valueOf(objMon12[4].toString()));
		
		//13、借款种类分布
		List<Object[]> obj13s = getBorrowSpeciesDistribution();
		Object[] obj13 = obj13s.get(0);
		dto.setPkgByGuarantee((BigDecimal)obj13[0]);
		dto.setPkgByRisk((BigDecimal)obj13[1]);
		dto.setPkgByNoGua((BigDecimal)obj13[2]);
		
		List<Object[]> objMon13s = getBorrowSpeciesDistributionMon(startDateStr, endDateStr);
		Object[] objMon13 = objMon13s.get(0);
		dto.setPkgByGuarantee((BigDecimal)objMon13[0]);
		dto.setPkgByRisk((BigDecimal)objMon13[1]);
		dto.setPkgByNoGua((BigDecimal)objMon13[2]);
		
		return dto;
	}

	/**
	 * 当月第一天
	 * @param theDate
	 * @return
	 */
	public String getFirstDay(Date theDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        return str.toString();

    }
	
	 /**
     * 当月最后一天
     * @return
     */
	public String getLastDay(Date theDate) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(theDate);
        calendar.set(calendar.get(Calendar.YEAR),
                     calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return new StringBuilder(df.format(calendar.getTime())).append(" 23:59:59").toString();
    }

}