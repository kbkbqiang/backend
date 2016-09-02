package com.backend.dao.separate;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
* 动态数据源（需要继承AbstractRoutingDataSource）
*/

public class DynamicDataSource extends AbstractRoutingDataSource{
	
	@Override
	protected Object determineCurrentLookupKey() {
		/**
		 * DataSourceAspect代码中使用setDataSourceType
		 * 设置当前的数据源，在路由类中使用getDataSourceType进行获取，
		 * 交给AbstractRoutingDataSource进行注入使用。
		 */
		return HandleDataSource.getDataSource();  
	}

}
