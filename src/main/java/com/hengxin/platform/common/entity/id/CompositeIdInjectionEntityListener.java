package com.hengxin.platform.common.entity.id;

import java.util.Date;

import javax.persistence.PrePersist;

import com.hengxin.platform.member.domain.BaseApplication;
import com.hengxin.platform.product.domain.BaseInfo;

public class CompositeIdInjectionEntityListener {

	@PrePersist
	public void prePersist(Object entity) {
		if (entity instanceof BaseApplication) {
			BaseApplication e = (BaseApplication) entity;
			e.setCreateTs(new Date());
		} else if (entity instanceof BaseInfo) {
			BaseInfo e = (BaseInfo) entity;
			e.setCreateTs(new Date());
		}
	}

}