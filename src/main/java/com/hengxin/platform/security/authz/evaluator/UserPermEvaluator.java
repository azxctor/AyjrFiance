package com.hengxin.platform.security.authz.evaluator;

/*
* Project Name: micro-finance
* File Name: DefaultDataPermissionEvaluator.java
* Class Name: DefaultDataPermissionEvaluator
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
	
//package com.hengxin.platform.security.authz.evaluator;
//
//import java.io.Serializable;
//
//import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.hengxin.platform.member.entity.UserPo;
//import com.hengxin.platform.member.repository.UserRepository;
//import com.hengxin.platform.security.authz.DataPermissionType;
//
//
///**
// * Class Name: DefaultDataPermissionEvaluator
// * Description: TODO
// * @author zhengqingye
// *
// */
//@Component
//public class UserPermEvaluator extends AbstractDependentPermEvaluator {
//
//    @Autowired
//    private UserRepository userRepo;
//
//    /* (non-Javadoc)
//     * @see com.hengtiansoft.vmibatis.showcase.security.authz.aop.DataPermissionEvaluator#hasPermission(org.apache.shiro.subject.Subject, java.lang.Object, com.hengtiansoft.vmibatis.showcase.security.authz.aop.DataPermissionType)
//     */
//
//    @Override
//    public boolean hasPermission(Subject subject, Object targetObject, DataPermissionType permType) {
//        UserPo user = (UserPo) targetObject;
//        
//        return subject.getPrincipal().toString().equals(user.getUsername());
//    }
//
//    @Override
//    public Object getObject(Serializable targetId, String targetType) {
//        return userRepo.findOne(targetId.toString());
//    }

//}
