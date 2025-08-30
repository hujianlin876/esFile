package com.esfile.service.system.impl;

import com.esfile.service.system.UserPreferencesService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户偏好设置服务实现类
 */
@Service
public class UserPreferencesServiceImpl implements UserPreferencesService {

    // 模拟数据库存储，实际应该从数据库读取
    private final Map<Long, Map<String, Object>> userPreferences = new HashMap<>();
    private final Map<Long, Map<String, Object>> userThemes = new HashMap<>();
    private final Map<Long, Map<String, Object>> userLanguages = new HashMap<>();

    public UserPreferencesServiceImpl() {
        // 初始化默认用户偏好设置
        initDefaultUserPreferences();
    }

    private void initDefaultUserPreferences() {
        // 默认用户偏好设置
        Map<String, Object> defaultPreferences = new HashMap<>();
        defaultPreferences.put("sidebarCollapsed", false);
        defaultPreferences.put("compactMode", false);
        defaultPreferences.put("notifications", true);
        defaultPreferences.put("autoSave", true);
        defaultPreferences.put("pageSize", 20);
        defaultPreferences.put("defaultViewMode", "list");
        defaultPreferences.put("showBreadcrumb", true);
        defaultPreferences.put("showQuickActions", true);
        
        // 默认主题设置
        Map<String, Object> defaultTheme = new HashMap<>();
        defaultTheme.put("primaryColor", "#409EFF");
        defaultTheme.put("darkMode", false);
        defaultTheme.put("compactMode", false);
        defaultTheme.put("sidebarCollapsed", false);
        defaultTheme.put("headerFixed", true);
        defaultTheme.put("footerFixed", false);
        
        // 默认语言设置
        Map<String, Object> defaultLanguage = new HashMap<>();
        defaultLanguage.put("currentLanguage", "zh-CN");
        defaultLanguage.put("autoDetect", true);
        defaultLanguage.put("dateFormat", "yyyy-MM-dd");
        defaultLanguage.put("timeFormat", "HH:mm:ss");
        defaultLanguage.put("numberFormat", "#,##0.00");
        
        // 为默认用户ID设置偏好
        userPreferences.put(1L, new HashMap<>(defaultPreferences));
        userThemes.put(1L, new HashMap<>(defaultTheme));
        userLanguages.put(1L, new HashMap<>(defaultLanguage));
    }

    @Override
    public Map<String, Object> getUserPreferences(Long userId) {
        Map<String, Object> preferences = userPreferences.get(userId);
        if (preferences == null) {
            // 如果用户没有偏好设置，返回默认设置
            preferences = new HashMap<>();
            preferences.put("sidebarCollapsed", false);
            preferences.put("compactMode", false);
            preferences.put("notifications", true);
            preferences.put("autoSave", true);
            preferences.put("pageSize", 20);
            preferences.put("defaultViewMode", "list");
            preferences.put("showBreadcrumb", true);
            preferences.put("showQuickActions", true);
        }
        return new HashMap<>(preferences);
    }

    @Override
    public boolean updateUserPreferences(Long userId, Map<String, Object> preferences) {
        try {
            Map<String, Object> existingPreferences = userPreferences.get(userId);
            if (existingPreferences == null) {
                existingPreferences = new HashMap<>();
            }
            
            // 更新偏好设置
            existingPreferences.putAll(preferences);
            userPreferences.put(userId, existingPreferences);
            
            // TODO: 实际应该保存到数据库
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getUserThemeSettings(Long userId) {
        Map<String, Object> theme = userThemes.get(userId);
        if (theme == null) {
            // 如果用户没有主题设置，返回默认主题
            theme = new HashMap<>();
            theme.put("primaryColor", "#409EFF");
            theme.put("darkMode", false);
            theme.put("compactMode", false);
            theme.put("sidebarCollapsed", false);
            theme.put("headerFixed", true);
            theme.put("footerFixed", false);
        }
        return new HashMap<>(theme);
    }

    @Override
    public boolean updateUserThemeSettings(Long userId, Map<String, Object> theme) {
        try {
            Map<String, Object> existingTheme = userThemes.get(userId);
            if (existingTheme == null) {
                existingTheme = new HashMap<>();
            }
            
            // 更新主题设置
            existingTheme.putAll(theme);
            userThemes.put(userId, existingTheme);
            
            // TODO: 实际应该保存到数据库
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getUserLanguageSettings(Long userId) {
        Map<String, Object> language = userLanguages.get(userId);
        if (language == null) {
            // 如果用户没有语言设置，返回默认语言
            language = new HashMap<>();
            language.put("currentLanguage", "zh-CN");
            language.put("autoDetect", true);
            language.put("dateFormat", "yyyy-MM-dd");
            language.put("timeFormat", "HH:mm:ss");
            language.put("numberFormat", "#,##0.00");
        }
        return new HashMap<>(language);
    }

    @Override
    public boolean updateUserLanguageSettings(Long userId, Map<String, Object> language) {
        try {
            Map<String, Object> existingLanguage = userLanguages.get(userId);
            if (existingLanguage == null) {
                existingLanguage = new HashMap<>();
            }
            
            // 更新语言设置
            existingLanguage.putAll(language);
            userLanguages.put(userId, existingLanguage);
            
            // TODO: 实际应该保存到数据库
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean resetUserPreferences(Long userId) {
        try {
            // 重置为默认设置
            userPreferences.remove(userId);
            userThemes.remove(userId);
            userLanguages.remove(userId);
            
            // TODO: 实际应该从数据库删除
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> exportUserSettings(Long userId) {
        Map<String, Object> exportData = new HashMap<>();
        
        // 导出用户偏好设置
        exportData.put("preferences", getUserPreferences(userId));
        exportData.put("theme", getUserThemeSettings(userId));
        exportData.put("language", getUserLanguageSettings(userId));
        exportData.put("exportTime", new Date());
        exportData.put("version", "1.0");
        
        return exportData;
    }

    @Override
    public boolean importUserSettings(Long userId, Map<String, Object> settings) {
        try {
            // 导入用户偏好设置
            if (settings.containsKey("preferences")) {
                updateUserPreferences(userId, (Map<String, Object>) settings.get("preferences"));
            }
            
            if (settings.containsKey("theme")) {
                updateUserThemeSettings(userId, (Map<String, Object>) settings.get("theme"));
            }
            
            if (settings.containsKey("language")) {
                updateUserLanguageSettings(userId, (Map<String, Object>) settings.get("language"));
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
