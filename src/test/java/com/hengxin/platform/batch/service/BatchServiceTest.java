package com.hengxin.platform.batch.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.hengxin.platform.batch.entity.JobWork;
import com.hengxin.platform.batch.enums.ETaskGroupId;
import com.hengxin.platform.fund.util.DateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, 
	locations = {"classpath:/test/spring/hengxin-platform-service-test.xml"})
@TransactionConfiguration(defaultRollback = false)
public class BatchServiceTest {
	
	@Autowired
	private JobWorkService jobWorkService;
	@Autowired
	private BatchBizTaskService batchBizTaskService;
	
	@Test
	public void getJobWorkList() {
		String taskGroupId = ETaskGroupId.REPAYMENT.getCode();
		Date workDate = DateUtils.getDate("2014-12-05", "yyyy-MM-dd");
		List<JobWork> list = jobWorkService.getJobWorkList(taskGroupId, workDate);
		System.out.println(list.size());
	}
	
	@Test
	public void createJobWorks(){
		String taskGroupId = ETaskGroupId.REPAYMENT.getCode();
		batchBizTaskService.createJobWork(taskGroupId, "sys");
	}
	
	
}
