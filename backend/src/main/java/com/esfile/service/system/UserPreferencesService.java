package com.esfile.service.system;

import java.util.Map;

/**
 * 用户偏好设置服务接口
 */
public interface UserPreferencesService {

    /**
     * 获取用户偏好设置
     */
    Map<String, Object> getUserPreferences(Long userId);

    /**
     * 更新用户偏好设置
     */
    boolean updateUserPreferences(Long userId, Map<String, Object> preferences);

    /**
     * 获取用户主题设置
     */
    Map<String, Object> getUserThemeSettings(Long userId);

    /**
     * 更新用户主题设置
     */
    boolean updateUserThemeSettings(Long userId, Map<String, Object> theme);

    /**
     * 获取用户语言设置
     */
    Map<String, Object> getUserLanguageSettings(Long userId);

    /**
     * 更新用户语言设置
     */
    boolean updateUserLanguageSettings(Long userId, Map<String, Object> language);

    /**
     * 重置用户偏好设置
     */
    boolean resetUserPreferences(Long userId);

    /**
     * 导出用户设置
     */
    Map<String, Object> exportUserSettings(Long userId);

    /**
     * 导入用户设置
     */
    boolean importUserSettings(Long userId, Map<String, Object> settings);
}
