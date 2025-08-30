package com.esfile.service.system;

import java.util.List;
import java.util.Map;

/**
 * 系统配置服务接口
 */
public interface SystemConfigService {

    /**
     * 获取系统配置
     */
    List<Map<String, Object>> getSystemConfig(String category);

    /**
     * 更新系统配置
     */
    boolean updateSystemConfig(Map<String, Object> config);

    /**
     * 批量更新系统配置
     */
    boolean batchUpdateSystemConfig(List<Map<String, Object>> configs);

    /**
     * 获取主题设置
     */
    Map<String, Object> getThemeSettings();

    /**
     * 更新主题设置
     */
    boolean updateThemeSettings(Map<String, Object> theme);

    /**
     * 获取语言设置
     */
    Map<String, Object> getLanguageSettings();

    /**
     * 更新语言设置
     */
    boolean updateLanguageSettings(Map<String, Object> language);

    /**
     * 获取可用语言列表
     */
    List<Map<String, Object>> getAvailableLanguages();

    /**
     * 获取可用主题列表
     */
    List<Map<String, Object>> getAvailableThemes();

    /**
     * 重置系统配置
     */
    boolean resetSystemConfig();
}
