
/*
* Project Name: kmfex-platform-trunk
* File Name: EBizError.java
* Class Name: EBizError
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
	
package com.hengxin.platform.common.enums;

import java.util.Arrays;

import com.hengxin.platform.common.exception.DisplayableError;


/**
 * Class Name: EBizError
 * Description: business errors which may be recoverable, or should be displayed on web page.
 * @author zhengqingye
 *
 */
public enum EErrorCode implements DisplayableError{
    //Default error
    COMM_INTERNAL_ERROR("COMM0001"),
    
    //Errors for internal technical issues.
    TECH_PARAM_REQUIRED("TECH0001"),
    TECH_DATA_NOT_EXIST("TECH0002"),
    TECH_DATA_INVALID("TECH0003"),
    TECH_OPTIMISTIC_LOCK("TECH0004"),
    TECH_SESSION_TIMEOUT("TECH0005", "error.common.session_timeout"),
    
    //Errors for member
    USER_NOT_EXIST("MEMBER0001","member.user.not.exist"),
    SMS_SEND_ERROR("MEMBER0002","member.sms.send.error"),
    USER_FROZEN("MEMBER0003","member.frozen"),
    
    //Errors for product services
    PROD_PKG_OP_NOT_IN_CLOSED_TIME("PROD0001", "product.error.package.not_in_closed_time"),
    PROD_PKG_STATUS_IS_NOT_SUBSCRIBE("PROD0002", "product.error.package.status_not_in_subscribe"),
    PROD_PKG_TERMINATE_WHEN_SUPPLY_IS_FULL("PROD0003", "product.error.package.terminate_when_full"),
    PROD_PKG_DONT_HAVE_SUBSCRIB_RECORD("PROD0004", "product.error.package.no_subscribe_record"),
    PROD_PKG_STATUS_IS_NOT_WAIT_SIGN("PROD0005", "product.error.package.status_not_in_wait_sign"),
    NO_ENOUGH_FINANCIER_BALANCE("PROD0006","product.error.financier_balance_not_enough"),// 保证金冻结失败，融资会员账户余额不足！
    NO_ENOUGH_GUARANTOR_BALANCE("PROD0007","product.error.guarantor_balance_not_enough"),// 保证金冻结失败，担保机构账户余额不足！
    PROD_PKG_STATUS_IS_NOT_WAIT_LOAD_APPROAL("PROD0008", "product.error.package.status_not_in_wait_loan_approal"),
    PROD_PKG_STATUS_IS_NOT_PAYING("PROD0009", "product.error.package.status_not_in_paying"),
    PROD_PKG_HAS_BREACH_TERM("PROD00010", "product.error.package.has.breach.term"),
    PROD_PKG_HAS_BEEN_DISPOSED("PROD00011", "product.error.package.has.been.disposed"),
    PROD_PKG_IS_LAST_TERM("PROD00012", "product.error.package.is.last.term"),
    PROD_PKG_OP_IN_DAY_END_TIME("PROD0013", "product.error.package.op.in.day.end.time"),
    PROD_PKG_STATUS_IS_ERROR("PROD0014", "product.error.package.status_not_error"),
    PROD_SIGNIN_OPEN_RECORD("PROD0015", "product.error.signin.open_record"),
    PROD_TERMTYPE_NOT_MATCH_REPAYMETHOD("PROD0016", "product.error.termtype.not.match.repaymethod"),
    PROD_WARRANTYTYPE_ERROR("PROD0017", "product.error.warrantytype.error"),
    PROD_MANUAL_REPAY_IN_SETTLE_TIME("PROD0018", "product.manual.repay.in.settle.time"),
    PROD_PKG_STATUS_IS_NOT_WAIT_LOAD_APPROAL_CONFIRM("PROD0019", "product.error.package.status_not_in_wait_loan_approal_confirm"),
    PROD_WRTR_OVERDUE_CMPNS_FINE_BAL_NOT_ENOUGH("PROD0020", "product.wrtr.overdue.cmpns.fine.bal.not.enough"),

