package com.hengxin.platform.common.constant;

public interface ApplicationConstant {

    String X_FORM_ID = "x-form-id";

    String SAVE_SUCCESS_MESSAGE = "保存成功！";
    String SUBMIT_SUCCESS_MESSAGE = "提交成功！";
    String OPERATE_SUCCESS_MESSAGE = "操作成功！";
    String SUBSCRIBE_SUCCESS_MESSAGE = "自动申购参数设置成功!将在下一交易日生效!";
    String RELIEVE_SUCCESS_MESSAGE = "自动申购解除成功!将在下一交易日生效!";
    String OPERATE_SUCCESS_FLAG = "SUCCESS";
    String REVOKE_SUCCESS_FLAG = "撤单成功";

    String ADD_SUCCESS_MESSAGE = "添加成功！";
    String GET_SUCCESS_FLAG = "获取成功";
    String GET_FAIL_FLAG = "该用户名没有对应的账号";

    String ACTIVE_CODE = "A";
    String INACTIVE_CODE = "I";

    String ENABLE_CODE = "Y";
    String DISABLE_CODE = "N";
    String PENDING_CODE = "P";

    String REGION_CATEGORY = "REGION";
    String BANK_CATEGORY = "BANK";
    String REGION_ROOT = "0000";

    String MIN4_REGEXP = "(.{5,25})?$";
    String MIN6_REGEXP = "(.{6,})?$";
    String NUMBER_REGEXP = "[0-9]*";
    String ID_CARD_REGEXP = "(\\d{15}|\\d{18}|(\\d{17}(x|X)))?$";
    String BANK_CARD_REGEXP = "([0-9]{1,25})?$";
    String MOBILE_REGEXP = "^(\\d{11})?$";
    String PHONE_REGEXP = "^(\\d{1,4}(-)?\\d{6,10}(-\\d{1,4})?)?$";
    String POSTCODE_REGEXP = "(\\d{6})?$";
    String QQ_REGEXP = "(\\d{4,20})?$";
    String EMAIL_REGEXP = "(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)?$";
    //String THOUSAND_REGEX = "[0-9]+0{3}$";
    String ARRARY_REGEXP = "^([a-zA-Z0-9_]+)(\\[[0-9]+\\])$";
//    String USER_NAME_REGEXP = "^[a-zA-Z][a-zA-Z_0-9]*$";
//    String USER_NAME_REGEXP = "^[_0-9a-zA-Z\u4e00-\u9fa5][0-9a-zA-Z\u4e00-\u9fa5_][a-zA-Z\u4e00-\u9fa5][a-zA-Z\u4e00-\u9fa5_0-9]*$";
    String USER_NAME_REGEXP = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
    String PROD_LEVEL_REGEXP = "^100|[1-9]?[0-9]$";

    String VM_HASPREFIX = "hasPrefix";
    String VM_PREFIX_PERSON = "personPrefix";
    String VM_PREFIX_ORG = "orgPrefix";
    String VM_PREFIX_INVESTOR = "investorPrefix";
    String VM_PREFIX_FINANCIER = "financierPrefix";
    String MANUAL_VALIDATE = "manualValidate";
    
    //Log discriminator
    String LOG_USER_NAME = "userName";
    String LOG_SESSION_ID = "sessionId";
    String LOG_JOB = "jobId";

    // added by martin for jbpm process
    String JBPM_PROCESS_PRODUCT_PACKAGENAME = "com.hengxin.platform.product.finance.process";
}
