package com.hengxin.platform.common.entity.id.producer;

import static org.apache.commons.lang.StringUtils.leftPad;
import static org.apache.commons.lang.StringUtils.substring;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.hengxin.platform.common.entity.id.Context;
import com.hengxin.platform.common.entity.id.DefaultIdProducer;
import com.hengxin.platform.common.entity.id.SequenceConsumer;
import com.hengxin.platform.common.util.ApplicationContextUtil;
import com.hengxin.platform.common.util.IdCheckUtil;
import com.hengxin.platform.fund.entity.AcctPo;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.member.repository.UserRepository;
import com.hengxin.platform.security.entity.UserPo;

/**
 * 
 * 
 * @author yeliangjin
 * 
 */
public class AcctIdProducer extends DefaultIdProducer<AcctPo> {

	private static final String ACCT_NO_FORMAT = "88{0}{1}"; //"88{0}{1}{2}";
	private static final int SEQ_LENGTH = 6;
	private static final Map<String, String> USER_TYPE_CODE = new HashMap<String, String>();
	static {
		USER_TYPE_CODE.put(EUserType.PERSON.getCode(), "01");
		USER_TYPE_CODE.put(EUserType.ORGANIZATION.getCode(), "02");
	}

	@Override
	protected String getSequenceType() {
		return SequenceConsumer.Interval.NO.value();
	}

	@Override
	protected String getSequenceNameSuffix(Context ctx) {
		return "_" + getRegion(ctx);
	}

	@Override
	protected Object doProduceId(Context ctx) {
//		String acctNo = MessageFormat.format(ACCT_NO_FORMAT, getRegion(ctx),
//				getType(ctx), formatSeq(ctx));
		String acctNo = MessageFormat.format(ACCT_NO_FORMAT, getRegion(ctx), formatSeq(ctx));
		return acctNo + IdCheckUtil.getCheckDigit(acctNo);
	}

	@Override
	protected int getSeqLength() {
		return SEQ_LENGTH;
	}

	private UserPo getUser(Context ctx) {
		if (ctx.getParam() == null) {
			UserRepository userRepo = ApplicationContextUtil
					.getBean(UserRepository.class);
			UserPo user = userRepo.findOne(getEntity(ctx).getUserId());
			ctx.setParam(user);
		}
		UserPo user = (UserPo) ctx.getParam();
		if (user == null) {
			throw new IllegalArgumentException("no user found: "
					+ getEntity(ctx).getUserId());
		}
		return user;
	}

	private String getRegion(Context ctx) {
		String region = getUser(ctx).getRegion();
		if (region == null) {
			throw new IllegalArgumentException(
					"region of the user must not be null: "
							+ getUser(ctx).getUserId());
		}
		return leftPad(substring(region, 0, 4), 4, '0');
	}

	@SuppressWarnings("unused")
	private String getType(Context ctx) {
		EUserType type = getUser(ctx).getType();
		if (type == null) {
			throw new IllegalArgumentException(
					"type of the user must not be null: "
							+ getUser(ctx).getUserId());
		}
		return USER_TYPE_CODE.get(type.getCode());
	}

	@Override
	protected boolean cacheEnabled() {
		return false;
	}

}
