package com.hengxin.platform.job;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class DynamicJobInvocation {

	@Autowired
	Scheduler s;
	@Autowired
	@Qualifier("exampleJobBean")
	JobDetail job;

	@Scheduled(fixedDelay = 2000)
	public void test() {
		try {
			if (!s.checkExists(job.getKey())) {
				s.addJob(job, false);
			}
			s.triggerJob(job.getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
