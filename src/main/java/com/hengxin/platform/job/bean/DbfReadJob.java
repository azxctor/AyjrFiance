
/*
* Project Name: kmfex_bnk
* File Name: DbfReadJob.java
* Class Name: DbfReadJob
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
	
package com.hengxin.platform.job.bean;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * Class Name: DbfReadJob
 * Description: TODO
 * @author congzhou
 *
 */
@DisallowConcurrentExecution
public class DbfReadJob extends QuartzJobBean{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DbfReadJob.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.debug("dummy DbfReadJob invoked");
        LOGGER.debug("dummy DbfReadJob completed");
    }

}
