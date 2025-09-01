package com.esfile.service.database.impl;

import com.esfile.service.database.DatabaseToolsService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 数据库工具服务实现类
 */
@Service
public class DatabaseToolsServiceImpl implements DatabaseToolsService {

    @Override
    public Map<String, Object> formatSql(String sql) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简单的SQL格式化逻辑
            String formattedSql = formatSqlString(sql);
            
            result.put("formattedSql", formattedSql);
            result.put("originalSql", sql);
            result.put("errors", new ArrayList<>());
            result.put("warnings", new ArrayList<>());
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("lineCount", formattedSql.split("\n").length);
            statistics.put("wordCount", formattedSql.split("\\s+").length);
            statistics.put("complexity", calculateComplexity(sql));
            result.put("statistics", statistics);
            
        } catch (Exception e) {
            result.put("error", "SQL格式化失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> exportQueryResult(String format, List<Map<String, Object>> data) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 模拟导出操作
            String filename = "export_" + System.currentTimeMillis() + "." + format;
            String downloadUrl = "/api/files/download/" + filename;
            
            result.put("downloadUrl", downloadUrl);
            result.put("filename", filename);
            result.put("size", data.size() * 1024); // 模拟文件大小
            result.put("expiresAt", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 24小时后过期
            
        } catch (Exception e) {
            result.put("error", "导出失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> analyzeSql(String sql) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> analysis = new HashMap<>();
            
            // 分析SQL复杂度
            String complexity = analyzeComplexity(sql);
            analysis.put("complexity", complexity);
            
            // 估算成本
            analysis.put("estimatedCost", estimateCost(sql));
            analysis.put("estimatedRows", estimateRows(sql));
            
            // 检查潜在问题
            List<String> warnings = checkWarnings(sql);
            analysis.put("warnings", warnings);
            
            // 提供建议
            List<String> suggestions = generateSuggestions(sql);
            analysis.put("suggestions", suggestions);
            
            result.put("analysis", analysis);
            
        } catch (Exception e) {
            result.put("error", "SQL分析失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getSqlTemplates() {
        List<Map<String, Object>> templates = new ArrayList<>();
        
        // 添加一些常用的SQL模板
        templates.add(createTemplate("查询表结构", "DESCRIBE table_name;", "查询"));
        templates.add(createTemplate("查看表数据", "SELECT * FROM table_name LIMIT 100;", "查询"));
        templates.add(createTemplate("查看索引", "SHOW INDEX FROM table_name;", "查询"));
        templates.add(createTemplate("查看表状态", "SHOW TABLE STATUS LIKE 'table_name';", "查询"));
        templates.add(createTemplate("查看数据库", "SHOW DATABASES;", "查询"));
        templates.add(createTemplate("查看表列表", "SHOW TABLES;", "查询"));
        
        return templates;
    }

    @Override
    public boolean createSqlTemplate(Map<String, Object> template) {
        try {
            // 这里可以保存到数据库
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateSqlTemplate(Long id, Map<String, Object> template) {
        try {
            // 这里可以更新数据库中的模板
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteSqlTemplate(Long id) {
        try {
            // 这里可以从数据库中删除模板
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 私有辅助方法
    private String formatSqlString(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }
        
        // 简单的格式化逻辑
        String formatted = sql.trim()
                .replaceAll("(?i)\\bSELECT\\b", "\nSELECT")
                .replaceAll("(?i)\\bFROM\\b", "\nFROM")
                .replaceAll("(?i)\\bWHERE\\b", "\nWHERE")
                .replaceAll("(?i)\\bORDER BY\\b", "\nORDER BY")
                .replaceAll("(?i)\\bGROUP BY\\b", "\nGROUP BY")
                .replaceAll("(?i)\\bHAVING\\b", "\nHAVING")
                .replaceAll("(?i)\\bLIMIT\\b", "\nLIMIT")
                .replaceAll("(?i)\\bJOIN\\b", "\nJOIN")
                .replaceAll("(?i)\\bLEFT JOIN\\b", "\nLEFT JOIN")
                .replaceAll("(?i)\\bRIGHT JOIN\\b", "\nRIGHT JOIN")
                .replaceAll("(?i)\\bINNER JOIN\\b", "\nINNER JOIN");
        
        return formatted;
    }

    private int calculateComplexity(String sql) {
        if (sql == null) return 0;
        
        int complexity = 1;
        String upperSql = sql.toUpperCase();
        
        // 根据SQL特征计算复杂度
        if (upperSql.contains("JOIN")) complexity += 2;
        if (upperSql.contains("WHERE")) complexity += 1;
        if (upperSql.contains("GROUP BY")) complexity += 1;
        if (upperSql.contains("ORDER BY")) complexity += 1;
        if (upperSql.contains("HAVING")) complexity += 2;
        if (upperSql.contains("UNION")) complexity += 3;
        if (upperSql.contains("SUBQUERY")) complexity += 2;
        
        return complexity;
    }

    private String analyzeComplexity(String sql) {
        int complexity = calculateComplexity(sql);
        
        if (complexity <= 2) return "low";
        else if (complexity <= 5) return "medium";
        else return "high";
    }

    private int estimateCost(String sql) {
        // 简单的成本估算
        return calculateComplexity(sql) * 100;
    }

    private int estimateRows(String sql) {
        // 简单的行数估算
        return calculateComplexity(sql) * 1000;
    }

    private List<String> checkWarnings(String sql) {
        List<String> warnings = new ArrayList<>();
        String upperSql = sql.toUpperCase();
        
        if (upperSql.contains("SELECT *")) {
            warnings.add("建议避免使用 SELECT *，明确指定需要的列");
        }
        
        if (upperSql.contains("LIMIT") && !upperSql.contains("ORDER BY")) {
            warnings.add("使用 LIMIT 时建议同时使用 ORDER BY 确保结果顺序");
        }
        
        if (upperSql.contains("LIKE '%") && upperSql.contains("%'")) {
            warnings.add("使用 LIKE '%...%' 可能导致全表扫描，影响性能");
        }
        
        return warnings;
    }

    private List<String> generateSuggestions(String sql) {
        List<String> suggestions = new ArrayList<>();
        String upperSql = sql.toUpperCase();
        
        if (upperSql.contains("SELECT *")) {
            suggestions.add("将 SELECT * 替换为具体的列名");
        }
        
        if (upperSql.contains("WHERE") && !upperSql.contains("INDEX")) {
            suggestions.add("考虑在 WHERE 条件中的列上创建索引");
        }
        
        return suggestions;
    }

    private Map<String, Object> createTemplate(String name, String sql, String category) {
        Map<String, Object> template = new HashMap<>();
        template.put("id", System.currentTimeMillis());
        template.put("name", name);
        template.put("description", name);
        template.put("category", category);
        template.put("sql", sql);
        template.put("tags", Arrays.asList(category));
        template.put("createTime", new Date());
        template.put("updateTime", new Date());
        return template;
    }
}
