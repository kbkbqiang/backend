/**   
* @Title: HandleDataSource.java
* @Package com.niiwoo.dao.annotation
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2014-10-14 上午9:28:43
* @version V1.0   
*/


package com.backend.dao.separate;

import com.backend.dao.enums.DataSourceType;

/**
 * 
 * @ClassName: HandleDataSource 
 * @Description TODO 本地线程全局变量 保存一个线程安全的DatabaseType容器 
 * @author zhaoqiang 
 * @date: 2016年9月1日 上午9:16:07
 */
public class HandleDataSource {
	
    public static final ThreadLocal<String> holder = new ThreadLocal<String>();  
    public static void putDataSource(String datasource) {  
        holder.set(datasource);  
    }  
      
    public static String getDataSource() {  
        return holder.get();  
    } 
    
    public static void removeDataSource(){
    	holder.remove();
    }
    
    /** 
     * 当前是否选择了写数据源 
     */  
    public static boolean isChoiceWrite() {
        return DataSourceType.write.toString() == holder.get();  
    }  
      
    /** 
     * 当前是否选择了读数据源 
     */  
    public static boolean isChoiceRead() {  
        return DataSourceType.read.toString() == holder.get();  
    }  
    
    /** 
     * 是否还未设置数据源 
     */  
    public static boolean isChoiceNone() {  
        return null == holder.get();   
    }
    
    /** 
     * 标记为写数据源 
     */  
    public static void markWrite() {  
        holder.set(DataSourceType.write.toString());  
    }  
      
    /** 
     * 标记为读数据源 
     */  
    public static void markRead() {  
        holder.set(DataSourceType.read.toString());  
    }  
      
    /** 
     * 重置 
     */  
    public static void reset() {  
        holder.set(null);  
    }  
    
   
}
