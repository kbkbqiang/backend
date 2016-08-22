package com.zq.backend.common.utils.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.zq.backend.common.utils.DateUtils;



/**
 * 导出excel文件 ； 设置excel输出的列信息时,按添加的顺序输出，注意添加的顺序； 有两种新增列的方式： addColmn ：
 * 新增列，返回ExcelUtils对象； addColmnModel ： 新增列，返回列模型对象columnModel；
 * 区别在于addColmn可以excelUtils
 * .addColmn(...).addColmn(...);而addColmnModel可以在excelUtils
 * .addColmnModel(...)后继续设置该列的属性；
 * 
 * @author jiangping
 * 
 * @param <T>
 */
@SuppressWarnings("deprecation")
public class ExcelUtils<T> {
	private Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	private List<T> dataList; // 从数据库中查询出来的列表

	private List<ColumnModel> cmList; // 列信息，包括标题，对应T的域名，数据格式化接口对象
	private String headString; // 头部信息
	private String[] params; // 统计时间
	private String headStatisticsDes;	//头部统计信息

	private List<Integer> countIndexList = new ArrayList<Integer>(); // 需要进行统计的列的序号，用于最后一行的统计数据
	private BigDecimal[] countValues; // 统计结果值，用于最后一行的统计数据

	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private int currentRow = 0;

	private HSSFCellStyle cellStyleTitle;// 标题样式
	private HSSFCellStyle cellStyleHead;// head样式
	private HSSFCellStyle cellStyleTime;// 统计时间样式
	private HSSFCellStyle cellStyleContent;// 统计时间样式

	private Method[] methods; // 泛型T的类所具有的方法
	private int methodCount = 0; // 泛型T的类所具有的方法数量

	private DecimalFormat df = new DecimalFormat("#.00"); // 统计数据的格式化
	private int defaultColumnWidth = 3000;

	private boolean hasNo = true;
	private String indexStr = "";

