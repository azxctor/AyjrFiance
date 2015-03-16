/*
 * Project Name: kmfex-platform
 * File Name: BaseService.java
 * Class Name: BaseService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * Licensed under the Hengtiansoft
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.common.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.jbpm.process.audit.VariableInstanceLog;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskData;
import org.kie.api.task.model.TaskSummary;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hengtiansoft.sc.jbpm.service.JbpmService;
import com.hengxin.platform.common.constant.ApplicationConstant;

/**
 * Class Name: BaseService Description:
 * <p>
 * the base service
 * 
 * @author minhuang
 * 
 */
public class ExtendJbpmService extends ApplicationObjectSupport {

    private boolean available = false;

    /**
     * @return return the value of the var available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @param available
     *            Set available value
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @return return the value of the var jbpmService
     */
    public JbpmService getJbpmService() {
        if (isAvailable()) {
            return (JbpmService) (getApplicationContext().getBean("jbpmService"));
        }
        return new JbpmService();
    }

    /**
     * 
     * Description: TODO
     * 
     * @param processId
     * @param params
     */
    @Transactional
    public ProcessInstance createProcess(final String processId, final Map<String, Object> params) {
        return getJbpmService().startProcess(processId, params);
    }

    /**
     * 
     * Description: TODO
     * 
     * @param processId
     * @param params
     */
    @Transactional
    public void createProcessEscapeRule(final String processId, final Map<String, Object> params, final Object object) {
        createProcess(processId, params);
        getJbpmService().insertEntryPoint(object);
        getJbpmService().fireAllRules();
    }

    /**
     * 
     * Description: TODO
     * 
     * @param userId
     * @param taskId
     * @return
     */
    public WorkflowProcessInstance getProcessInstance(final String userId, final long taskId) {
        Task task = getJbpmService().getTask(taskId);
        TaskData taskData = task.getTaskData();
        WorkflowProcessInstance processInstance = (WorkflowProcessInstance) (getJbpmService()
                .getProcessInstance(taskData.getProcessInstanceId()));
        return processInstance;
    }

    /**
     * 
     * Description: TODO
     * 
     * @param userId
     * @param taskId
     * @return
     */
    public long getProcessInstanceIdByTaskId(final String userId, final long taskId) {
        Task task = getJbpmService().getTask(taskId);
        TaskData taskData = task.getTaskData();
        return taskData.getProcessInstanceId();
    }

    /**
     * 
     * Description: TODO
     * 
     * @param params
     * @param userId
     * @param taskId
     */
    @Transactional
    public void doTask(final Map<String, Object> params, final String userId, final long taskId) {
        if (StringUtils.isNotBlank(userId)) {
            getJbpmService().startTask(taskId, userId);
            getJbpmService().completeTask(taskId, userId, params);
        }
    }

    /**
     * 
     * Description: TODO
     * 
     * @param userId
     * @param status
     * @param id
     * @param language
     * @return
     */
    public TaskSummary getTaskByIdAndStatusAndProcessId(final String processId, final String userId,
            final List<Status> status, Object id, final String language, final Map<String, Boolean> approveMap) {
        if (!(id instanceof String)) {
            id = id.toString();
        }
        if (approveMap != null && approveMap.size() > 1) {
            return null;
        }
        List<TaskSummary> tasSummaryList = getJbpmService().getTaskService().getTasksAssignedAsPotentialOwnerByStatus(
                userId, status, getLanguage(language));
        if (tasSummaryList == null || tasSummaryList.size() == 0) {
            return null;
        }
        for (TaskSummary taskSummary : tasSummaryList) {
            final String matchedProcessId = taskSummary.getProcessId();
            final long processInstanceId = taskSummary.getProcessInstanceId();
            Map<String, Object> varMap = getJbpmService()
                    .getProcessInstanceVariables(String.valueOf(processInstanceId));
            if (matchedProcessId.equals(processId)
                    && varMap.containsValue(id + ApplicationConstant.JBPM_PROCESS_PRODUCT_PACKAGENAME)) {
                setProcessInstanceVariables(taskSummary.getProcessInstanceId(), approveMap);
                return taskSummary;
            }
        }
        return null;
    }

    /**
     * 
     * Description: TODO
     * 
     * @param processInstanceId
     */
    private void setProcessInstanceVariables(final long processInstanceId, final Map<String, Boolean> approveMap) {
        ProcessInstance processInstance = getJbpmService().getKsession().getProcessInstance(processInstanceId);
        if (processInstance != null && approveMap != null) {
            for (Map.Entry<String, Boolean> entry : approveMap.entrySet()) {
                ((WorkflowProcessInstanceImpl) processInstance).setVariable(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 
     * Description: TODO
     * 
     * @param processInstanceId
     * @param userId
     * @param status
     * @param id
     * @param language
     * @return
     */
    public TaskSummary getTaskByIdAndStatusAndProcessInstanceId(final long processInstanceId, final String userId,
            final List<Status> status, Object id, final String language) {
        if (!(id instanceof String)) {
            id = id.toString();
        }
        List<TaskSummary> taskList = getJbpmService().getTaskService().getTasksByStatusByProcessInstanceId(
                processInstanceId, status, getLanguage(language));
        if (taskList != null && taskList.size() > 0) {
            List<TaskSummary> tasSummaryList = getJbpmService().getTaskService()
                    .getTasksAssignedAsPotentialOwnerByStatus(userId, status, getLanguage(language));
            if (tasSummaryList == null || tasSummaryList.size() == 0) {
                return null;
            }
            for (TaskSummary task : taskList) {
                for (TaskSummary taskSummary : tasSummaryList) {
                    if (task.getId() == taskSummary.getId()) {
                        return taskSummary;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * Description: TODO
     * 
     * @param valueOf
     * @param string
     * @return
     */
    public Object getProcessInstanceVariableByVariableId(final String processInstanceId, final String variableId) {
        Map<String, Object> varMap = getJbpmService().getProcessInstanceVariables(processInstanceId);
        if (varMap != null && varMap.size() > 0) {
            return varMap.get(variableId);
        } else {
            return getProcessInstanceVariableByVariableIdAndLog(processInstanceId, variableId);
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Object getProcessInstanceVariableByVariableIdAndLog(final String processInstanceId, final String variableId) {
        List<VariableInstanceLog> varLogList = getJbpmService().getLogService().findVariableInstances(
                Long.valueOf(processInstanceId), variableId);
        if (varLogList != null && varLogList.size() > 0) {
            VariableInstanceLog varLog = Collections.max(varLogList, new Comparator<VariableInstanceLog>() {
                @Override
                public int compare(VariableInstanceLog log0, VariableInstanceLog log1) {
                    return log0.getDate().compareTo(log1.getDate());
                }
            });
            return varLog.getValue();
        }
        return null;
    }

    /**
     * 
     * Description: TODO
     * 
     * @param language
     * @return
     */
    private String getLanguage(final String language) {
        if (StringUtils.isNotEmpty(language)) {
            return language;
        } else {
            return "en-UK";
        }
    }
}
