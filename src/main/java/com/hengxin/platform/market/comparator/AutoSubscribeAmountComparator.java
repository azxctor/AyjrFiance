package com.hengxin.platform.market.comparator;

import java.util.Comparator;

import com.hengxin.platform.market.dto.AutoSubscribeCandidateDto;

/**
 * 基于最大可用投资额排序
 * 
 */
public class AutoSubscribeAmountComparator implements
		Comparator<AutoSubscribeCandidateDto> {

	@Override
	public int compare(AutoSubscribeCandidateDto o1,
			AutoSubscribeCandidateDto o2) {
		return o1.getMaxAmount().compareTo(o2.getMaxAmount());
	}
}