	/**
	 * 输出Excel文件
	 * 
	 * @param response
	 * @param fileName
	 *            文件名，若不为空，则以日期作为文件名
	 */
	public void outputExcel(HttpServletResponse response, String fileName) {
		OutputStream ouputStream = null;
		try {

			// 设置响应类型和头
			fileName = StringUtils.isEmpty(fileName) ? DateUtils.formatDate(
					new Date(), DateUtils.DATETIME_FORMAT) + ".xls" : fileName;
			// 若文件名无后缀则添加后缀
			fileName = fileName.indexOf(".") == -1 ? (fileName + ".xls")
					: fileName;
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName);
			// 获取输出流
			ouputStream = response != null ? response.getOutputStream() : null;
			// 向流中输出excel内容
			outputExcel(ouputStream);
		} catch (IOException e) {
			logger.error("导出Excel异常：",e);
		} finally {
			if (ouputStream != null) {
				try {
					ouputStream.flush();
					ouputStream.close();
				} catch (Exception e) {
				}
			}
			if (response != null) {
				try {
					response.flushBuffer();
				} catch (IOException e) {
					logger.error("导出Excel，刷新response异常：",e);
				}
			}
		}

	}

	/**
	 * 输入EXCEL文件
	 * 
	 * @param fileName
	 *            文件名
	 */
	public void outputExcel(OutputStream ouputStream) {
		// FileOutputStream fos = null;
		try {
			// 建立excel头信息
			createNormalHead();
			// excel统计时间信息
			createNormalTwoRow();
			// 标题
			createColumHeader();
			// 内容信息
			createColumContent();
			// 统计信息
			createLastSumRow();

			// 输出excel
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (FileNotFoundException e) {
			logger.error("导出Excel异常：",e);
		} catch (IOException e) {
			logger.error("导出Excel异常：",e);
		}
	}

	public ExcelUtils(List<T> dataList) {
		this(dataList, null, null, null, null, null, null);
	}

	public ExcelUtils(List<T> dataList, String sheetName) {
		this(dataList, null, null, null, null, null, sheetName);
	}

	public ExcelUtils(List<T> dataList, List<ColumnModel> cmList) {
		this(dataList, cmList, null, null, null, null, null);
	}

	/**
	 * 基础构造方法，初始化基本数据，统计数据，内容和标题样式等
	 * 
	 * @param dataList
	 * @param cmList
	 * @param headString
	 * @param params
	 */
	public ExcelUtils(List<T> dataList, List<ColumnModel> cmList,
			String headString, String[] params, HSSFWorkbook wb,
			HSSFSheet sheet, String sheetName) {
		this.dataList = dataList;
		this.cmList = cmList;
		this.headString = headString;
		this.params = params;
		// 初始化excel表空间
		this.wb = wb;
		this.sheet = sheet;
		if (null == this.wb) {
			this.wb = new HSSFWorkbook();
			wb = this.wb;
		}
		if (null == this.sheet) {

			sheetName = StringUtils.isEmpty(sheetName) ? DateUtils.format(
					new Date(), DateUtils.DATETIME_FORMAT) : sheetName;

			this.sheet = this.wb.createSheet(sheetName);
			sheet = this.sheet;
		}
		// 初始化泛型方法
		if (null != dataList && dataList.size() > 0) {
			methods = dataList.get(0).getClass().getMethods();
			methodCount = methods.length;
		}
		// 初始化统计数据
		if (null != cmList) {
			countValues = new BigDecimal[cmList.size()];
			for (int i = 0; i < getColumnCount(); i++) {
				countValues[i] = new BigDecimal(0);
			}
		}
		// 初始化cellStyleContent内容样式
		cellStyleContent = wb.createCellStyle();
		cellStyleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyleContent.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyleContent.setWrapText(true);// 指定单元格自动换行
		cellStyleContent.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

		// 初始化cellStyleTitle标题样式
		cellStyleTitle = wb.createCellStyle();
		cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyleTitle.setWrapText(true);// 指定单元格自动换行

		// 单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setColor(HSSFColor.WHITE.index);
		font.setFontHeight((short) 260);
		cellStyleTitle.setFont(font);

		// 设置单元格背景色
		cellStyleTitle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		cellStyleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	}

	public ExcelUtils<T> addColmn(String title, String fieldName) {

		return addColmn(title, fieldName, null, null, false, null, null, null);

	}

	/**
	 * 新增列，并返回该列
	 * 
	 * @param title
	 * @param fieldName
	 * @return
	 */
	public ColumnModel addColmnModel(String title, String fieldName) {
		if (null == cmList) {
			cmList = new ArrayList<ColumnModel>();
		}
		ColumnModel model = new ColumnModel(title, fieldName, null, null,
				false, null, null, null);
		cmList.add(model);

		return model;
	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 * @param fieldName
	 * @param defaultValue
	 * @return
	 */
	public ExcelUtils<T> addColmn(String title, String fieldName,
			String defaultValue) {

		return addColmn(title, fieldName, null, null, false, null,
				defaultValue, null);

	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 * @param fieldName
	 * @param isCount
	 * @return
	 */
	public ExcelUtils<T> addColmn(String title, String fieldName,
			boolean isCount) {
		return addColmn(title, fieldName, null, null, isCount, null, null, null);

	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 * @param fieldName
	 * @param format
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ExcelUtils<T> addColmn(String title, String fieldName,
			DataFormat format) {
		return addColmn(title, fieldName, format, null, false, null, null, null);

	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 * @param fieldName
	 * @param format
	 * @param formatParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ExcelUtils<T> addColmn(String title, String fieldName,
			DataFormat format, Object formatParam) {
		return addColmn(title, fieldName, format, null, false, null, null,
				formatParam);

	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 * @param fieldName
	 * @param format
	 * @param defaultValue
	 * @param formatParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ExcelUtils<T> addColmn(String title, String fieldName,
			DataFormat format, String defaultValue, Object formatParam) {

		return addColmn(title, fieldName, format, null, false, null,
				defaultValue, formatParam);

	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 * @param fieldName
	 * @param format
	 * @param cellStyle
	 * @param isCount
	 * @param formatParam
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ExcelUtils<T> addColmn(String title, String fieldName,
			DataFormat format, HSSFCellStyle cellStyle, boolean isCount,
			Object formatParam) {

		return addColmn(title, fieldName, format, cellStyle, isCount, null,
				null, formatParam);
	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 *            标题
	 * @param fieldName
	 *            T对象中的域名
	 * @param format
	 *            格式化对象
	 * @param cellStyle
	 *            该列的内容样式
	 * @param isCount
	 *            是否做统计
	 * @param width
	 *            列宽
	 * @param formatParam
	 *            格式化参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ExcelUtils<T> addColmn(String title, String fieldName,
			DataFormat format, HSSFCellStyle cellStyle, boolean isCount,
			Integer width, Object formatParam) {
		return addColmn(title, fieldName, format, cellStyle, isCount, width,
				null, formatParam);
	}

	/**
	 * 新增列，并返回本对象
	 * 
	 * @param title
	 *            标题
	 * @param fieldName
	 *            T对象中的域名
	 * @param format
	 *            格式化对象
	 * @param cellStyle
	 *            该列的内容样式
	 * @param isCount
	 *            是否做统计
	 * @param width
	 *            列宽
	 * @param defaultValue
	 *            若该列中数据为空时，默认的设置
	 * @param formatParam
	 *            格式化参数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ExcelUtils<T> addColmn(String title, String fieldName,
			DataFormat format, HSSFCellStyle cellStyle, boolean isCount,
			Integer width, String defaultValue, Object formatParam) {
		if (null == cmList) {
			cmList = new ArrayList<ColumnModel>();
		}
		ColumnModel model = new ColumnModel(title, fieldName, format,
				cellStyle, isCount, formatParam, width, defaultValue);
		cmList.add(model);

		return this;
	}

	/**
	 * 返回列数
	 * 
	 * @return
	 */
	public int getColumnCount() {
		if (null == cmList) {
			return 0;
		}
		return cmList.size();
	}

	/**
	 * 返回行数，不包括标题
	 * 
	 * @return
	 */
	public int getRowCount() {
		if (null == dataList) {
			return 0;
		}
		return dataList.size();
	}

	/**
	 * 建立内容列表
	 */
	@SuppressWarnings("unchecked")
	public void createColumContent() {
		// 若无列属性，直接返回
		int colLen = getColumnCount();
		if (colLen < 1) {
			return;
		}
		// 若无行数据，直接返回
		int dataLen = getRowCount();
		if (dataLen < 1) {
			return;
		}

		for (int i = 0; i < dataLen; i++) {
			T obj = dataList.get(i);
			// 创建一行
			HSSFRow row = sheet.createRow(currentRow++);

			if (hasNo) {
				HSSFCell cell = row.createCell(0);
				cell.setCellType(HSSFCell.ENCODING_UTF_16);
				cell.setCellStyle(cellStyleContent);
				cell.setCellValue(new HSSFRichTextString(String.valueOf(i + 1)));
			}
			for (int j = 0; j < colLen; j++) {
				try {
					ColumnModel model = cmList.get(j);
					Object val = getValue(obj, model);
					// 判断是否为统计数据，若为统计数据，计算统计值
					if (model.isCount()) {
						try {
							countValues[j] = countValues[j].add(new BigDecimal(
									String.valueOf(val)));
						} catch (Exception e) {

						}
					}
					String val2 = null != val ? String.valueOf(val) : null;
					DataFormat<T> f = model.getFormatInter();
					if (null != f) {
						val2 = f.format(val, obj, model.getFormatParam());
					}
					row.setHeight((short) 500);
					// 创建一个单元格
					HSSFCell cell = row.createCell(hasNo ? j + 1 : j);
					cell.setCellType(HSSFCell.ENCODING_UTF_16);
					// 判断是否为空对象，若为空对象看是否有默认值defaultValue，若有则现实defaultValue
					if (StringUtils.isEmpty(val2)
							&& !StringUtils.isEmpty(model.getDefaultValue())) {
						val2 = model.getDefaultValue();
					} else if (null == val2 && (null != val)) {
						val2 = String.valueOf(val);
					} else if (null == val2) {
						val2 = "";
					}
					cell.setCellValue(new HSSFRichTextString(val2));
					if (null != model.getCellStyle()) {
						cell.setCellStyle(model.getCellStyle());
					} else {
						cell.setCellStyle(cellStyleContent);
					}
				} catch (Exception e) {
					logger.info("导出Excel是出现问题数据" + e.getMessage());
				}

			}
		}
	}

	/**
	 * 创建合计行 int colSum, String[] cellValue
	 * 
	 * @param colSum
	 *            需要合并到的列索引
	 * @param cellValue
	 */
	public void createLastSumRow() {
		if (null == countIndexList || countIndexList.size() < 1) {
			return;
		}
		int colSum = countIndexList.get(0) - 1;
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);

		HSSFRow lastRow = sheet.createRow((short) (currentRow++));
		HSSFCell sumCell = lastRow.createCell(0);

		sumCell.setCellValue(new HSSFRichTextString("合计"));
		sumCell.setCellStyle(cellStyle);

		sheet.addMergedRegion(new Region(sheet.getLastRowNum(), (short) 0,
				sheet.getLastRowNum(), (short) colSum));
		int count = countIndexList.size();

		for (int i = 0; i < count; i++) {
			int index = countIndexList.get(i);
			BigDecimal bd = countValues[index];
			sumCell = lastRow.createCell(index);
			sumCell.setCellStyle(cellStyle);
			sumCell.setCellValue(new HSSFRichTextString(df.format(bd)));
		}

	}

	/**
	 * 获取数据库中查询出来的数据，就行格式化后返回
	 * 
	 * @param obj
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object getValue(T obj, ColumnModel model) {
		Object v = null;
		if (obj instanceof Map) {
			Map m = (Map) obj;
			v = m.get(model.getFieldName());

		} else {
			if (methodCount < 1) {
				return "";
			}
			String fieldName = model.getFieldName();
			fieldName = StringUtils.isEmpty(fieldName) ? fieldName : fieldName
					.substring(0, 1).toUpperCase() + fieldName.substring(1);
			String methodName = "get" + fieldName;
			for (int i = 0; i < methodCount; i++) {
				Method m = methods[i];
				if (m.getName().equals(methodName)) {
					try {
						v = m.invoke(obj);
					} catch (IllegalAccessException e) {
						logger.error("导出Excel反向映射获取值异常：",e);
					} catch (IllegalArgumentException e) {
						logger.error("导出Excel反向映射获取值异常：",e);
					} catch (InvocationTargetException e) {
						logger.error("导出Excel反向映射获取值异常：",e);
					}
					break;
				}
			}

		}

		return v;

	}

	/**
	 * 设置excel表格中列的宽度
	 * 
	 * @param columnIndex
	 *            列的索引
	 * @param width
	 *            宽度
	 */
	public void setColumnWidth(int columnIndex, int width) {
		sheet.setColumnWidth(hasNo ? columnIndex + 1 : columnIndex, width);
	}

	/**
	 * 设置报表标题
	 */
	public void createColumHeader() {
		if (null == cmList) {
			return;
		}
		// 设置统计数据初始化
		if (null != cmList
				&& (null == countValues || countValues.length < cmList.size())) {
			countValues = new BigDecimal[cmList.size()];
			for (int i = 0; i < getColumnCount(); i++) {
				countValues[i] = new BigDecimal(0);
			}
		}
		// 设置列头
		HSSFRow row2 = sheet.createRow(currentRow);
		currentRow++;
		// 指定行高
		row2.setHeight((short) 600);
		HSSFCell cell3 = null;
		// 若有序号，则建立序号列
		if (hasNo) {
			cell3 = row2.createCell(0);
			cell3.setCellType(HSSFCell.ENCODING_UTF_16);
			cell3.setCellStyle(cellStyleTitle);
			cell3.setCellValue(new HSSFRichTextString(indexStr));
		}

		for (int i = 0; i < cmList.size(); i++) {
			ColumnModel cm = cmList.get(i);
			String title = null != cm ? cm.getTitle() : "";
			cell3 = row2.createCell(hasNo ? i + 1 : i);
			cell3.setCellType(HSSFCell.ENCODING_UTF_16);
			cell3.setCellStyle(cellStyleTitle);
			cell3.setCellValue(new HSSFRichTextString(title));
			if (cm.isCount()) {
				countIndexList.add(i);
			}
			// 若用户设置了列宽,按用户设置的值来设置；若用户没有设置则按默认列宽来设置
			if (null != cm.getWidth()) {
				sheet.setColumnWidth(hasNo ? i + 1 : i, cm.getWidth());
			} else {
				sheet.setColumnWidth(hasNo ? i + 1 : i, defaultColumnWidth);
			}

		}

	}

	/**
	 * 创建通用EXCEL头部
	 * 
	 * @param headString
	 *            头部显示的字符
	 * @param colSum
	 *            该报表的列数
	 */
	public void createNormalHead() {
		if (StringUtils.isEmpty(headString) || null == cmList
				|| cmList.size() < 1) {
			return;
		}
		HSSFRow row = sheet.createRow(currentRow);
		currentRow++;
		// 设置第一行
		HSSFCell cell = row.createCell(0);
		row.setHeight((short) 700);

		// 定义单元格为字符串类型
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(new HSSFRichTextString(headString));

		// 指定合并区域
		short colSize = (short) (hasNo?cmList.size():cmList.size()-1);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, colSize));
		// sheet.addMergedRegion(cellRegion);
		if (null == cellStyleHead) {
			cellStyleHead = wb.createCellStyle();
			cellStyleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
			cellStyleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
			cellStyleHead.setWrapText(true);// 指定单元格自动换行

			// 设置单元格字体
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			font.setFontName("宋体");
			font.setFontHeight((short) 500);
			cellStyleHead.setFont(font);
		}
		cell.setCellStyle(cellStyleHead);
	}

	/**
	 * 创建通用报表第二行
	 * 
	 * @param params
	 *            统计条件数组
	 * @param colSum
	 *            需要合并到的列索引
	 */
	public void createNormalTwoRow() {
		if (((null == params || params.length < 1) && StringUtils
				.isEmpty(headStatisticsDes))
				|| null == cmList
				|| cmList.size() < 1) {
			return;
		}
		HSSFRow row1 = sheet.createRow(currentRow);
		currentRow++;
		row1.setHeight((short) 300);

		HSSFCell cell2 = row1.createCell(0);

		cell2.setCellType(HSSFCell.ENCODING_UTF_16);
		if (StringUtils.isEmpty(headStatisticsDes)) {
			cell2.setCellValue(new HSSFRichTextString("统计时间：" + params[0] + "至"
					+ params[1]));
		} else {
			cell2.setCellValue(headStatisticsDes);
		}

		// 指定合并区域
		short colSize = (short) (hasNo?cmList.size():cmList.size()-1);
		sheet.addMergedRegion(new Region(1, (short) 0, 1, colSize));
		// sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) colSum));
		if (null == cellStyleTime) {
			cellStyleTime = wb.createCellStyle();
			cellStyleTime.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
			cellStyleTime.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
			cellStyleTime.setWrapText(true);// 指定单元格自动换行

			// 设置单元格字体
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName("宋体");
			font.setFontHeight((short) 250);
			cellStyleTime.setFont(font);
		}

		cell2.setCellStyle(cellStyleTime);

	}

	public HSSFCellStyle getCellStyleTitle() {
		return cellStyleTitle;
	}

	public void setCellStyleTitle(HSSFCellStyle cellStyleTitle) {
		this.cellStyleTitle = cellStyleTitle;
	}

	public String getHeadString() {
		return headString;
	}

	public void setHeadString(String headString) {
		this.headString = headString;
	}

	public HSSFCellStyle getCellStyleHead() {
		return cellStyleHead;
	}

	public void setCellStyleHead(HSSFCellStyle cellStyleHead) {
		this.cellStyleHead = cellStyleHead;
	}

	public HSSFCellStyle getCellStyleTime() {
		return cellStyleTime;
	}

	public void setCellStyleTime(HSSFCellStyle cellStyleTime) {
		this.cellStyleTime = cellStyleTime;
	}

	public HSSFCellStyle getCellStyleContent() {
		return cellStyleContent;
	}

	public void setCellStyleContent(HSSFCellStyle cellStyleContent) {
		this.cellStyleContent = cellStyleContent;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
		// 初始化泛型方法
		if (null != dataList && dataList.size() > 0) {
			methods = dataList.get(0).getClass().getMethods();
			methodCount = methods.length;
		}
	}

	public List<ColumnModel> getCmList() {
		return cmList;
	}

	public void setCmList(List<ColumnModel> cmList) {
		this.cmList = cmList;
		// 初始化统计数据
		countValues = new BigDecimal[cmList.size()];
		for (int i = 0; i < getColumnCount(); i++) {
			countValues[i] = new BigDecimal(0);
		}
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public HSSFWorkbook getWb() {
		return wb;
	}

	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public DecimalFormat getDf() {
		return df;
	}

	public void setDf(DecimalFormat df) {
		this.df = df;
	}

	public int getDefaultColumnWidth() {
		return defaultColumnWidth;
	}

	public void setDefaultColumnWidth(int defaultColumnWidth) {
		this.defaultColumnWidth = defaultColumnWidth;
	}

	public boolean isHasNo() {
		return hasNo;
	}

	public void setHasNo(boolean hasNo) {
		this.hasNo = hasNo;
	}

	public String getIndexStr() {
		return indexStr;
	}

	public void setIndexStr(String indexStr) {
		this.indexStr = indexStr;
	}
	
	public String getHeadStatisticsDes() {
		return headStatisticsDes;
	}

	public void setHeadStatisticsDes(String headStatisticsDes) {
		this.headStatisticsDes = headStatisticsDes;
	}

}
