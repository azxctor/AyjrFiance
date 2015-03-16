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

import com.hengxin.platform.account.dto.upstream.WithdrawalApplDetailDto;
import com.hengxin.platform.common.service.ExcelService;
@Service
public class WithdrawDepositApplsApproveExcel extends ExcelService {

 @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<WithdrawalApplDetailDto> detailList = (List<WithdrawalApplDetailDto>) model.get("detailList");
        
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet("非签约会员提现");
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);

        DecimalFormat format = new DecimalFormat();
        format.applyPattern("##,##0.00");
        for (WithdrawalApplDetailDto detail : detailList) {

            row = sheet.createRow(a);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getApplDt());
            cell.setCellStyle(style);
            

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
            cell.setCellValue(detail.getAcctNo());
            cell.setCellStyle(style);
 
            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            String amtstr = format.format(detail.getTrxAmt());
            cell.setCellValue(amtstr);
            cell.setCellStyle(style);
            
            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getBnkAcctNo());
            cell.setCellStyle(style); 
            
            
            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getBnkAcctName());
            cell.setCellStyle(style); 
            
            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getBnkName());
            cell.setCellStyle(style); 
            
            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getCashPool().getText());
            cell.setCellStyle(style); 
            
            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(detail.getTrxMemo());
            cell.setCellStyle(style);  
            
            
            cell = row.createCell(8); 
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            if(null!=detail.getApplStatus())
   		    {
            	cell.setCellValue(detail.getApplStatus().getText());
   		    }else{
   		    	cell.setCellValue(detail.getDealStatus().getText());
   		    } 
            cell.setCellStyle(style);  
            
     
            a++; 
        }
 
    }

}
