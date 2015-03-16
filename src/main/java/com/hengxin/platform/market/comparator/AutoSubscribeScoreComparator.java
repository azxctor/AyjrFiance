package com.hengxin.platform.market.comparator;

import java.util.Comparator;

import com.hengxin.platform.market.dto.AutoSubscribeCandidateDto;

/**
 * 基于申购优先级(积分)排序
 *
 */
public class AutoSubscribeScoreComparator implements
		Comparator<AutoSubscribeCandidateDto> {

	@Override
	public int compare(AutoSubscribeCandidateDto o1,
			AutoSubscribeCandidateDto o2) {
		return o1.getScore().compareTo(o2.getScore());
	}
}
