/*
 * Project Name: kmfex-platform
 * File Name: FundTrxJnlExcelService.java
 * Class Name: FundTrxJnlExcelService
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

package com.hengxin.platform.fund.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.service.ExcelService;
import com.hengxin.platform.fund.consts.DictConsts;
import com.hengxin.platform.fund.dto.FundPoolDtlDto;
import com.hengxin.platform.fund.dto.FundTrxJnlDto;
import com.hengxin.platform.fund.enums.EFundPayRecvFlag;
import com.hengxin.platform.fund.util.DateUtils;

/**
 * Class Name: FundTrxJnlExcelService
 * 
 * @author tingwang
 * 
 */
@Service
public class FundTrxJnlExcelService extends ExcelService {

    /*
     * (non-Javadoc)
     * 
     * @see com.hengxin.platform.common.service.ExcelService#buildExcelDocument(java.util.Map,
     * org.apache.poi.hssf.usermodel.HSSFWorkbook)
     */

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<FundTrxJnlDto> fundTrxJnlList = (List<FundTrxJnlDto>) model.get("fundTrxJnlList");
        FundPoolDtlDto fundPoolDtlDto = (FundPoolDtlDto) model.get("fundPoolDtl");

        // 读取工作表
        HSSFSheet sheet = workbook.getSheet(DictConsts.POOLTRXJNL_SHEETNAME);
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("##,##0.00");
        for (FundTrxJnlDto fundTrxJnl : fundTrxJnlList) {

            row = sheet.createRow(a);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            String dateStr = DateUtils.formatDate(fundTrxJnl.getTrxDt(), "yyyy-MM-dd");
            cell.setCellValue(dateStr);
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(fundTrxJnl.getAcctNo());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(fundTrxJnl.getUserName());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            String cashPool = fundTrxJnl.getCashPool() == null ? StringUtils.EMPTY : fundTrxJnl.getCashPool().getText();
            cell.setCellValue(cashPool);
            cell.setCellStyle(style);

            if (fundTrxJnl.getPayRecvFlg() == EFundPayRecvFlag.RECV) {
                cell = row.createCell(4);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String amtstr = format.format(fundTrxJnl.getTrxAmt());
                cell.setCellValue(amtstr);
                cell.setCellStyle(style);

                cell = row.createCell(5);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue("--");
                cell.setCellStyle(style);
            } else {
                cell = row.createCell(4);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue("--");
                cell.setCellStyle(style);

                cell = row.createCell(5);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                String amtstr = format.format(BigDecimal.ZERO.subtract(fundTrxJnl.getTrxAmt()));
                cell.setCellValue(amtstr);
                cell.setCellStyle(style);
            }

            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(fundTrxJnl.getUseType().getText());
            cell.setCellStyle(style);

            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(fundTrxJnl.getTrxMemo());
            cell.setCellStyle(style);

            a++;
        }

        row = sheet.createRow(a);

        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(StringUtils.EMPTY);
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(StringUtils.EMPTY);
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(StringUtils.EMPTY);
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("合计");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        String amtstrpay = format.format(fundPoolDtlDto.getRecvAmt());
        cell.setCellValue(amtstrpay);
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        String amtstrrec = format.format(BigDecimal.ZERO.subtract(fundPoolDtlDto.getPayAmt()));
        cell.setCellValue(amtstrrec);
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("存量余额");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(format.format(fundPoolDtlDto.getTotalBal()));
        cell.setCellStyle(style);

    }

}
