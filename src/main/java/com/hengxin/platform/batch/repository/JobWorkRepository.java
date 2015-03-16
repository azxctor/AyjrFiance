package com.hengxin.platform.batch.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hengxin.platform.batch.entity.JobWork;
import com.hengxin.platform.batch.enums.EJobWorkStatus;

public interface JobWorkRepository extends JpaRepository<JobWork, String>,
		JpaSpecificationExecutor<JobWork> {

//	@Query(value = "select count(1) from gl_job_wrk wk "
//			+ " where wk.grp_id =?1 "
//			+ "   and (wk.work_dt = ?2 or wk.work_dt = ?3)"
//			+ "   and wk.exec_status in ?4 ", nativeQuery = true)
//	public Integer getUnSucceedTskCount(String taskGroupId, Date currWorkDate,
//			Date preWorkDate, List<String> jobWorkStatus);
	
	
	public List<JobWork> findByTaskGroupIdInAndExecStatusInAndWorkDateIn(List<String> taskGroupIdList, List<EJobWorkStatus> jobStatus, List<Date> workDateList);

	
	public List<JobWork> findByTaskGroupIdAndWorkDateOrderBySeqNumAsc(String taskGroupId, Date workDate);
}
