/*
 * Project Name: kmfex-platform
 * File Name: TransferExcelMsgDto.java
 * Class Name: TransferExcelMsgDto
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

package com.hengxin.platform.account.dto.upstream;

import java.io.Serializable;
import java.util.List;

import com.hengxin.platform.account.dto.BatchTransferExcelRowMsgDto;
import com.hengxin.platform.fund.enums.EFundUseType;

/**
 * Class Name: TransferExcelMsgDto Description: TODO
 * 
 * @author tingwang
 * 
 */

public class BatchTransferExcelMsgDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String path;

    private String fileName;

    private EFundUseType useType;

    private List<BatchTransferExcelRowMsgDto> transferMsgs;

    public List<BatchTransferExcelRowMsgDto> getTransferMsgs() {
        return transferMsgs;
    }

    public void setTransferMsgs(List<BatchTransferExcelRowMsgDto> transferMsgs) {
        this.transferMsgs = transferMsgs;
    }

    public EFundUseType getUseType() {
        return useType;
    }

    public void setUseType(EFundUseType useType) {
        this.useType = useType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
