
/*
* Project Name: kmfex-platform-trunk
* File Name: SeperateLoggingJobListener.java
* Class Name: SeperateLoggingJobListener
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
	
package com.hengxin.platform.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.MDC;

import com.hengxin.platform.common.constant.ApplicationConstant;

/**
 * Class Name: SeperateLoggingJobListener
 * Description: TODO
 * @author zhengqingye
 *
 */
public class SeperateLoggingJobListener extends JobListenerSupport {

	private long start;
	
    /* (non-Javadoc)
     * @see org.quartz.JobListener#getName()
     */
    @Override
    public String getName() {
        return SeperateLoggingJobListener.class.getName();
    }
    
    public void jobToBeExecuted(JobExecutionContext context) {
        MDC.put(ApplicationConstant.LOG_JOB, context.getJobDetail().getKey().getName());
        start = System.currentTimeMillis();
        getLog().info("job exexution started");
    }

    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
    	getLog().info("job exexution completed in {}ms", System.currentTimeMillis() - start);
        MDC.remove(ApplicationConstant.LOG_JOB);
    }

}
