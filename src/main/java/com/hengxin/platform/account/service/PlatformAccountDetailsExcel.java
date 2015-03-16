package com.hengxin.platform.account.service;

import java.text.DecimalFormat;
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
import com.hengxin.platform.fund.util.DateUtils;
@Service
public class PlatformAccountDetailsExcel extends ExcelService {

 @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<AccountDetailsDto> detailList = (List<AccountDetailsDto>) model.get("detailList");
        
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet("平台账号明细");
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("##,##0.00");
        for (AccountDetailsDto detail : detailList) {

            row = sheet.createRow(a);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            String dateStr = DateUtils.formatDate(detail.getTrxDt(), "yyyy-MM-dd");
            cell.setCellValue(dateStr);
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getUseType().getText());
            cell.setCellStyle(style);
 
            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            String amtstr = format.format(detail.getTrxAmt());
            cell.setCellValue(amtstr);
            cell.setCellStyle(style);
            
            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getTrxMemo());
            cell.setCellStyle(style);
 

            a++;
        }
 
    }

}
