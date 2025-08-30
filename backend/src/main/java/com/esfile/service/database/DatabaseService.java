package com.esfile.service.database;

import com.esfile.entity.dto.SqlExecuteDto;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作服务接口
 * 定义数据库操作的所有方法
 */
public interface DatabaseService {
    
    /**
     * 执行SQL语句
     */
    Map<String, Object> executeSql(SqlExecuteDto sqlDto);
    
    /**
     * 获取表列表
     */
    List<Map<String, Object>> getTableList();
    
    /**
     * 获取表信息
     */
    Map<String, Object> getTableInfo(String tableName);
    
    /**
     * 获取表数据
     */
    Map<String, Object> getTableData(String tableName, int page, int size, String orderBy);
    
    /**
     * 获取数据库信息
     */
    Map<String, Object> getDatabaseInfo();
}
