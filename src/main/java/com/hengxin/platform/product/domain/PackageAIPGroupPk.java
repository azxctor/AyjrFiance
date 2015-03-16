
/*
* Project Name: kmfex-platform
* File Name: PackageAIPGroupDoPK.java
* Class Name: PackageAIPGroupDoPK
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
	
package com.hengxin.platform.product.domain;

import java.io.Serializable;

/**
 * Class Name: PackageAIPGroupPk Description: TODO
 * 
 * @author huanbinzhang
 * 
 */

@SuppressWarnings("serial")
public class PackageAIPGroupPk implements Serializable {
    private String packageId;
    private Integer group;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

}
