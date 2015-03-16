package com.hengxin.platform.report.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.service.ExcelService;
import com.hengxin.platform.report.consts.ReportConsts;
import com.hengxin.platform.report.dto.DailyCompensatoryDetailDto;

@Service
public class DailyCompensatoryExcelService extends ExcelService {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
        @SuppressWarnings("unchecked")
        List<DailyCompensatoryDetailDto> list = (List<DailyCompensatoryDetailDto>) model.get("applList");
        // 读取工作表
        HSSFSheet sheet = workbook.getSheet(ReportConsts.SHEETNAME_DAILY_COMPENSATORY);
        HSSFRow row;
        HSSFCell cell = null;
        int a = 1;
        HSSFCellStyle style = this.getStyle(workbook);
        for (DailyCompensatoryDetailDto dailyOverDueDetailDto : list) {

            row = sheet.createRow(a);
            cell = row.createCell(0);
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(dailyOverDueDetailDto.getSequenceId());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getLastPayTs());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getSignTs());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getPaymentDate());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getWrtrName());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getPackageId());
            cell.setCellStyle(style);

            cell = row.createCell(6);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getPackageName());
            cell.setCellStyle(style);

            cell = row.createCell(7);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getCmpnsTs());
            cell.setCellStyle(style);
            
            cell = row.createCell(8);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getCmpnsPdAmt().toString());
            cell.setCellStyle(style);

            cell = row.createCell(9);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(dailyOverDueDetailDto.getItem());
            cell.setCellStyle(style);          

            a++;
        }

    }

}