    //Errors for bnkdocking
    XML_UNSOPPORTED_ENCODING("BNKD0001","bnkd.xml.unsopported.encoding"),
    XML_UNMARSHAL_ERROR("BNKD0002","bnkd.xml.unmarshal.error"),
    SOCKET_IO_ERROR("BNKD0003","bnkd.socket.io.error"),
    XML_MARSHAL_ERROR("BNKD0004","bnkd.xml.marshal.error"),
    MAC_ERROR("BNKD0005","bnkd.mac.error"),
    ID_NOT_MATCH("BNKD0006","bnkd.id.not.natch"),
    QUERY_EXCHANGE_ACCT_AMT_ERROR("BNKD0007","bnkd.query.exchange.acct.amt.error"),
    REVERSE_TRANSFER("BNKD0008","bnkd.reverse.transfer"),
    DAILY_SIGN_ERROR("BNKD0009","bnkd.daily.sign.error"),
    NAME_NOT_MATCH("BNKD0010","bnkd.name.not.natch"),
    
    //Errors for fund
    ACCT_NOT_ENOUGH_AVL_BAL("FUND0000","fund.acct.avl.bal.not.enough"),
    ACCT_NOT_EXIST("FUND0001","fund.acct.not.exist"),
    SUB_ACCT_NOT_EXIST("FUND0002","fund.sub.acct.not.exist"),
    AMT_PARAM_INVALID("FUND0003","fund.amt.param.invalid"),
    FNR_DTL_HAS_CLOSED("FUND0004","fund.fnr.dtl.has.closed"),
    FNR_DTL_OPER_TYPE_INVALID("FUND0005","fund.fnr.dtl.opertype.invalid"),
    INNER_TRANSFER_INVALID("FUND0006","fund.inner.subacct.transfer.invalid"),
    ACCT_STATUS_ILLEGAL("FUND0007","fund.acct.status.illegal"),
    AMT_RECV_PAY_NOT_EQUALS("FUND0008","fund.amt.recv.pay.not.equals"),
    PAYER_OR_PAYEE_CANNOT_EMPTY("FUND0009","fund.payer.or.payee.cannot.empty"),
    JNL_NO_PARAM_DUPLICATE("FUND0010","fund.jnl.no.param.duplicate"),
    USER_ACCT_EXIST("FUND0011","fund.user.acct.exist"),
    USER_SUB_ACCT_EXIST("FUND0012","fund.user.subacct.exist"),
    POSITION_NOT_EXIST("FUND0013","fund.pos.not.exist"),
    POSITION_TRADE_NOT_BALANCE("FUND0014","fund.pos.trade.not.balance"),
    BANK_ACCT_NO_EXIST("FUND0015","fund.bank.acct.no.exist"),
    ACCOUNT_CHECKING_FAILED("FUND0016","fund.account.checking.failed"),
    FUND_ACCT_CAN_NOT_PAY("FUND0017","fund.acct.can.not.pay"),
    FUND_ACCT_REVERSE_FAILED("FUND0018","fund.acct.reverse.failed"),
    ACCT_NOT_ENOUGH_CASHABLE_BAL("FUND0019","fund.acct.cashable.bal.not.enough"),
    BANK_ACCT_CONFILICT_WITH_ACCT("FUND0020","fund.bank.acct.confilct.with.acct"),
    BANK_ACCT_ALREADY_SIGNED("FUND0021","fund.bank.acct.already.signed"),
    BANK_CAN_NOT_SIGNED("FUND0022","fund.bank.can.not.signed"),
    BANK_ACCT_ALREADY_UNSIGNED("FUND0023","fund.bank.acct.already.unsigned"),
    
