package com.esfile.service.database;

import java.util.List;
import java.util.Map;

/**
 * 数据库工具服务接口
 */
public interface DatabaseToolsService {
    
    /**
     * SQL格式化
     */
    Map<String, Object> formatSql(String sql);
    
    /**
     * 导出查询结果
     */
    Map<String, Object> exportQueryResult(String format, List<Map<String, Object>> data);
    
    /**
     * SQL分析
     */
    Map<String, Object> analyzeSql(String sql);
    
    /**
     * 获取SQL模板
     */
    List<Map<String, Object>> getSqlTemplates();
    
    /**
     * 创建SQL模板
     */
    boolean createSqlTemplate(Map<String, Object> template);
    
    /**
     * 更新SQL模板
     */
    boolean updateSqlTemplate(Long id, Map<String, Object> template);
    
    /**
     * 删除SQL模板
     */
    boolean deleteSqlTemplate(Long id);
}
