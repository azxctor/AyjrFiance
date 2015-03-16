/*
 * Project Name: kmfex-platform
 * File Name: CommonMenuController.java
 * Class Name: CommonMenuController
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

package com.hengxin.platform.common.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.dto.CommonMenuDto;
import com.hengxin.platform.common.enums.EMenuConstant;
import com.hengxin.platform.common.exception.BizServiceException;
import com.hengxin.platform.common.util.CommonBusinessUtil;
import com.hengxin.platform.common.util.MenuUtil;
import com.hengxin.platform.fund.domain.BankAcct;
import com.hengxin.platform.fund.enums.EBankType;
import com.hengxin.platform.fund.enums.EFlagType;
import com.hengxin.platform.fund.service.BankAcctService;
import com.hengxin.platform.member.domain.MemberApplication;
import com.hengxin.platform.member.service.MemberService;
import com.hengxin.platform.risk.service.InvestorQuestionnaireService;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: CommonMenuController.
 * 
 * @author yingchangwang
 * 
 */
@Controller
public class LandingPageController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LandingPageController.class);

    @Autowired
    private MenuUtil menuUtil;

    @Autowired
    private SecurityContext sec;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BankAcctService bankAcctService;

    @Autowired
    private transient InvestorQuestionnaireService investorQuestionnaireService;

    @RequestMapping(value = "menu")
    @ResponseBody
    @SuppressWarnings("unchecked")
	public List<CommonMenuDto> getCommonMenus(HttpSession session) {
		return (List<CommonMenuDto>) session.getAttribute("menuList");
	}

    @RequestMapping(value = "dashboard")
    public String signIn(HttpServletRequest request, Model model, @RequestParam(required = false) boolean login) {
        List<EMenuConstant> dashboardLinkList = menuUtil.getDashboardLink();
        if (dashboardLinkList == null || dashboardLinkList.isEmpty()) {
            LOGGER.error("######## error: " + sec.getCurrentUserName()
                    + " has no permissions. And the user is member user: " + (!sec.isPlatformUser()));
        }
        model.addAttribute("dashboardLinkList", dashboardLinkList);
        model.addAttribute("promptMsg", false);
        if (login && sec.isInvestor()) {
        	try {
	    		for (BankAcct bank : bankAcctService.findBankAcctByUserIdAndSignedFlg(
	    					sec.getCurrentUserId(), EFlagType.NO.getCode())) {
	    			if (EBankType.CMB.getCode().equals(bank.getBnkCd())) {
	    				// 招行非签约投资用户需要弹出提示
	    				model.addAttribute("promptMsg", true);
	    				break;
	    			}
	    		}
        	} catch (BizServiceException e) {
        		
        	}
    	}
        if (CommonBusinessUtil.isRiskActiveTemp() && sec.isInvestor()
        		&& !investorQuestionnaireService.hasQuestionnaire(sec.getCurrentUserId())) {
        	model.addAttribute("promptRiskQuestionnaire", true);
        }
        
        //get user age.
        String userId = sec.getCurrentUserId();
        if (sec.isInvestor()) {
        	MemberApplication member = memberService.getMemberWithLatestStatus(userId);
            String age = member.getPersonAge() != null ? member.getPersonAge() : member.getOrgLegalPersonAge();
            model.addAttribute("age", age != null ? age : "0");
		}
        
        if (login
        		&& (sec.isInvestor() || sec.isAuthServiceCenter()
						|| sec.isAuthServiceCenterAgent()
						|| sec.isAuthServiceCenterGeneral())) {
			model.addAttribute("promptAutoinvest", true);
		}
        Date bulletinEndDate = CommonBusinessUtil.getBulletinEndDate();
		if (login
				&& (sec.isInvestor() || sec.isAuthServiceCenter()
						|| sec.isAuthServiceCenterAgent() || sec
							.isAuthServiceCenterGeneral())
				&& Boolean.TRUE.equals(sec.getCurrentUser().getShowBulletin())
				&& bulletinEndDate != null
				&& new Date().before(bulletinEndDate)) {
			model.addAttribute("promptBulletin", true);
		}
		model.addAttribute("login", login);
        return "dashboard";
    }
}
