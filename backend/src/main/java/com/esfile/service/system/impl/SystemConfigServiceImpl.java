package com.esfile.service.system.impl;

import com.esfile.service.system.SystemConfigService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 系统配置服务实现类
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    // 模拟数据库存储，实际应该从数据库或配置文件读取
    private final Map<String, Object> systemConfigs = new HashMap<>();
    private final Map<String, Object> themeSettings = new HashMap<>();
    private final Map<String, Object> languageSettings = new HashMap<>();

    public SystemConfigServiceImpl() {
        // 初始化默认配置
        initDefaultConfigs();
    }

    private void initDefaultConfigs() {
        // 系统配置
        Map<String, Object> appConfig = new HashMap<>();
        appConfig.put("appName", "ESFile管理系统");
        appConfig.put("version", "1.0.0");
        appConfig.put("description", "企业级文件管理系统");
        appConfig.put("maintainer", "admin@esfile.com");
        appConfig.put("maxFileSize", "100MB");
        appConfig.put("allowedFileTypes", Arrays.asList("jpg", "png", "pdf", "doc", "xls", "txt"));
        
        Map<String, Object> securityConfig = new HashMap<>();
        securityConfig.put("sessionTimeout", 3600);
        securityConfig.put("maxLoginAttempts", 5);
        securityConfig.put("passwordMinLength", 8);
        securityConfig.put("enableCaptcha", true);
        
        systemConfigs.put("app", appConfig);
        systemConfigs.put("security", securityConfig);

        // 主题设置
        themeSettings.put("primaryColor", "#409EFF");
        themeSettings.put("darkMode", false);
        themeSettings.put("compactMode", false);
        themeSettings.put("sidebarCollapsed", false);
        themeSettings.put("headerFixed", true);
        themeSettings.put("footerFixed", false);

        // 语言设置
        languageSettings.put("currentLanguage", "zh-CN");
        languageSettings.put("availableLanguages", Arrays.asList("zh-CN", "en-US"));
        languageSettings.put("defaultLanguage", "zh-CN");
        languageSettings.put("autoDetect", true);
    }

    @Override
    public List<Map<String, Object>> getSystemConfig(String category) {
        if (category == null || category.isEmpty()) {
            // 返回所有配置
            List<Map<String, Object>> allConfigs = new ArrayList<>();
            for (Map.Entry<String, Object> entry : systemConfigs.entrySet()) {
                Map<String, Object> config = new HashMap<>();
                config.put("category", entry.getKey());
                config.put("config", entry.getValue());
                allConfigs.add(config);
            }
            return allConfigs;
        } else {
            // 返回指定分类的配置
            Object config = systemConfigs.get(category);
            if (config != null) {
                List<Map<String, Object>> result = new ArrayList<>();
                Map<String, Object> configItem = new HashMap<>();
                configItem.put("category", category);
                configItem.put("config", config);
                result.add(configItem);
                return result;
            }
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateSystemConfig(Map<String, Object> config) {
        try {
            String category = (String) config.get("category");
            Object configData = config.get("config");
            
            if (category != null && configData != null) {
                systemConfigs.put(category, configData);
                // TODO: 实际应该保存到数据库或配置文件
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean batchUpdateSystemConfig(List<Map<String, Object>> configs) {
        try {
            for (Map<String, Object> config : configs) {
                updateSystemConfig(config);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getThemeSettings() {
        return new HashMap<>(themeSettings);
    }

    @Override
    public boolean updateThemeSettings(Map<String, Object> theme) {
        try {
            themeSettings.putAll(theme);
            // TODO: 实际应该保存到数据库或配置文件
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Object> getLanguageSettings() {
        return new HashMap<>(languageSettings);
    }

    @Override
    public boolean updateLanguageSettings(Map<String, Object> language) {
        try {
            languageSettings.putAll(language);
            // TODO: 实际应该保存到数据库或配置文件
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getAvailableLanguages() {
        List<String> languages = (List<String>) languageSettings.get("availableLanguages");
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (String lang : languages) {
            Map<String, Object> langInfo = new HashMap<>();
            langInfo.put("code", lang);
            langInfo.put("name", getLanguageName(lang));
            langInfo.put("flag", getLanguageFlag(lang));
            result.add(langInfo);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getAvailableThemes() {
        List<Map<String, Object>> themes = new ArrayList<>();
        
        Map<String, Object> lightTheme = new HashMap<>();
        lightTheme.put("name", "light");
        lightTheme.put("label", "浅色主题");
        lightTheme.put("primaryColor", "#409EFF");
        lightTheme.put("backgroundColor", "#ffffff");
        lightTheme.put("textColor", "#303133");
        themes.add(lightTheme);
        
        Map<String, Object> darkTheme = new HashMap<>();
        darkTheme.put("name", "dark");
        darkTheme.put("label", "深色主题");
        darkTheme.put("primaryColor", "#409EFF");
        darkTheme.put("backgroundColor", "#1f1f1f");
        darkTheme.put("textColor", "#ffffff");
        themes.add(darkTheme);
        
        Map<String, Object> blueTheme = new HashMap<>();
        blueTheme.put("name", "blue");
        blueTheme.put("label", "蓝色主题");
        blueTheme.put("primaryColor", "#1890ff");
        blueTheme.put("backgroundColor", "#f0f2f5");
        blueTheme.put("textColor", "#262626");
        themes.add(blueTheme);
        
        return themes;
    }

    @Override
    public boolean resetSystemConfig() {
        try {
            initDefaultConfigs();
            // TODO: 实际应该从数据库或配置文件重新加载
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getLanguageName(String langCode) {
        switch (langCode) {
            case "zh-CN":
                return "简体中文";
            case "en-US":
                return "English";
            default:
                return langCode;
        }
    }

    private String getLanguageFlag(String langCode) {
        switch (langCode) {
            case "zh-CN":
                return "🇨🇳";
            case "en-US":
                return "🇺🇸";
            default:
                return "🌐";
        }
    }
}
