/*
 * Project Name: kmfex-platform
 * File Name: MemberDetailController.java
 * Class Name: MemberDetailController
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hengxin.platform.common.constant.UrlConstant;
import com.hengxin.platform.common.controller.BaseController;
import com.hengxin.platform.common.converter.ConverterService;
import com.hengxin.platform.common.dto.DataTablesResponseDto;
import com.hengxin.platform.member.domain.Message;
import com.hengxin.platform.member.dto.MessageCriteria;
import com.hengxin.platform.member.dto.MessageDto;
import com.hengxin.platform.member.dto.MessageIdListDto;
import com.hengxin.platform.member.enums.EMessageType;
import com.hengxin.platform.member.service.MemberMessageService;
import com.hengxin.platform.product.ProductUtil;
import com.hengxin.platform.security.Permissions;
import com.hengxin.platform.security.SecurityContext;

/**
 * Class Name: BackLogController
 * 
 * @author Ryan
 */

@Controller
public class BackLogController extends BaseController {

    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BackLogController.class);

    @Autowired
    private MemberMessageService messageService;

    @Autowired
    private SecurityContext context;

    /**
     * 
     * Description: 渲染页面
     * 
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = UrlConstant.MY_MESSAGE_WAITING_TODO_URL, method = RequestMethod.GET)
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public String renderView(HttpServletResponse response, Model model,
            @RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "type", defaultValue = "") String type,
            @RequestParam(value = "currentPage", defaultValue = "") String currentPage) {
        if (StringUtils.isNotBlank(id)) {
            model.addAttribute("id", id);
        }
        if (StringUtils.isNotBlank(type)) {
            model.addAttribute("type", type);
        } else {
            model.addAttribute("type", "type");
        }
        if (StringUtils.isNotBlank(currentPage)) {
            model.addAttribute("currentPage", currentPage);
        }

        return "members/mymessages";
    }

    @RequestMapping(value = "getMessageView/{messageId}/{type}", method = RequestMethod.GET)
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public String renderView(HttpServletResponse response, @PathVariable String messageId, @PathVariable String type,
            Model model) {
    	int currentPage = 0;
    	if("NOTICE".equalsIgnoreCase(type)){
    		currentPage=1;
    	}else{
           messageService.updateMessage(messageId);
           currentPage = messageService.getMessagePageNumber(context.getCurrentUserId(), messageId);
    	}
        model.addAttribute("id", messageId);
        model.addAttribute("type", type);
        model.addAttribute("currentPage", currentPage);
        return "redirect:../../" + UrlConstant.MY_MESSAGE_WAITING_TODO_URL;
    }

    /**
     * 
     * Description: 根据messageId查询指定消息
     * 
     * @param messageId
     * @param model
     * @return
     */
    @RequestMapping(value = "message/{messageId}", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public MessageDto getMessageWithMessageId(@PathVariable String messageId, Model model) {
        Message m = messageService.updateMessage(messageId);
        MessageDto messageDto = new MessageDto();
        ConverterService.convert(m, messageDto);
        messageDto.setCreateTs(ProductUtil.formatDate(m.getCreateTs(),"yyyy-MM-dd HH:mm:ss"));
        return messageDto;
    }

    /**
     * 
     * Description: 查询当前用户的所有代办事项或者消息
     * 
     * @param response
     * @param searchCriteria
     * @return
     */
    @RequestMapping(value = "message", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public DataTablesResponseDto<MessageDto> getMessagesUnRead(@RequestBody MessageCriteria searchCriteria) {
        searchCriteria.setUserId(context.getCurrentUserId());
        List<String> lis = new ArrayList<String>();
        lis.add("createTs");
        searchCriteria.setDataProp(lis);
        List<String> sort = new ArrayList<String>();
        sort.add("desc");
        searchCriteria.setSortDirections(sort);
        searchCriteria.setReadable(false);
        DataTablesResponseDto<MessageDto> result = messageService.getMessages(searchCriteria);
        return result;
    }

    /**
     * 
     * Description: 查询未读消息
     * 
     * @return
     */
    @RequestMapping(value = "message/unread", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public DataTablesResponseDto<MessageDto> getMessages() {
        MessageCriteria searchCriteria = new MessageCriteria();
        searchCriteria.setUserId(context.getCurrentUserId());
        searchCriteria.setType(EMessageType.MESSAGE.getCode());
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        List<String> lis = new ArrayList<String>();
        lis.add("createTs");
        List<String> der = new ArrayList<String>();
        der.add("desc");
        searchCriteria.setSortedColumns(list);
        searchCriteria.setSortDirections(der);
        searchCriteria.setDataProp(lis);
        searchCriteria.setDisplayStart(1);
        searchCriteria.setDisplayLength(6);
        searchCriteria.setCategory("3");
        searchCriteria.setReadable(true);
        DataTablesResponseDto<MessageDto> result = messageService.getMessages(searchCriteria);

        return result;
    }

    /**
     * 
     * Description: 批量设置已读
     * 
     * @param messageIdListDto
     */
    @RequestMapping(value = "message/update/read", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public boolean updateMessageRead(@RequestBody MessageIdListDto messageIdListDto) {
        return messageService.updateMessageList(messageIdListDto.getMessageIdList());
    }

    /**
     * 
     * Description: 批量设置未读
     * 
     * @param messageIdListDto
     */
    @RequestMapping(value = "message/update/unread", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public boolean updateMessageUnRead(@RequestBody MessageIdListDto messageIdListDto) {
        return messageService.updateMessageListForUnRead(messageIdListDto.getMessageIdList());
    }
    
    /**
     * 
     * Description: 批量删除
     * 
     * @param messageIdListDto
     */
    @RequestMapping(value = "message/delete", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public boolean deleteMessage(@RequestBody MessageIdListDto messageIdListDto) {
        return messageService.deleteMessageList(messageIdListDto.getMessageIdList());
    }

    /**
     * 
     * Description: 消息推送
     * 
     * @return
     */
    @RequestMapping(value = "message/getmessagenum", method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions(Permissions.MESSAGE_WAITING_TODO)
    public int getMessageNum() {
        return messageService.getMessageByUserIdAndRead(context.getCurrentUserId());
    }

}
