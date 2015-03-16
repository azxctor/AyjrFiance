/*
 * Project Name: kmfex-platform
 * File Name: FeeGroup.java
 * Class Name: FeeGroup
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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Class Name: FeeGroup Description: TODO
 *
 * @author yingchangwang
 *
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "GL_FEE_GRP")
public class FeeGroup extends BaseInfo implements Serializable {
    @Id
    @Column(name = "FEE_GRP_ID")
    private String id;

    @Column(name = "FEE_GRP_NAME")
    private String groudName;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "GL_FEE_GRP_DTL", inverseJoinColumns = @JoinColumn(name = "FEE_ID"), joinColumns = @JoinColumn(name = "FEE_GRP_ID"))
    private List<Fee> feeList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroudName() {
        return groudName;
    }

    public void setGroudName(String groudName) {
        this.groudName = groudName;
    }

    public List<Fee> getFeeList() {
        return feeList;
    }

    public void setFeeList(List<Fee> feeList) {
        this.feeList = feeList;
    }
}
