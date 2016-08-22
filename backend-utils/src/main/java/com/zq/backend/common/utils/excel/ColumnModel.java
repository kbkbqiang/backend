package com.zq.backend.common.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 * 导出excel时，列模型
 * 
 * @author jiangping
 * 
 */
public class ColumnModel {
	private String title; // 标题
	private String fieldName; // 数据对象中的域名
	@SuppressWarnings("rawtypes")
	private DataFormat formatInter; // 格式化接口
	private HSSFCellStyle cellStyle;// 单元格样式
	private boolean isCount; // 是否计算统计数据，用于显示，默认false，即不需要，若为true,则在数据最后输出后输出统计数据
	private Object formatParam; // 格式化格式
	private Integer width; // 列宽
	private String defaultValue; // 若输出的字符串为空时，默认输出的字符串，例如:"--"

	public ColumnModel() {
		this(null, null, null, null, false, null);
	}

	public ColumnModel(String title, String fieldName) {
		this(title, fieldName, null, null, false, null);
	}

	public ColumnModel(String title, String fieldName, String defaultValue) {
		this(title, fieldName, null, null, false, null, null, defaultValue);
	}

	@SuppressWarnings("rawtypes")
	public ColumnModel(String title, String fieldName, DataFormat format) {
		this(title, fieldName, format, null, false, null, null, null);
	}

	@SuppressWarnings("rawtypes")
	public ColumnModel(String title, String fieldName, DataFormat formatInter,
			HSSFCellStyle cellStyle, boolean isCount, Object formatParam) {
		this(title, fieldName, formatInter, cellStyle, isCount, formatParam,
				null);

	}

	@SuppressWarnings("rawtypes")
	public ColumnModel(String title, String fieldName, DataFormat formatInter,
			HSSFCellStyle cellStyle, boolean isCount, Object formatParam,
			Integer width) {
		this(title, fieldName, formatInter, cellStyle, isCount, formatParam,
				width, null);
		// this.title = title;
		// this.fieldName = fieldName;
		// this.formatInter = formatInter;
		// this.cellStyle = cellStyle;
		// this.isCount = isCount;
		// this.formatParam = formatParam;
		// this.width = width;
	}

	@SuppressWarnings("rawtypes")
	public ColumnModel(String title, String fieldName, DataFormat formatInter,
			HSSFCellStyle cellStyle, boolean isCount, Object formatParam,
			Integer width, String defaultValue) {
		this.title = title;
		this.fieldName = fieldName;
		this.formatInter = formatInter;
		this.cellStyle = cellStyle;
		this.isCount = isCount;
		this.formatParam = formatParam;
		this.width = width;
		this.defaultValue = defaultValue;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public HSSFCellStyle getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(HSSFCellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isCount() {
		return isCount;
	}

	public void setCount(boolean isCount) {
		this.isCount = isCount;
	}

	@SuppressWarnings("rawtypes")
	public DataFormat getFormatInter() {
		return formatInter;
	}

	@SuppressWarnings("rawtypes")
	public void setFormatInter(DataFormat formatInter) {
		this.formatInter = formatInter;
	}

	public Object getFormatParam() {
		return formatParam;
	}

	public void setFormatParam(Object formatParam) {
		this.formatParam = formatParam;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
