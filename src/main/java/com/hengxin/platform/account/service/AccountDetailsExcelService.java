/*
 * Project Name: kmfex-platform
 * File Name: ContractExcelService.java
 * Class Name: ContractExcelService
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

package com.hengxin.platform.account.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hengxin.platform.account.dto.AccountDetailsDto;
import com.hengxin.platform.common.service.ExcelService;
import com.hengxin.platform.fund.consts.DictConsts;

/**
 * Class Name: ContractExcelService.
 * 
 * @author yingchangwang
 * 
 */
@Service
public class AccountDetailsExcelService extends ExcelService {

    /**
     * 导出我的账户明细.
     * 
     * @Override
     */
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) {
        @SuppressWarnings("unchecked")
        List<AccountDetailsDto> list = (List<AccountDetailsDto>) model.get("accountdetails");
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet(DictConsts.MY_ACCOUNT_DETAILS_SHEETNAME);
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);
        String flag = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (AccountDetailsDto accountDetails : list) {
            row = sheet.createRow(a);
            
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(sdf.format(accountDetails.getTrxDt()));
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("\t" + accountDetails.getUseType().getText());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            if ("P".equalsIgnoreCase(accountDetails.getPayRecvFlg())) {
            	flag = "-";
            } else {
            	flag = "";
            }
            cell.setCellValue("\t" + flag +accountDetails.getTrxAmt());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(accountDetails.getCreateTs());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("\t" + accountDetails.getRelBizId()==null?"":accountDetails.getRelBizId());
            cell.setCellStyle(style);
            
            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("\t" + accountDetails.getTrxMemo()==null?"":accountDetails.getTrxMemo());
            cell.setCellStyle(style);
            a++;
        }

    }

}
