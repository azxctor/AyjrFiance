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

package com.hengxin.platform.product.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.service.ExcelService;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.product.dto.FinancingPackageContractDto;

/**
 * Class Name: ContractExcelService
 * 
 * @author yingchangwang
 * 
 */
@Service
public class ContractExcelService extends ExcelService {

    /**
     * 导出融资包合同列表
     * 
     * @Override
     */
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<FinancingPackageContractDto> list = (List<FinancingPackageContractDto>) model.get("contractList");
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet(DictConsts.CONTRACT_SHEETNAME);
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);
        for (FinancingPackageContractDto contract : list) {
            row = sheet.createRow(a);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(contract.getUserName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("\t" + contract.getUserIdNumber());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue("\t" + contract.getSignedAmt().doubleValue());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(contract.getSignDate());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("\t" + contract.getContranctNo());
            cell.setCellStyle(style);
            a++;
        }

    }

}
