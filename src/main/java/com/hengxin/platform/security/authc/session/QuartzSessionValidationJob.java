/*
* Project Name: kmfex-platform-trunk
* File Name: QuartzSessionValidationJob.java
* Class Name: QuartzSessionValidationJob
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
	
package com.hengxin.platform.security.authc.session;


import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hengxin.platform.common.util.ApplicationContextUtil;

/**
 * A quartz job that basically just calls the {@link org.apache.shiro.session.mgt.ValidatingSessionManager#validateSessions()}
 * method on a configured session manager.  The session manager will automatically be injected by the
 * superclass if it is in the job data map or the scheduler map.
 *
 * @since 0.1
 */
@DisallowConcurrentExecution
public class QuartzSessionValidationJob implements Job {

    /*--------------------------------------------
    |             C O N S T A N T S             |
    ============================================*/
    /**
     * Key used to store the session manager in the job data map for this job.
     */
    static final String SESSION_MANAGER_KEY = "sessionManager";

    /*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    private static final Logger log = LoggerFactory.getLogger(QuartzSessionValidationJob.class);

    /*--------------------------------------------
    |         C O N S T R U C T O R S           |
    ============================================*/

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    /*--------------------------------------------
    |               M E T H O D S               |
    ============================================*/

    /**
     * Called when the job is executed by quartz.  This method delegates to the
     * <tt>validateSessions()</tt> method on the associated session manager.
     *
     * @param context the Quartz job execution context for this execution.
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {

//        JobDataMap jobDataMap = context.getMergedJobDataMap();
//        ValidatingSessionManager sessionManager = (ValidatingSessionManager) jobDataMap.get(SESSION_MANAGER_KEY);
        ValidatingSessionManager sessionManager = ApplicationContextUtil.getBean(ValidatingSessionManager.class);

        if (log.isDebugEnabled()) {
            log.debug("Executing session validation Quartz job...");
        }

        sessionManager.validateSessions();

        if (log.isDebugEnabled()) {
            log.debug("Session validation Quartz job complete.");
        }
    }
}