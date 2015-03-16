package com.hengxin.platform.member.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.hengxin.platform.common.domain.DynamicOption;
import com.hengxin.platform.common.service.ExcelService;
import com.hengxin.platform.member.dto.MemberInfoDto;
import com.hengxin.platform.member.enums.EGender;
import com.hengxin.platform.member.enums.EUserType;
import com.hengxin.platform.report.consts.ReportConsts;

/**
 * 会员信息导出Excel.
 * 
 * @author chenwulou
 *
 */
@Service
public class MemberInfoExcelService extends ExcelService {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) throws Exception {
		@SuppressWarnings("unchecked")
		List<MemberInfoDto> list = (List<MemberInfoDto>) model.get("applList");
		/** 读取工作表 **/ 
		HSSFSheet sheetPerson = workbook.getSheet(ReportConsts.SHEETNAME_MEMBERINFO_PERSON);
		HSSFSheet sheetOrg = workbook.getSheet(ReportConsts.SHEETNAME_MEMBERINFO_ORG);
		HSSFRow rowPerson;
		HSSFRow rowOrg;
		HSSFCell cellPerson = null;
		HSSFCell cellOrg = null;
		int rowNum = 1;
		HSSFCellStyle style = this.getStyle(workbook);
		EUserType userType = null;
		/**
		 * HSSFDataFormat hssfDF = workbook.createDataFormat(); HSSFCellStyle
		 * style_right = this.getStyle(workbook);
		 * style_right.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		 * style_right.setDataFormat(hssfDF.getFormat("#,###,###,###,##0.00"));
		 **/
		
		/** 判断是导出个人还是机构，list(0).type是O，那这一定是机构的list了，list(0).type是P或者null,则为个人游客 **/
		if (list.size() > 0) {
			userType = list.get(0).getType();
		}
		/** userType == null: 只注册了普通账号,没有完善基本信息，是userType为null的游客 **/
		if (null == userType || EUserType.PERSON.equals(userType)) {
			for (MemberInfoDto memberInfoDto : list) {
				rowPerson = sheetPerson.createRow(rowNum);

				cellPerson = rowPerson.createCell(0);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellPerson.setCellValue(memberInfoDto.getUsername());
				cellPerson.setCellStyle(style);

				cellPerson = rowPerson.createCell(1);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellPerson.setCellValue(memberInfoDto.getName());
				cellPerson.setCellStyle(style);

				cellPerson = rowPerson.createCell(2);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				EGender personSex = memberInfoDto.getPersonSex();
				String personSexText = null;
				if (null != personSex) {
					personSexText = personSex.getText();
				}
				cellPerson.setCellValue(personSexText);
				cellPerson.setCellStyle(style);

				cellPerson = rowPerson.createCell(3);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellPerson.setCellValue(memberInfoDto.getPersonAge());
				cellPerson.setCellStyle(style);

				cellPerson = rowPerson.createCell(4);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				DynamicOption region = memberInfoDto.getRegion();
				String regionFullText = null;
				if (null != region) {
					regionFullText = region.getFullText();
				}
				cellPerson.setCellValue(regionFullText);
				cellPerson.setCellStyle(style);

				cellPerson = rowPerson.createCell(5);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellPerson.setCellValue(memberInfoDto.getMobile());
				cellPerson.setCellStyle(style);

				cellPerson = rowPerson.createCell(6);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellPerson.setCellValue(memberInfoDto.getPersonQQ());
				cellPerson.setCellStyle(style);

				cellPerson = rowPerson.createCell(7);
				cellPerson.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellPerson.setCellValue(memberInfoDto.getCreateTs());
				cellPerson.setCellStyle(style);
				rowNum++;
			}
		} else if (null != userType && EUserType.ORGANIZATION.equals(userType)) {
			for (MemberInfoDto memberInfoDto : list) {
				/** 机构游客信息 **/
				rowOrg = sheetOrg.createRow(rowNum);

				cellOrg = rowOrg.createCell(0);
				cellOrg.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellOrg.setCellValue(memberInfoDto.getUsername());
				cellOrg.setCellStyle(style);

				cellOrg = rowOrg.createCell(1);
				cellOrg.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellOrg.setCellValue(memberInfoDto.getName());
				cellOrg.setCellStyle(style);

				cellOrg = rowOrg.createCell(2);
				cellOrg.setCellType(HSSFCell.CELL_TYPE_STRING);
				DynamicOption region = memberInfoDto.getRegion();
				String regionFullText = null;
				if (null != region) {
					regionFullText = region.getFullText();
				}
				cellOrg.setCellValue(regionFullText);
				cellOrg.setCellStyle(style);

				cellOrg = rowOrg.createCell(3);
				cellOrg.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellOrg.setCellValue(memberInfoDto.getMobile());
				cellOrg.setCellStyle(style);

				cellOrg = rowOrg.createCell(4);
				cellOrg.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellOrg.setCellValue(memberInfoDto.getOrgQQ());
				cellOrg.setCellStyle(style);

				cellOrg = rowOrg.createCell(5);
				cellOrg.setCellType(HSSFCell.CELL_TYPE_STRING);
				cellOrg.setCellValue(memberInfoDto.getCreateTs());
				cellOrg.setCellStyle(style);
				rowNum++;
			}
		}
	}
}
