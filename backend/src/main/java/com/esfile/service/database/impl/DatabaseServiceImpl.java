package com.esfile.service.database.impl;

import com.esfile.entity.dto.SqlExecuteDto;
import com.esfile.service.database.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * 数据库操作服务实现类
 * 提供数据库查询、执行等操作功能
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Override
    public Map<String, Object> executeSql(SqlExecuteDto sqlDto) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String sql = sqlDto.getSql();
            String sqlType = getSqlType(sql);
            
            if ("SELECT".equalsIgnoreCase(sqlType)) {
                // 查询操作
                result = executeQuery(sql, sqlDto.getParameters());
            } else {
                // 更新操作
                result = executeUpdate(sql, sqlDto.getParameters());
            }
            
            result.put("success", true);
            result.put("sqlType", sqlType);
            result.put("executionTime", System.currentTimeMillis());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "SQL执行失败: " + e.getMessage());
            result.put("sqlType", "ERROR");
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getTableList() {
        List<Map<String, Object>> tables = new ArrayList<>();
        
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            String catalog = null;
            String schemaPattern = null;
            String tableNamePattern = "%";
            String[] types = {"TABLE"};
            
            ResultSet rs = metaData.getTables(catalog, schemaPattern, tableNamePattern, types);
            
            while (rs.next()) {
                Map<String, Object> table = new HashMap<>();
                table.put("tableName", rs.getString("TABLE_NAME"));
                table.put("tableType", rs.getString("TABLE_TYPE"));
                table.put("remarks", rs.getString("REMARKS"));
                table.put("catalog", rs.getString("TABLE_CAT"));
                table.put("schema", rs.getString("TABLE_SCHEM"));
                
                tables.add(table);
            }
            
            rs.close();
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "获取表列表失败: " + e.getMessage());
            tables.add(error);
        }
        
        return tables;
    }

    @Override
    public Map<String, Object> getTableInfo(String tableName) {
        Map<String, Object> tableInfo = new HashMap<>();
        
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            String catalog = null;
            String schema = null;
            
            // 获取表结构信息
            ResultSet columns = metaData.getColumns(catalog, schema, tableName, "%");
            List<Map<String, Object>> columnList = new ArrayList<>();
            
            while (columns.next()) {
                Map<String, Object> column = new HashMap<>();
                column.put("columnName", columns.getString("COLUMN_NAME"));
                column.put("dataType", columns.getString("TYPE_NAME"));
                column.put("columnSize", columns.getInt("COLUMN_SIZE"));
                column.put("nullable", columns.getInt("NULLABLE"));
                column.put("columnDefault", columns.getString("COLUMN_DEF"));
                column.put("remarks", columns.getString("REMARKS"));
                
                columnList.add(column);
            }
            
            // 获取主键信息
            ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, tableName);
            List<String> primaryKeyList = new ArrayList<>();
            
            while (primaryKeys.next()) {
                primaryKeyList.add(primaryKeys.getString("COLUMN_NAME"));
            }
            
            // 获取索引信息
            ResultSet indexes = metaData.getIndexInfo(catalog, schema, tableName, false, false);
            List<Map<String, Object>> indexList = new ArrayList<>();
            
            while (indexes.next()) {
                Map<String, Object> index = new HashMap<>();
                index.put("indexName", indexes.getString("INDEX_NAME"));
                index.put("columnName", indexes.getString("COLUMN_NAME"));
                index.put("nonUnique", indexes.getBoolean("NON_UNIQUE"));
                index.put("ascOrDesc", indexes.getString("ASC_OR_DESC"));
                
                indexList.add(index);
            }
            
            tableInfo.put("tableName", tableName);
            tableInfo.put("columns", columnList);
            tableInfo.put("primaryKeys", primaryKeyList);
            tableInfo.put("indexes", indexList);
            
            columns.close();
            primaryKeys.close();
            indexes.close();
            
        } catch (Exception e) {
            tableInfo.put("error", "获取表信息失败: " + e.getMessage());
        }
        
        return tableInfo;
    }

    @Override
    public Map<String, Object> getTableData(String tableName, int page, int size, String orderBy) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建查询SQL
            String countSql = "SELECT COUNT(*) FROM " + tableName;
            String dataSql = "SELECT * FROM " + tableName;
            
            if (orderBy != null && !orderBy.trim().isEmpty()) {
                dataSql += " ORDER BY " + orderBy;
            }
            
            // 添加分页
            if (page > 0 && size > 0) {
                dataSql += " LIMIT " + (page - 1) * size + ", " + size;
            }
            
            // 执行查询
            int total = jdbcTemplate.queryForObject(countSql, Integer.class);
            List<Map<String, Object>> data = jdbcTemplate.queryForList(dataSql);
            
            result.put("total", total);
            result.put("data", data);
            result.put("page", page);
            result.put("size", size);
            result.put("tableName", tableName);
            
        } catch (Exception e) {
            result.put("error", "获取表数据失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> dbInfo = new HashMap<>();
        
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            
            dbInfo.put("databaseProductName", metaData.getDatabaseProductName());
            dbInfo.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            dbInfo.put("driverName", metaData.getDriverName());
            dbInfo.put("driverVersion", metaData.getDriverVersion());
            dbInfo.put("url", metaData.getURL());
            dbInfo.put("userName", metaData.getUserName());
            dbInfo.put("maxConnections", metaData.getMaxConnections());
            dbInfo.put("maxTablesInSelect", metaData.getMaxTablesInSelect());
            
        } catch (Exception e) {
            dbInfo.put("error", "获取数据库信息失败: " + e.getMessage());
        }
        
        return dbInfo;
    }

    /**
     * 执行查询SQL
     */
    private Map<String, Object> executeQuery(String sql, List<Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> data;
            
            if (parameters != null && !parameters.isEmpty()) {
                data = jdbcTemplate.queryForList(sql, parameters.toArray());
            } else {
                data = jdbcTemplate.queryForList(sql);
            }
            
            result.put("data", data);
            result.put("rowCount", data.size());
            result.put("sql", sql);
            
        } catch (Exception e) {
            result.put("error", "查询执行失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 执行更新SQL
     */
    @Transactional
    private Map<String, Object> executeUpdate(String sql, List<Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int affectedRows;
            
            if (parameters != null && !parameters.isEmpty()) {
                affectedRows = jdbcTemplate.update(sql, parameters.toArray());
            } else {
                affectedRows = jdbcTemplate.update(sql);
            }
            
            result.put("affectedRows", affectedRows);
            result.put("sql", sql);
            
        } catch (Exception e) {
            result.put("error", "更新执行失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 判断SQL类型
     */
    private String getSqlType(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "UNKNOWN";
        }
        
        String upperSql = sql.trim().toUpperCase();
        
        if (upperSql.startsWith("SELECT")) {
            return "SELECT";
        } else if (upperSql.startsWith("INSERT")) {
            return "INSERT";
        } else if (upperSql.startsWith("UPDATE")) {
            return "UPDATE";
        } else if (upperSql.startsWith("DELETE")) {
            return "DELETE";
        } else if (upperSql.startsWith("CREATE")) {
            return "CREATE";
        } else if (upperSql.startsWith("DROP")) {
            return "DROP";
        } else if (upperSql.startsWith("ALTER")) {
            return "ALTER";
        } else {
            return "UNKNOWN";
        }
    }
}
