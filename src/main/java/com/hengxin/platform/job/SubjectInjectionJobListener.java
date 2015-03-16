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

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

import com.hengxin.platform.security.domain.User;

/**
 * Class Name: ShiroJobListener Description: to simulate system user for job
 * execution
 * 
 * @author yeliangjing
 * 
 */
public class SubjectInjectionJobListener extends JobListenerSupport {

	private static final String JOB_USER_ID = "System";

	@Override
	public String getName() {
		return SubjectInjectionJobListener.class.getName();
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		Subject subject = ThreadContext.getSubject();
		if (subject == null) {
			Session session = Mockito.mock(Session.class);
			Mockito.when(session.getAttribute(Mockito.anyString())).then(
					new Answer<Object>() {

						@Override
						public Object answer(InvocationOnMock invocation)
								throws Throwable {
							if ("shiro.user".equals(invocation.getArguments()[0])) {
								User user = new User();
								user.setUserId(JOB_USER_ID);
								return user;
							}
							return null;
						}

					});
			subject = Mockito.mock(Subject.class);
			Mockito.when(subject.getSession()).thenReturn(session);
			ThreadContext.bind(subject);
		}

	}

	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {

	}

}