    BANK_ACCT_ALREADY_USED_BY_OTHERS("FUND0024","fund.bank.acct.already.used.by.others"),
    EXISTS_UN_END_RECHARGE_APPL("FUND0025","fund.exists.un.end.recharge.appl"),
    EXISTS_UN_END_WDRW_APPL("FUND0026","fund.exists.un.end.withdraw.appl"),
    EXISTS_UN_END_TRSF_APPL("FUND0027","fund.exists.un.end.transfer.appl"),
    EXISTS_UN_END_RVS_APPL("FUND0028","fund.exists.un.end.reverse.appl"),
    
    BATCH_EXEC_FAILED("BATH0001","batch.task.execute.failed"),
    BATCH_EXEC_INVALID("BATH0002","batch.task.execute.invalid"),
    BATCH_EXEC_NOT_MARKET_OPEN("BATH0003","batch.task.execute.not.market.open"),
    BATCH_EXEC_PROCESSING_WAITING("BATH0004","batch.task.execute.processing.waiting"),
    
    //Errors for account
    ACCT_USERID_NOT_EXIST("ACCT0001","account.error.userid.not.exist"),
    ACCT_DATE_PARSE_FAILED("ACCT0002","account.error.date.parse.failed"),
    ACCT_AMT_NOT_GREATER_ZERO("ACCT0003","account.error.amt.not.greater.zero"),
    ACCT_AMT_NOT_LESS_BAL("ACCT0004","account.error.amt.not.less.bal"),
    ACCT_USERID_NOT_MATCH("ACCT0005","account.error.acct.userid.not.match"),
    ACCT_PWD_NOT_MATCH("ACCT0006","account.error.pwd.check.failed"),
    ACCT_RECHARGE_FAILED("ACCT0007","account.error.recharge.failed"),
    ACCT_COMMON_RECHARGE_FAILED("ACCT0007","account.error.common.recharge.failed"),
    ACCT_WITHDRAWAL_FAILED("ACCT0008","account.error.withdraw.failed"),
    ACCT_WITHDRAWARL_APPL_NOT_EXIST("ACCT0009","account.error.appl.not.exist"),
    ACCT_WITHDRAWARL_APPL_HAS_DEAL("ACCT0010","account.error.appl.has.deal"),
    ACCT_TYPE_NOT_MATCH("ACCT0011","account.error.type.not.match"),
    ACCT_BANKINFO_NOT_EXIST("ACCT0012","account.error.bankacct.not.exist"),
    ACCT_SINGEDFLAG_NOT_MATCH("ACCT0013","account.error.singedflg.not.match"),
    ACCT_BNKACCTNAME_NOT_MATCH("ACCT0014","account.error.bnkacctname.not.match"),
    ACCT_MARKET_NOT_OPEN("ACCT0015","account.error.market.not.open"),
    ACCT_RELBNKID_HAS_EXIST("ACCT0016","account.error.relbnkid.has.exist"),
    ACCT_EXCEL_RESOLVE_FAILED("ACCT0017","account.error.excel.resolve.failed"),
    ACCT_NOT_MATCH_UNSIGNED_USER_INFO("ACCT0018","account.not.match.unsigned.user.info"),
    ACCT_EXCEL_MSG_ERROR("ACCT0019","account.error.excel.msg.error"),
    ACCT_AVL_NOT_ENOUGH_IN_BATCH_TRANSFER("ACCT0020","account.avl.not.enough.in.batch.transfer"),
    ACCT_CAN_NOT_PAY_IN_BATCH_TRANSFER("ACCT0021","account.can.not.pay.in.batch.transfer"),
    ACCT_RECHARGE_VOUCHER_NO_EXISTS("ACCT0022","account.recharge.vocuherno.exists"),
    ACCT_TRANSFER_APPLICATION_STATUS_ERROR("ACCT0023","account.transfer.application.status.error"),
    ACCT_EXCEL_MSG_EMPTY("ACCT0024","account.error.excel.msg.empty"),
    ACCT_RECHARGE_MARKET_NOT_OPEN("ACCT0025","account.recharge.error.market.not.open"),
    ACCT_RECHARGE_APPLY_MARKET_NOT_OPEN("ACCT0026","account.recharge.apply.error.market.not.open"),
    
    
    // Errors for market
    BALANCE_LACK("MAKT0001", "market.error.balance.lack"),
    SUB_AMOUNT_EMPTY("MAKT0002","market.error.subscribe.amount.empty"),
    SUB_AMOUNT_INVAILD("MAKT0003","market.error.subscribe.amount.invaild"),
    SUB_AMOUNT_OUT_RANGE("MAKT0004","market.error.subscribe.amount.outrange"),
    SUB_PERMISSION_NOT_HAVE("MAKT0005","market.error.subscribe.permissions.nothave"),
    SUB_PACKAGE_NOT_FOUND("MAKT0006","market.error.subscribe.package.notfound"),
    SUB_PACKAGE_FULL("MAKT0007","market.error.subscribe.package.full"),
    SUB_NOT_START("MAKT0008","market.error.subsribe.notstart"),
    SUB_HAS_PASSED("MAKT0009","market.error.subscribe.haspassed"),
    SUB_AMOUNT_LACK("MAKT0010","market.error.subscribe.amount.lack"),
    MARKET_CLOSE("MAKT00011","market.error.market.close"),
    TRANSFER_NOT_FOUND("MAKT00012","market.error.transfer.notfound"),
    TRANSFER_YOUR_OWN("MAKT00013","market.error.transfer.youown"),
    
