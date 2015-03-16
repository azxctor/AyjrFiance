/*
 * Project Name: kmfex-platform-trunk
 * File Name: EBizRole.java
 * Class Name: EBizRole
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

package com.hengxin.platform.security.enums;

import com.hengxin.platform.common.enums.DBEnum;

/**
 * Class Name: EBizRole
 * 
 * Description: 该类只包含代码中需要硬编码的业务角色 
 * 
 * @author zhengqingye
 * 
 */

public enum EBizRole implements DBEnum {

    ADMIN("admin","系统管理员"), 
    FINANCIER("financier","融资会员"), 
    INVESTER("invester","投资会员"), 
    AUTHZ_CNTL("authenticated_service_center","授权服务中心管理员"), 
    PROD_SERV("prod_serv","担保机构"), 
    TOURIST("tourist","游客"), 
    TOURIST_AGENCY("tourist_agency","机构游客"), 
    //-------------------trade
    TRANS_MANAGER("trade_manager","交易经理"),
    RANS_TRADE_INNOVATIVE_BIZ("trade_innovative_biz","创新业务"),
    RANS_TRADE_DATA_SEARCH("trade_data_search","数据查询"),
    TRANS_TRADE_MGR1("trade_daily_mgr1","日常管理1"),
    TRANS_TRADE_MGR2("trade_daily_mgr2","日常管理2"),
    //-------------------
    AUTH_CNTL_AGENT("auth_cntl_agent","服务中心代理"),
    AUTH_CNTL_GENERAL("auth_cntl_general","服务中心普通管理"),
    //--------------------risk
    PLATFORM_RISK_CONTROL_MANAGER("risk_manager","风控经理"),
    PLATFORM_RISK_CONTROL_SUPERVISOR("risk_supervisor","风控主管"),
    PLATFORM_RISK_CONTROL_MAKER("risk_maker","风控经办"),
    PLATFORM_RISK_CONTROL_LOAD_APPROVE("risk_load_approve","风控放款审批"),
    PLATFORM_RISK_CONTROL_POST_LENDING("risk_post_lending","风控贷后管理"),
    PLATFORM_RISK_CONTROL_PRODUCT_APPROVE("risk_product_approve","风控项目审核"),
    //--------------------client_service
    PLATFORM_CLIENT_SERVICE_MANAGER("cs_manager","客服经理"),
    PLATFORM_CLIENT_SERVICE_front_supervisor("cs_front_supervisor","前端响应主管"),
    PLATFORM_MEMBER_ASSOCIATE1("cs_associate1","客服专员1"),
    PLATFORM_MEMBER_ASSOCIATE2("cs_associate2","客服专员2"),
    PLATFORM_MEMBER_QOS_ASSOCIATE("cs_qos_associate","服务质量管理专员"),
    PLATFORM_MEMBER_MEMBERINFO_ASSOCIATE("cs_memberinfo_associate","会员信息管理专员"),
    //--------------------
    PLATFORM_PRODUCTION_DEPT("prod_dept","产品部"),
    PLATFORM_CHANNEL_DEPT("channel_dept","渠道部"),
    //--------------------settle
    PLATFORM_SETTLEMENT_MANAGER("settle_manager","结算经理"),
    PLATFORM_SETTLEMENT_DOUBLE_CHECK_1("settle_doublecheck_1","复核岗1"),
    PLATFORM_SETTLEMENT_DOUBLE_CHECK_2("settle_doublecheck_2","复核岗2"),
    PLATFORM_SETTLEMENT_MAKER_1("settle_maker_1","经办岗1"),
    PLATFORM_SETTLEMENT_MAKER_2("settle_maker_2","经办岗2"),
    PLATFORM_SETTLEMENT_CMB_MAKER("settle_cmb_maker","招行经办岗"),
    PLATFORM_SETTLEMENT_ICBC_MAKER("settle_icbc_maker","工行充值经办"),
    PLATFORM_SETTLEMENT_SEARCH("settle_search","查询"),   
    //-----------------------
    PLATFORM_POSTLENDING_ADMIN("postlending_admin","贷后管理员");

    private String code;
    private String name;

    private EBizRole(String code,String name) {
        this.code = code;
        this.name= name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
