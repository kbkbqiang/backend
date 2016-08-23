/**   
* @Title: DynamicDataSource.java
* @Package com.niiwoo.dao.annotation
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2014-10-14 上午9:32:33
* @version V1.0   
*/


package com.backend.dao.separate;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ClassName: DynamicDataSource
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author seven
 * @date 2014-10-14 上午9:32:33
 * 
 */

public class DynamicDataSource extends AbstractRoutingDataSource{
	
	@Override
	protected Object determineCurrentLookupKey() {
		 return HandleDataSource.getDataSource();  
	}

}
