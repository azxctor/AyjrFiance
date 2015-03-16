/*
 * Project Name: kmfex-platform
 * File Name: EGender.java
 * Class Name: EGender
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

/**
 * Class Name: EGender Description: TODO
 *
 * @author rczhan
 *
 */

public enum EOptionCategory implements DBEnum {

    NULL(""),
    AGE("AGE"),
    BANK("BANK"),
    CONCENTRATION_RATIO("CONCENTRATION_RATIO"),
    CREDIT_HIST("CREDIT_HIST"),
    CUSTOMER_DIST("CUSTOMER_DIST"),
    DEBT_RATIO("DEBT_RATIO"),
    EDUCATION("EDUCATION"),
    FAMILY_STATUS("FAMILY_STATUS"),
    INCOME_DEBT_RATIO("INCOME_DEBT_RATIO"),
    ORG_AGE("ORG_AGE"),
    ORG_INDUSTRY("ORG_INDUSTRY"),
    ORG_NATURE("ORG_NATURE"),
    ORG_TYPE("ORG_TYPE"),
    PROFIT_RATIO("PROFIT_RATIO"),
    REGION("REGION"),
    RESIDENCE("RESIDENCE"),
    REVENUE_GROWTH("REVENUE_GROWTH"),
    SOCIAL_STATUS("SOCIAL_STATUS"),
    TOTAL_ASSETS("TOTAL_ASSETS"),
    MEMBER_LEVEL("MEMBER_LEVEL"), // FIXME DELETE
    /**
     * The investor level exist UM_INVSTR_LVL.
     */
    INVESTOR_LEVEL("INVESTOR_LEVEL"),
    FINANCIER_LEVEL("FINANCIER_LEVEL"),
    SERVICE_CENTER_LEVEL("SERVICE_CENTER_LEVEL"),
    PRODUCT_SERVICE_LEVEL("PRODUCT_SERVICE_LEVEL"),
    REAL_ESTATE_TYPE("REAL_ESTATE_TYPE"),
    ASSET_TYPE("ASSET_TYPE"),
    DEBT_TYPE("DEBT_TYPE"),
    JOB("JOB");

    private String code;

    EOptionCategory(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

}
