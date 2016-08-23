/**   
* @Title: HandleDataSource.java
* @Package com.niiwoo.dao.annotation
* @Description: TODO(用一句话描述该文件做什么)
* @author seven   
* @date 2014-10-14 上午9:28:43
* @version V1.0   
*/


package com.backend.dao.separate;

/**
 * @ClassName: HandleDataSource
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author seven
 * @date 2014-10-14 上午9:28:43
 * 
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
}
