package com.genauth.sys.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Excelexport {
	private HttpServletResponse response;
	private HttpServletRequest request;
	private String excelName;
	private int EXCELSHEETSIZE = 10000;

	private Logger logger = Logger.getLogger(Excelexport.class);

	/**
	 * 针对Map导出数据 excelName 文件名称
	 * 
	 * @param request
	 * @param response
	 * @param excelName
	 */
	public Excelexport(HttpServletRequest request, HttpServletResponse response, String excelName) {
		this.excelName = excelName;
		this.response = response;
		this.request = request;
		setHeader();
	}

	/**
	 * http 头部信息设置
	 */
	private void setHeader() {
		SimpleDateFormat sdf = null;
		final String userAgent = request.getHeader("USER-AGENT");
		try {
			Calendar cal = Calendar.getInstance();
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String today = sdf.format(cal.getTimeInMillis());
			String fileName = excelName + today + ".xls";
			String finalFileName = null;
			if (userAgent.indexOf("MSIE") > -1) {
				finalFileName = URLEncoder.encode(fileName, "UTF8");
			} else if (userAgent.indexOf("Mozilla") > -1) {
				finalFileName = new String(fileName.getBytes(), "ISO8859-1");
			} else {
				finalFileName = URLEncoder.encode(fileName, "UTF8");
			}
			response.setHeader("Content-disposition", "attachment;filename=" + finalFileName);
			response.setContentType("application/vnd.ms-excel");
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isNumeric(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		// 判断是否是数字类型
		String strpat = "^[0-9]+(.[0-9]*)?$";
		Pattern pattern = Pattern.compile(strpat);
		return pattern.matcher(str).matches();
	}

	/**
	 * title 报表标题 infolist 导出的数据 ,每一行数据用一个数据存储，顺序必须与导出字段的类型一致 columnNames 导出字段
	 * 
	 * @throws IOException
	 * @throws WriteException
	 */
	public void export(String title, List<String[]> infolist, String... columnNames) {
		int size = infolist.size();
		logger.info("ExcelExport: startExport size=" + size);
		WritableWorkbook wwb = null;
		// DecimalFormat df=new DecimalFormat("#0.00");
		WorkbookSettings wbSetting = new WorkbookSettings();
		wbSetting.setUseTemporaryFileDuringWrite(true);
		// wbSetting.setTemporaryFileDuringWriteDirectory(new
		// File(excelPath));//临时文件夹的位置
		try {
			// 创建sheet表格
			wwb = Workbook.createWorkbook(response.getOutputStream(), wbSetting);
			if (size <= EXCELSHEETSIZE) {
				WritableSheet ws = wwb.createSheet("sheet", 0);
				logger.info("ExcelExport: create Sheet= sheet");
				exportSheetData(ws, title, infolist, columnNames);
				infolist.clear();
			} else {
				for (int page = 0; page <= size / EXCELSHEETSIZE; page++) {
					int endIndex = infolist.size() > EXCELSHEETSIZE ? EXCELSHEETSIZE : infolist.size();
					List<String[]> sheetData = infolist.subList(0, endIndex);
					WritableSheet ws = wwb.createSheet("sheet" + (page + 1), page);
					logger.info("ExcelExport: sheet= sheet" + (page + 1));
					exportSheetData(ws, title, sheetData, columnNames);
					sheetData.clear();// 数据量太大时,每次获取一部分,处理完后清空
				}
			}
			wwb.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wwb != null) {
				try {
					wwb.close();
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void exportSheetData(WritableSheet ws, String title, List<String[]> infolist, String... columnNames)
			throws Exception {
		for (int i = 1; i < columnNames.length; i++) {
			ws.setColumnView(i - 1, 17);
		}
		// 设置默认宽度
		ws.getSettings().setDefaultColumnWidth(15);

		// 设置格式
		WritableCellFormat cellFormat = new WritableCellFormat();
		cellFormat.setAlignment(Alignment.CENTRE);
		cellFormat.setBackground(Colour.GRAY_50);
		cellFormat.setBorder(Border.ALL, BorderLineStyle.DASHED);

		// 设置第二种格式
		WritableCellFormat cellFormat2 = new WritableCellFormat();
		cellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
		cellFormat2.setShrinkToFit(false);

		// 设置字体
		WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 13, WritableFont.BOLD);
		WritableCellFormat wcf1 = new WritableCellFormat(wf1);

		// NumberFormat nf = new NumberFormat("#.00");
		// WritableCellFormat wcf2 = new WritableCellFormat(nf);
		// 写入标题
		jxl.write.Label labelC;
		// jxl.write.Number number;
		labelC = new jxl.write.Label(0, 0, title);
		labelC.setCellFormat(cellFormat);
		ws.addCell(labelC);
		ws.mergeCells(0, 0, columnNames.length - 1, 0);

		for (int i = 0; i < columnNames.length; i++) {
			String columnname = columnNames[i];
			labelC = new jxl.write.Label(i, 1, columnname, wcf1);
			labelC.setCellFormat(cellFormat);
			ws.addCell(labelC);
		}

		for (int i = 0; i < infolist.size(); i++) {
			String[] array = infolist.get(i);
			for (int j = 0; j < array.length; j++) {// 输出一行
				// if(isNumeric(array[j])){
				//
				// try {
				// double dou=Double.parseDouble(array[j]);
				//// labelC = new jxl.write.Label(j, i+2, df.format(dou), wcf1);
				//// labelC.setCellFormat(cellFormat2);
				//// ws.addCell(labelC);
				// number = new jxl.write.Number(j, i+2, dou, wcf2);
				// number.setCellFormat(cellFormat2);
				// ws.addCell(number);
				// } catch (Exception e) {
				// labelC = new jxl.write.Label(j, i+2, array[j], wcf1);
				// labelC.setCellFormat(cellFormat2);
				// ws.addCell(labelC);
				// }
				// }else{
				//他妈的。 这种配置式的东西，从外面传进来的array[j]有可能是Null.
				if(null !=  array[j]){
					labelC = new jxl.write.Label(j, i + 2, "" + array[j], wcf1);
					labelC.setCellFormat(cellFormat2);
					ws.addCell(labelC);
				}
				// }
			}
			array = null;
		}
	}
	/**
	 * 将List<T>类型的数据导出到excel,导出数据entity T 中@Column 的
	 * ExcelHeader()属性的数据列，以ExcelHeader()属性为列头 有问题,还没调整好
	 * 
	 * @param objList
	 *            导出数据
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	// public void export4ObjList(String title,List<Object> objList) throws
	// IllegalAccessException, IllegalArgumentException,
	// InvocationTargetException, NoSuchMethodException, SecurityException{
	// int size = objList.size();
	//
	// WritableWorkbook wwb=null;
	// //DecimalFormat df=new DecimalFormat("#0.00");
	// WorkbookSettings wbSetting = new WorkbookSettings();
	// wbSetting.setUseTemporaryFileDuringWrite(true);
	//// wbSetting.setTemporaryFileDuringWriteDirectory(new
	// File(excelPath));//临时文件夹的位置
	// try {
	// //创建sheet表格
	// wwb = Workbook.createWorkbook(response.getOutputStream(),wbSetting);
	// for(int page = 0 ; page <= size/EXCELSHEETSIZE ; page ++){
	// int endIndex = objList.size()>EXCELSHEETSIZE?
	// EXCELSHEETSIZE:objList.size();
	// String sheetName = "sheet"+(page+1);
	// List<Object> sheetList = objList.subList(0, endIndex);
	// if(sheetList.size()>0){
	// WritableSheet ws = wwb.createSheet(sheetName, page);
	// logger.info("ExcelExport: export4ObjList size="+size+";sheet=
	// "+sheetName);
	// exportSheetData4List(ws, title, sheetList);
	//
	// }
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// if(wwb!=null){
	// try {
	// wwb.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	/**
	 * ws 填充数据的sheet title 报表标题 sheetlist 要填充的数据
	 * 
	 * @throws IOException
	 * @throws WriteException
	 */
	// private void exportSheetData4List(WritableSheet ws,String
	// title,List<Object> sheetlist){
	// try{
	// //DecimalFormat df = new DecimalFormat("#0.00");
	// Class<?> entity = sheetlist.get(0).getClass();
	// Field[] fields = entity.getDeclaredFields();
	// String[] getterNames = new String[fields.length];
	// String[] columnNames = new String[fields.length];
	// for(int i = 0 ; i < fields.length; i ++){
	// Field field = fields[i];
	// Column col = field.getAnnotation(Column.class);
	// if(StringUtils.isNotEmpty(col.ExcelHeader())){
	// columnNames[i] = col.ExcelHeader();
	// }
	// getterNames[i] = "get"+field.getName().substring(0,
	// 1).toUpperCase()+field.getName().substring(1);
	// }
	// // 设置格式
	// WritableFont titlewf = new WritableFont(WritableFont.ARIAL, 14,
	// WritableFont.BOLD, false,
	// UnderlineStyle.NO_UNDERLINE, Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
	// WritableFont datawf = new WritableFont(WritableFont.ARIAL, 12,
	// WritableFont.NO_BOLD, false,
	// UnderlineStyle.NO_UNDERLINE, Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
	// // 标题格式
	// WritableCellFormat titleformat = new WritableCellFormat(titlewf);// 标题格式
	// titleformat.setAlignment(Alignment.CENTRE);
	// titleformat.setBackground(Colour.GRAY_50);
	// titleformat.setBorder(Border.ALL, BorderLineStyle.DASHED);
	//
	// // 设置第二种格式
	// WritableCellFormat dataformat = new WritableCellFormat(datawf);
	// dataformat.setAlignment(Alignment.CENTRE);
	// dataformat.setShrinkToFit(false);
	// dataformat.setBackground(Colour.GRAY_50);
	// dataformat.setBorder(Border.ALL, BorderLineStyle.DASHED);
	//
	// CellView cellView = new CellView();
	// cellView.setAutosize(true); //设置自动大小
	// cellView.setSize(18);
	// //sheet表格 样式 设置列宽
	// for (int i = 1; i < columnNames.length; i++) {
	// ws.setColumnView(i - 1, cellView);
	// }
	// ws.getSettings().setDefaultColumnWidth(15);// 设置默认宽度
	// //填充title
	// jxl.write.Label labelC;
	// labelC = new jxl.write.Label(0, 0, title);
	// labelC.setCellFormat(titleformat);
	// ws.addCell(labelC);
	// ws.mergeCells(0, 0, columnNames.length - 1, 0);
	// //填充title
	// for (int i = 0; i < columnNames.length; i++) {
	// String columnname = columnNames[i];
	// labelC = new Label(i, 1, columnname, titleformat);
	// ws.addCell(labelC);
	// }
	// //填充data
	// for(int row = 0 ; row < sheetlist.size(); row ++){
	// Object rowData = sheetlist.get(row);
	// for(int col = 0 ; col < fields.length; col ++){
	// Object colData = entity.getMethod(getterNames[col]).invoke(rowData);
	// labelC = new jxl.write.Label(row, col+2, ""+colData,
	// dataformat);//全部作为文本类型展示， 不区分数字格式
	// ws.addCell(labelC);
	// }
	// }
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// sheetlist.clear();
	// }
	//
	// }
}
