
/*
 * Project Name: kmfex-platform
 * File Name: EErrorCode.java
 * Class Name: EErrorCode
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

package com.hengxin.platform.bnkdocking.enums;


/**
 * Class Name: EErrorCode
 * Description: TODO
 * @author congzhou
 *
 */

public enum EBnkErrorCode {
    SUCCESS("交易成功", "0000"),
    ERROR_1001("未开通银商转账服务", "1001"),
    ERROR_1002("银商转账服务协议未经银行激活", "1002"),
    ERROR_1003("未关闭银商转账服务", "1003"),
    ERROR_1004("已开通银商转账服务", "1004"),
    ERROR_1005("银商转账服务银行已激活", "1005"),
    ERROR_1006("未已关闭银商转账服务", "1006"),
    ERROR_1007("已冻结银商转账服务协议", "1007"),
    ERROR_1020("新银行账号已与其他交易所账号建立协议","1020"),
    ERROR_1021("银行账号已与其他交易所交易账号建立协议", "1021"),
    ERROR_1022("交易所交易账号已与其他银行账号建立协议", "1022"),
    ERROR_1023("银行账号已升位，请使用新银行账号", "1023"),
    ERROR_1025("不支持的一卡通Bin号", "1025"),
    ERROR_1026("不支持两地一卡通 ", "1026"),
    ERROR_1027("不支持8位一卡通", "1027"),
    ERROR_1028("不支持的账户类型 ", "1028"),
    ERROR_1029("一卡通卡号不合法", "1029"),
    ERROR_1030("银行对公账号不合法", "1030"),
    ERROR_1031("财富账号不合法", "1031"),
    ERROR_1035("证件不符", "1035"),
    ERROR_1036("户名不符", "1036"),
    ERROR_1037("不允许跨分行变更账号", "1037"),
    ERROR_1038("账户类型不符", "1038"),
    ERROR_1039("无此城市码", "1039"),
    ERROR_1040("无此分行号", "1040"),
    ERROR_1041("无此交易所编号", "1041"),
    ERROR_1042("交易所无此币种 ", "1042"),
    ERROR_1043("当日有转账交易，不能换卡或关闭协议", "1043"),
    ERROR_1044("当日账户余额不为零，不能换卡或关闭协议", "1044"),
    ERROR_1045("份额不为0，不能关闭协议", "1045"),
    ERROR_1050("需要金卡及以上银行卡才允许开通协议", "1050"),
    ERROR_1051("关闭协议时结息失败，请重做或联系95555", "1051"),
    ERROR_1052("此卡是旧卡，请使用新卡来做交易", "1052"),
    ERROR_1053("此卡凭证已挂失", "1053"),
    ERROR_1054("此卡密码已挂失", "1054"),
    ERROR_1200("机构户禁止交易所发起转账", "1200"),
    ERROR_1201("汇总户可用额度不足", "1201"),
    ERROR_1202("超过单笔银行转出限额", "1202"),
    ERROR_1203("超过单日银行转出累计限额", "1203"),
    ERROR_1204("已做交易日期切换", "1204"),
    ERROR_1205("交易账户余额不足", "1205"),
    ERROR_1211("交易所流水号不存在", "1211"),
    ERROR_1212("银行流水号不存在", "1212"),
    ERROR_1213("银行主机流水号不存在", "1213"),
    ERROR_1214("交易所流水号重复", "1214"),
    ERROR_1215("银行流水号重复", "1215"),
    ERROR_1216("银行主机流水号重复", "1216"),
    ERROR_1217("银行交易已记账", "1217"),
    ERROR_1218("银行交易已冲正", "1218"),
    ERROR_1219("银行无此交易", "1219"),
    ERROR_1220("交易正在进行中", "1220"),
    ERROR_1221("已发冲正，交易结果未知", "1221"),
    ERROR_1222("冲正失败，交易结果未知", "1222"),
    ERROR_1223("原交易失败", "1223"),
    ERROR_1231("请使用银行渠道办理此项转账业务", "1231"),
    ERROR_1232("请使用交易所渠道办理此项转账业务", "1232"),
    ERROR_1263("交易要素不符", "1263"),
    ERROR_1284("下一交易日期失配", "1284"),
    ERROR_1285("未到日切时间", "1285"),
    ERROR_1286("无效交易日期", "1286"),
    ERROR_1299("无超时重发交易", "1299"),
    ERROR_1301("无效客户号", "1301"),
    ERROR_1303("无此客户号", "1303"),
    ERROR_1305("对不起，户名不符", "1305"),
    ERROR_1306("无效密码", "1306"),
    ERROR_1307("客户密码错", "1307"),
    ERROR_1309("客户证件不符", "1309"),
    ERROR_1311("不活动客户", "1311"),
    ERROR_1313("无效证件类别", "1313"),
    ERROR_1315("客户凭证号错", "1315"),
    ERROR_1317("客户已冻结", "1317"),
    ERROR_1319("客户已关闭", "1319"),
    ERROR_1321("无效证件号", "1321"),
    ERROR_1322("传入证件已过期", "1322"),
    ERROR_1323("无效客户名", "1323"),
    ERROR_1325("该卡已经换卡", "1325"),
    ERROR_1327("密码错误次数超限", "1327"),
    ERROR_1329("无效账号", "1329"),
    ERROR_1331("账号已存在", "1331"),
    ERROR_1333("无此账号 ", "1333"),
    ERROR_1335("非活动账户 ", "1335"),
    ERROR_1337("账户已关闭", "1337"),
    ERROR_1339("账户已冻结", "1339"),
    ERROR_1341("账户余额不足 ", "1341"),
    ERROR_1343("无效密码", "1343"),
    ERROR_1345("该客户无相应货币活期户 ", "1345"),
    ERROR_1347("无效货币号", "1347"),
    ERROR_1349("无效地区码", "1349"),
    ERROR_1351("该交易需Ｂ级复核", "1351"),
    ERROR_1353("该交易需Ａ级授权", "1353"),
    ERROR_1355("不能本人复核", "1355"),
    ERROR_1357("需另一柜员交叉复核", "1357"),
    ERROR_1359("大额提现预警，请和发卡行联系", "1359"),
    ERROR_1361("此为睡眠户，请核实", "1361"),
    ERROR_1363("凭证号失配", "1363"),
    ERROR_1365("SVCD**EROR", "1365"),
    ERROR_1366("转账金额超限", "1366"),
    ERROR_1367("非授权用户", "1367"),
    ERROR_1369("用户权限受限", "1369"),
    ERROR_1371("用户密码错", "1371"),
    ERROR_1373("两次密码不一致 ", "1373"),
    ERROR_1375("用户已存在 ", "1375"),
    ERROR_1377("无效用户号 ", "1377"),
    ERROR_1379("用户不存在 ", "1379"),
    ERROR_1381("无效用户卡号 ", "1381"),
    ERROR_1383("用户卡号不存在 ", "1383"),
    ERROR_1385("用户卡号冲突 ", "1385"),
    ERROR_1389("该业务只能在账户的开户机构办理", "1389"),
    ERROR_1391("非现钞户", "1391"),
    ERROR_1711("无效交易金额 ", "1711"),
    ERROR_1712("无效交易币种 ", "1712"),
    ERROR_1713("交易所编号不能为空 ", "1713"),
    ERROR_1714("交易所交易账号不能为空 ", "1714"),
    ERROR_1716("银行账号不能为空 ", "1716"),
    ERROR_1717("账户类型不能为空 ", "1717"),
    ERROR_1718("用户号不能为空 ", "1718"),
    ERROR_1719("网点号不能为空 ", "1719"),
    ERROR_1720("分行号不能为空 ", "1720"),
    ERROR_1722("银行单笔转出限额大于单日银出累计限额 ", "1722"),
    ERROR_1730("交易所流水不能为空 ", "1730"),
    ERROR_1735("证件不能为空 ", "1735"),
    ERROR_1736("户名不能为空 ", "1736"),
    ERROR_1737("国别不能为空 ", "1737"),
    ERROR_1738("联系地址与邮编不能为空", "1738"),
    ERROR_1739("联系电话不能为空", "1739"),
    ERROR_1741("无此通信用户 ", "1741"),
    ERROR_1801("未开通此功能 ", "1801"),
    ERROR_1802("功能暂时受限 ", "1802"),
    ERROR_1803("不在交易时间 ", "1803"),
    ERROR_1804("正在清算或者非交易时间，功能受限 ", "1804"),
    ERROR_1931("未知的主机返回码 ", "1931"),
    ERROR_1932("主机通讯超时,请稍后查询交易结果", "1932"),
    ERROR_1933("主机通讯超时,交易失败", "1933"),
    ERROR_1934("银行不能进账,请联系客服中心-95555", "1934"),
    ERROR_1940("交易异常,请稍后查询交易结果", "1940"),
    ERROR_1951("连接交易所失败", "1951"),
    ERROR_1952("交易所响应超时，请稍后查询结果", "1952"),
    ERROR_1953("交易所数据格式错误 ", "1953"),
    ERROR_1954("交易所数据包MAC错误", "1954"),
    ERROR_1961("交易所公钥文件不存在 ", "1961"),
    ERROR_1970("银行系统故障 ", "1910"),
    ERROR_1971("银行数据库系统故障", "1971"),
    ERROR_1972("未知的交易所返回码", "1972"),
    ERROR_2001("交易所交易账号不能为空 ", "2001"), 
    ERROR_2003("交易所交易账号可取资金不足 ", "2003"), 
    ERROR_2005("交易所交易账号可用资金不足 ", "2005"), 
    ERROR_2007("交易所交易账号长度不正确 ", "2007"), 
    ERROR_2009("交易所交易账号不存在 ", "2009"), 
    ERROR_2011("交易所交易账号已存在 ", "2011"), 
    ERROR_2013("交易所交易账户已销户 ", "2013"), 
    ERROR_2015("交易所交易账户未销户 ", "2015"), 
    ERROR_2017("交易所交易账号状态异常 ", "2017"), 
    ERROR_2021("交易所交易账户开户失败 ", "2021"), 
    ERROR_2023("交易所交易账户被禁止销户 ", "2023"), 
    ERROR_2025("交易所交易账户被禁止银行支取 ", "2025"), 
    ERROR_2027("交易所交易账户被禁止转账支取 ", "2027"), 
    ERROR_2028("客户转账权限状态不正常 ", "2028"), 
    ERROR_2029("交易所交易账户数据不一致 ", "2029"), 
    ERROR_2031("交易所交易账户被挂失或被冻结 ", "2031"), 
    ERROR_2032("取消冻结失败 ", "2032"), 
    ERROR_2033("交易所交易账户密码错误次数超限 ", "2033"), 
    ERROR_2035("交易所交易账号已关联银行账号 ", "2035"), 
    ERROR_2036("交易所交易账号已关联它行账号 ", "2036"), 
    ERROR_2037("银商转账服务协议已存在 ", "2037"), 
    ERROR_2038("银商转账服务协议已关闭 ", "2038"), 
    ERROR_2039("交易所交易账号与银行账号不一致 ", "2039"), 
    ERROR_2040("银行代码不存在 ", "2040"), 
    ERROR_2041("银行账号不存在 ", "2041"), 
    ERROR_2042("银商转账服务协议不存在", "204"), 
    ERROR_2043("银行账号已销户 ", "2043"), 
    ERROR_2045("银行账号已存在 ", "2045"), 
    ERROR_2046("银行账号状态不正常 ", "2046"), 
    ERROR_2047("银行账号或银行代码为空 ", "2047"),
    ERROR_2049("转账金额超过单笔转出上限 ", "2049"), 
    ERROR_2051("转账金额超过单笔转出下限 ", "2051"), 
    ERROR_2053("转账金额超过单笔转入上限 ", "2053"), 
    ERROR_2055("转账金额超过单笔转入下限 ", "2055"), 
    ERROR_2057("累计转出金额超过上限 ", "2057"), 
    ERROR_2059("累计转入金额超过上限 ", "2059"), 
    ERROR_2060("累计转账金额超过上限", "2060"), 
    ERROR_2061("转出次数超过转出次数上限 ","2061"), 
    ERROR_2063("不在转账时间范围内 ", "2063"), 
    ERROR_2065("非交易时间 ", "2065"), 
    ERROR_2067("系统暂停资金操作 ", "2067"),
    ERROR_2068("交易所系统正在结算 ", "2068"), 
    ERROR_2069("无此交易所交易账号币种分账户 ", "2069"),
    ERROR_2071("当日有转账,不能变更银行账号", "2071"),
    ERROR_2072("当日有转账,不能开通银商转账协议", "2072"),
    ERROR_2073("当日有转账,不能取消银商转账协议", "2073"),
    ERROR_2075("当日有委托,不能销户 ", "2075"),
    ERROR_2077("请到柜台办理相关手续 ", "2077"),
    ERROR_2079("数据不合法 ", "2079"),
    ERROR_2081("无效交易金额 ", "2081"),
    ERROR_2083("输入日期错误 ", "2083"),
    ERROR_2085("交易所交易账户密码错误 ", "2085"),
    ERROR_2086("交易所交易账户状态异常", "2086"),
    ERROR_2087("该客户尚未开户 ","2087"),
    ERROR_2089("没有对应的转账记录 ", "2089"),
    ERROR_2091("非银行发起方交易 ", "2091"),
    ERROR_2093("证件不符 ", "2093"),
    ERROR_2095("户名不符 ", "2095"),
    ERROR_2097("交易所交易账户余额不为0", "2097"),
    ERROR_2098("客户持有份额，不能关闭转账协议", "2098"),
    ERROR_2101("交易所流水号不存在 ", "2101"),
    ERROR_2102("银行流水号不存在 ", "2102"),
    ERROR_2103("交易所流水号重复 ", "2103"),
    ERROR_2104("银行流水号重复 ", "2104"),
    ERROR_2106("交易所交易未记账 ", "2106"),
    ERROR_2107("交易所交易已记账 ", "2107"),
    ERROR_2108("交易所交易已冲正 ", "2108"),
    ERROR_2109("交易超时，状态未知", "2109"),
    ERROR_2111("冲正失败，交易不能执行", "2111"),
    ERROR_2113("凭证操作失败 ", "2113"),
    ERROR_2115("未开通此功能 ", "2115"),
    ERROR_2117("银行数据格式错误 ", "2117"),
    ERROR_2119("银行数据包MAC错误", "2119"),
    ERROR_2121("交易所私钥文件不存在 ", "2121"),
    ERROR_2123("交易所系统故障 ", "2123"),
    ERROR_2125("交易异常 ", "2125"),
    ERROR_2127("通讯错误 ", "2127"),
    ERROR_2128("交易所份额账户未激活", "2128"),
    ERROR_2129("客户充值错误", "2129"),
    ERROR_2131("读取对账文件出错","2131"),
    ERROR_2133("当日有未结束的申请,不能开通银商转账协议", "2133"),
    ERROR_2134("当日有未结束的申请,不能取消银商转账协议", "2134");
   

    private String msg;
    private String msg_id;
    private EBnkErrorCode(String msg,String msg_id)
    {
        this.msg=msg;
        this.msg_id=msg_id;
    }
    public String getMsg()
    {
        return this.msg;
    }
    public String getMsg_id() {
        return this.msg_id;
    }
}