    // Errors for file upload
    FILE_FORMAT_NOT_MATCH("FILE0001", "fileupload.error.format.not.match"),
    FILE_UPLOAD_FAIL("FILE0002", "fileupload.error.upload.fail"),
    
    // Errors for action history
    REFLECT_EXCTPION("ACHY0001", ""),
    
    //Errors for chinapnr
    CMDID_NOT_EXIST("CHINAPNR001"),
    CHKVALUE_VERIFY_FAILED("CHINAPNR002"),
    
    //Errors for EBC
    EBC_CHECK_SIGN_FAILED("EBC001"),
    EBC_BIZ_ERROR("EBC002"),
    EBC_WITHDRAWAL_ERROR("EBC004", "ebc.withdrawal.error"),
    EBC_SIGNUP_ERROR("EBC007", "ebc.signup.error"),
    EBC_BINDING_ERROR("EBC008", "ebc.binding.error"),
    EBC_EDIT_ACCOUNT_ERROR("EBC009", "ebc.edit.account.error"),

    NULL("","");

    private String errorCode;
    
    //this field is only for display, do not set it if it is not needed.
    private String displayMsg;
    
    private Object[] args;
    
    private static final String DEFAULT_ERROR_MSG = "error.common.unknown";
   
    private EErrorCode(String errorCode, String displayMsg){
        this.errorCode = errorCode;
        this.displayMsg = displayMsg;
    }
    
    private EErrorCode(String errorCode, String displayMsg, String[] args){
        this.errorCode = errorCode;
        this.displayMsg = displayMsg;
        this.args = Arrays.copyOf(args, args.length);
    }
    
    private EErrorCode(String errorCode){
        this.errorCode = errorCode;
        this.displayMsg = DEFAULT_ERROR_MSG;
    }
    
    @Override
    public boolean isBizError(){
        return !displayMsg.equals(DEFAULT_ERROR_MSG);
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getDisplayMsg() {
        return displayMsg;
    }

    public void setDisplayMsg(String displayMsg) {
        this.displayMsg = displayMsg;
    }

    /**
     * @return return the value of the var args
     */
    @Override
    public Object[] getArgs() {
        return args;
    }

    /**
     * set dynamic args for the message template
     * @param args Set args value
     */
    public void setArgs(Object[] args) {
        this.args = Arrays.copyOf(args, args.length);
    }
    
}
