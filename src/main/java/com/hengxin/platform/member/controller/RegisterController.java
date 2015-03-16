/*
 * Project Name: kmfex-platform
 * File Name: RegisterController.java
 * Class Name: RegisterController
 *
 * Copyright 2014 KMFEX Inc
 *
 *
 *
 * http://www.kmfex.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hengxin.platform.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.CommonMenuDto;
import com.hengxin.platform.common.dto.ResultDto;
import com.hengxin.platform.common.dto.ResultDtoFactory;
import com.hengxin.platform.common.dto.annotation.OnValid;
import com.hengxin.platform.common.dto.upstream.RegisterDto;
import com.hengxin.platform.common.dto.upstream.RegisterDto.RegisterGroup;
import com.hengxin.platform.common.util.AppConfigUtil;
import com.hengxin.platform.common.util.MenuUtil;
import com.hengxin.platform.common.util.WebUtil;
import com.hengxin.platform.member.service.RegisterService;
import com.hengxin.platform.security.SecurityContext;
import com.hengxin.platform.security.domain.User;
import com.hengxin.platform.security.enums.EBizRole;
import com.hengxin.platform.security.service.UserService;
import com.hengxin.platform.sms.consts.SmsConsts;

/**
 * Class Name: RegisterController
 *
 * @author runchen
 */

@Controller
@RequestMapping(value = "/members")
public class RegisterController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
    
    @Autowired
    private transient UserService userService;
    
    @Autowired
    private transient WebUtil webUtil;

    @Autowired
    private SecurityContext secContext;
    
    @Autowired
    private MenuUtil roleMenuUtil;
    
    @Autowired
    RegisterService registerService;
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(HttpServletRequest request, Model model) {
        model.addAttribute("isSmsCheckEnabled", AppConfigUtil.getConfig(SmsConsts.REG_SMS_VERIF_ENABLE_FLAG));
        return "members/register";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDto doMemberRegister(@OnValid(RegisterGroup.class) @RequestBody RegisterDto registerDto,HttpSession session,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.debug("doMemberRegister() start: ");
        registerDto.setUsername(registerDto.getUsername().trim());
        User user = ConverterService.convert(registerDto, User.class);
        userService.createUser(user, EBizRole.TOURIST);
        
        Session newSession = SecurityContext.login(registerDto.getUsername(), registerDto.getPassword());
        List<CommonMenuDto> sysMenu = roleMenuUtil.getSysMenu();
        if(sysMenu!=null){
        	newSession.setAttribute("menuList", sysMenu);
            newSession.setAttribute("menuMap", roleMenuUtil.getMenuMap(sysMenu));
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doMemberRegister() succeed: ");
        }
        return ResultDtoFactory.toRedirect(webUtil.getFullUrlBasedOn("/web/members/basicinfo"));
    }
    
    @RequestMapping(value = "smsVerify/{phone}", method = RequestMethod.POST)
    @ResponseBody
    public ResultDto smsVerify(@PathVariable("phone") String phone, Model model) {
        boolean flag = registerService.smsVerify(phone);
        if (flag) {
        	return ResultDtoFactory.toAck("发送验证码成功");	
		}
        return ResultDtoFactory.toAck("发送验证码失败, 请点击重新发送");
    }
    

}
