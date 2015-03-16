package com.hengxin.platform.account.service;
//
///*
//* Project Name: kmfex
//* File Name: SignService.java
//* Class Name: SignService
//*
//* Copyright 2014 Hengtian Software Inc
//*
//* Licensed under the Hengtiansoft
//*
//* http://www.hengtiansoft.com
//*
//* Unless required by applicable law or agreed to in writing, software
//* distributed under the License is distributed on an "AS IS" BASIS,
//* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//* implied.
//* See the License for the specific language governing permissions and
//* limitations under the License.
//*/
//	
//package com.hengxin.platform.account.service;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.kmfex.platform.bnkdocking.enums.EBnkErrorCode;
//import com.kmfex.platform.common.dto.ResultDto;
//import com.kmfex.platform.common.dto.ResultDtoFactory;
//import com.kmfex.platform.common.util.CommonBusinessUtil;
//import com.kmfex.platform.fund.entity.AcctPo;
//import com.kmfex.platform.fund.repository.AcctRepository;
//import com.kmfex.platform.fund.repository.BankAcctRepository;
//import com.kmfex.platform.fund.service.BankAcctAgreementManageService;
//
//
///**
// * Class Name: SignService
// * Description: TODO
// * @author congzhou
// *
// */
//@Controller
//public class SignService {
//    
//    private final static Logger LOGGER = LoggerFactory
//            .getLogger(SignService.class);
//    
//    @Autowired
//    BankAcctAgreementManageService bankAcctAgreementManageService;
//    @Autowired
//    AcctRepository acctRepository;
//    @Autowired
//    BankAcctRepository bankAcctRepository;
//    
//    private static String[] userList;
//    static {
//        userList = new String[]{"880000000674"};
//    }
//    
//    @RequestMapping(value = "sign/{id}")
//    public ResultDto sign(@PathVariable("id") String userId, Model model) {
//        LOGGER.info("开始签约");
//        AcctPo acctPo = acctRepository.findByUserId(userId);
//        String acctNo = acctPo.getAcctNo();
//        String bankAcctNo = bankAcctRepository.findBankAcctByAcctNo(acctNo).get(0).getBnkAcctNo();
//        EBnkErrorCode result = bankAcctAgreementManageService.signBankAcct(acctNo, bankAcctNo, CommonBusinessUtil.getBankInterfaceAccountNo(), CommonBusinessUtil.getCurrentWorkDate());
//        if(StringUtils.equals(result.getMsg_id(), "0000")) {
//            LOGGER.debug("用户"+userId+"签约成功");
//        }
//        return ResultDtoFactory.toAck("签约成功", null);
//    }
//    
//    @RequestMapping(value = "unsign/{id}")
//    public ResultDto ubSign(@PathVariable("id") String userId, Model model) {
//        LOGGER.info("开始解约");
//        AcctPo acctPo = acctRepository.findByUserId(userId);
//        String acctNo = acctPo.getAcctNo();
//        String bankAcctNo = bankAcctRepository.findBankAcctByAcctNo(acctNo).get(0).getBnkAcctNo();
//        EBnkErrorCode result = bankAcctAgreementManageService.unSignBankAcct(acctNo, bankAcctNo, CommonBusinessUtil.getBankInterfaceAccountNo(), CommonBusinessUtil.getCurrentWorkDate());
//        if(StringUtils.equals(result.getMsg_id(), "0000")) {
//            LOGGER.debug("用户"+userId+"解约成功");
//        }
//        return ResultDtoFactory.toAck("解约成功", null);
//    }
//    
////    @Scheduled(cron="0/300 * *  * * ? ") 
//    public void sign() {
//        for(String userId:userList) {
//            AcctPo acctPo = acctRepository.findByUserId(userId);
//            String acctNo = acctPo.getAcctNo();
//            String bankAcctNo = bankAcctRepository.findBankAcctByAcctNo(acctNo).get(0).getBnkAcctNo();
//            EBnkErrorCode result = bankAcctAgreementManageService.signBankAcct(acctNo, bankAcctNo, CommonBusinessUtil.getBankInterfaceAccountNo(), CommonBusinessUtil.getCurrentWorkDate());
//            if(StringUtils.equals(result.getMsg_id(), "0000")) {
//                LOGGER.debug("用户"+userId+"签约成功");
//            }
//        }
//    }
//    
////    @Scheduled(cron="0/300 * *  * * ? ") 
//    public void unSign() {
//        for(String userId:userList) {
//            AcctPo acctPo = acctRepository.findByUserId(userId);
//            String acctNo = acctPo.getAcctNo();
//            String bankAcctNo = bankAcctRepository.findBankAcctByAcctNo(acctNo).get(0).getBnkAcctNo();
//            EBnkErrorCode result = bankAcctAgreementManageService.unSignBankAcct(acctNo, bankAcctNo, CommonBusinessUtil.getBankInterfaceAccountNo(), CommonBusinessUtil.getCurrentWorkDate());
//            if(StringUtils.equals(result.getMsg_id(), "0000")) {
//                LOGGER.debug("用户"+userId+"解约成功");
//            }
//        }
//    }
//
//}
