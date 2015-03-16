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

import com.hengxin.platform.account.dto.TransactionJournalDto;
import com.hengxin.platform.common.service.ExcelService;
@Service
public class TranjourExcel extends ExcelService {

 @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<TransactionJournalDto> detailList = (List<TransactionJournalDto>) model.get("detailList");
        
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet("会员资金流水明细");
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("##,##0.00");
        for (TransactionJournalDto detail : detailList) {

            row = sheet.createRow(a);
            
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getAcctNo());
            cell.setCellStyle(style);
            

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getName());
            cell.setCellStyle(style);
 
            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getSignedFlg().getText());
            cell.setCellStyle(style); 
            
            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getFullBackName());
            cell.setCellStyle(style); 
            
            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getUseType().getText());
            cell.setCellStyle(style); 
            
            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getTrxDt());
            cell.setCellStyle(style); 
            
            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            String amtstr = format.format(detail.getReceiveAmt());
            cell.setCellValue(amtstr);
            cell.setCellStyle(style); 
            
            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            amtstr = format.format(detail.getPayAmt());
            cell.setCellValue(amtstr);
            cell.setCellStyle(style); 
            
            cell = row.createCell(8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            amtstr = format.format(detail.getBal());
            cell.setCellValue(amtstr);
            cell.setCellStyle(style); 
            
            cell = row.createCell(9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getAgent());
            cell.setCellStyle(style); 
            
            cell = row.createCell(10);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getAgentName());
            cell.setCellStyle(style); 
            
            cell = row.createCell(11);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getTrxMemo());
            cell.setCellStyle(style);  
     
            a++; 
        }
 
    }

}
