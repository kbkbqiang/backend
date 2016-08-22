package com.zq.backend.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName: DBSqlUtil 
 * @Description TODO
 * @author zhaoqiang 
 * @date: 2016年8月22日 下午4:03:55
 */
public final class DBSqlUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBSqlUtil.class);
    private static ThreadLocal<Connection> CONNECTION_HOLDER = null;

    private static QueryRunner QUERY_RUNNER = null;

    private static BasicDataSource DATA_SOURCE = null;

    /*
    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();

        QUERY_RUNNER = new QueryRunner();

        Properties conf = PropsUtil.loadProps("config.properties");
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
        DATA_SOURCE.setMaxTotal(1024);
        DATA_SOURCE.setMaxIdle(128);
        DATA_SOURCE.setMaxWaitMillis(1024*15);
    }
    */

    public static void initDb(String driver,String url,String username,String password) {
        if(CONNECTION_HOLDER == null)
        {
            CONNECTION_HOLDER = new ThreadLocal<Connection>();

            QUERY_RUNNER = new QueryRunner();

            DATA_SOURCE = new BasicDataSource();
            DATA_SOURCE.setDriverClassName(driver);
            DATA_SOURCE.setUrl(url);
            DATA_SOURCE.setUsername(username);
            DATA_SOURCE.setPassword(password);
            DATA_SOURCE.setMaxTotal(1024);
            DATA_SOURCE.setMaxIdle(128);
            DATA_SOURCE.setMaxWaitMillis(1024*15);
        }
    }

    public static void setAutoCommit(boolean flag) throws Exception {
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(flag);
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
            throw e;
        }
    }

    public static void rollback(){
        try {
            Connection conn = getConnection();
            conn.rollback();
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
        }
    }

    public static void rollbackAndReset(){
        try {
            Connection conn = getConnection();
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
        }
    }

    public static void commit() throws Exception {
        try {
            Connection conn = getConnection();
            conn.commit();
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
            throw e;
        }
    }

    public static void commitAndReset() throws Exception {
        try {
            Connection conn = getConnection();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
            throw e;
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws Exception {
        if(CONNECTION_HOLDER == null) throw new Exception("can NOT init db yet");
        Connection conn = CONNECTION_HOLDER.get();
        if(conn != null && !conn.isValid(60 * 60 * 8)){
        	conn = null;
        	CONNECTION_HOLDER.remove();
        }
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (Exception e) {
                LOGGER.error("get connection failure", e);
                throw e;
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 执行查询语句
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws Exception {
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (Exception e) {
            LOGGER.error("execute query failure", e);
            throw e;
        }
        return result;
    }

    /**
     * 执行更新语句（包括：update、insert、delete）
     */
    public static int executeUpdate(String sql, Object... params) throws Exception {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (Exception e) {
            LOGGER.error("execute update failure", e);
            throw e;
        }
        return rows;
    }

    /**
     * 查询实体列表
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) throws Exception {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (Exception e) {
            LOGGER.error("query entity list failure", e);
            throw e;
        }
        return entityList;
    }

    /**
     * 查询 表列 列表
     */
    public static <T> List<T> queryColumnList(String cname, String sql, Object... params) throws Exception {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new ColumnListHandler<T>(cname), params);
        } catch (Exception e) {
            LOGGER.error("query column list failure", e);
            throw e;
        }
        return entityList;
    }

    /**
     * 查询行数
     */
    public static Long queryEntityCount(String sql, Object... params) throws Exception {
        Long count = 0L;
        try {
            Connection conn = getConnection();
            count = QUERY_RUNNER.query(conn, sql, new ResultSetHandler<Long>(){
                public Long handle(ResultSet rs) throws SQLException {
                    if(rs.next()){
                        return rs.getLong(1);
                    }
                    return 0L;
                }
            }, params);
        } catch (Exception e) {
            LOGGER.error("query entity list failure", e);
            throw e;
        }
        return count;
    }

    /**
     * 查询实体
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) throws Exception {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (Exception e) {
            LOGGER.error("query entity failure", e);
            throw e;
        }
        return entity;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap,String tableName) throws Exception {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "";
        if(tableName == null || tableName.isEmpty())
            sql = "INSERT INTO " + getTableName(entityClass);
        else
            sql = "INSERT INTO " + tableName;
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) throws Exception {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) throws Exception {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql, id) == 1;
    }

    /**
     * 执行 SQL 文件
     */
    public static void executeSqlFile(String filePath) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (Exception e) {
            LOGGER.error("execute sql file failure", e);
            throw e;
        }
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }
}
