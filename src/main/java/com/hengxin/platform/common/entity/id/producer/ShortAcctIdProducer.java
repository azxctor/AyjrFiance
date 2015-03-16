package com.hengxin.platform.common.entity.id.producer;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import com.hengxin.platform.common.entity.id.Context;
import com.hengxin.platform.common.entity.id.DefaultIdProducer;
import com.hengxin.platform.common.entity.id.SequenceConsumer;
import com.hengxin.platform.common.entity.id.ShortAcctIdContext;
import com.hengxin.platform.common.util.ApplicationContextUtil;
import com.hengxin.platform.common.util.IdCheckUtil;
import com.hengxin.platform.member.domain.Agency;
import com.hengxin.platform.member.repository.AgencyRepository;

public class ShortAcctIdProducer extends DefaultIdProducer<ShortAcctIdContext> {

	private static final String ACCT_NO_FORMAT = "{0}{1}";
	private static final String SHORT_ACCT = "SHORT_ACCT";
	private static final String DEFAULT_PREFIX = "88";
	private static final int SUB_ID_CARD_LENGTH = 2;
	private static final int SEQ_LENGTH = 3;
	
	private String idCardPrefix;

	@Override
	protected String getSequenceType() {
		return SequenceConsumer.Interval.NO.value();
	}
	
	@Override
	protected String getSequenceName(ShortAcctIdContext entity) {
		return SHORT_ACCT;
	}

	@Override
	protected String getSequenceNameSuffix(Context ctx) {
		return "_"+getAcctNoPrefix(ctx);
	}

	@Override
	protected Object doProduceId(Context ctx) {
		String acctNo = MessageFormat.format(ACCT_NO_FORMAT, getAcctNoPrefix(ctx), formatSeq(ctx));
		return acctNo + IdCheckUtil.getCheckDigit(acctNo);
	}

	@Override
	protected int getSeqLength() {
		return SEQ_LENGTH;
	}

	@Override
	protected boolean cacheEnabled() {
		return false;
	}
	
	private String getAcctNoPrefix(Context ctx){
		if(StringUtils.isBlank(idCardPrefix)){
			Agency agency = null;
			if(ctx.getParam()==null){
				agency = getAgency(ctx);
				if(agency!=null){
					ctx.setParam(agency);
				}
			}
			else{
				agency = (Agency)ctx.getParam();
			}
			if(agency!=null&&StringUtils.isNotBlank(agency.getOrgLegalPersonIdCardNumber())){
				String idCardNo = agency.getOrgLegalPersonIdCardNumber();
				setIdCardPrefix(StringUtils.substring(idCardNo, 0, SUB_ID_CARD_LENGTH));
			}
		}
		return StringUtils.defaultIfBlank(idCardPrefix, DEFAULT_PREFIX);
	}
	
	private String getUserId(Context ctx){
		return ((ShortAcctIdContext)ctx.getEntity()).getUserId();
	}
	
	private Agency getAgency(Context ctx){
		AgencyRepository repo = ApplicationContextUtil
				.getBean(AgencyRepository.class);
		return repo.findOne(getUserId(ctx));
	}

	public void setIdCardPrefix(String idCardPrefix) {
		this.idCardPrefix = idCardPrefix;
	}

}
