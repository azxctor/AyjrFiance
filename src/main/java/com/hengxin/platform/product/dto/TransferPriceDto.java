/*
 * Project Name: kmfex-platform
 * File Name: TransferPriceDto.java
 * Class Name: TransferPriceDto
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

package com.hengxin.platform.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.hengxin.platform.product.validator.CreditorTransferPriceCheck;

/**
 * Class Name: TransferPriceDto Description: TODO
 * 
 * @author yingchangwang
 * 
 */

@CreditorTransferPriceCheck(transPrice = "transPrice", lotId = "lotId")
public class TransferPriceDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "{package.error.transfer.price.lesszero}")
    @DecimalMin(value = "0", message = "{package.error.transfer.price.lesszero}")    
    private BigDecimal transPrice;

    private String lotId;

    public BigDecimal getTransPrice() {
        return transPrice;
    }

    public void setTransPrice(BigDecimal transPrice) {
        this.transPrice = transPrice;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }
}
