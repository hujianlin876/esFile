package com.esfile.controller.system;

import com.esfile.common.vo.ResponseResult;
import com.esfile.service.system.SystemConfigService;
import com.esfile.service.system.UserPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统设置控制器
 */
@RestController
@RequestMapping("/api/system/settings")
public class SettingsController {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private UserPreferencesService userPreferencesService;

    /**
     * 获取系统配置
     */
    @GetMapping("/config")
    public ResponseResult<List<Map<String, Object>>> getSystemConfig(
            @RequestParam(required = false) String category) {
        try {
            List<Map<String, Object>> configs = systemConfigService.getSystemConfig(category);
            return ResponseResult.success(configs);
        } catch (Exception e) {
            return ResponseResult.fail("获取系统配置失败: " + e.getMessage());
        }
    }

    /**
     * 更新系统配置
     */
    @PutMapping("/config")
    public ResponseResult<String> updateSystemConfig(@RequestBody Map<String, Object> config) {
        try {
            boolean success = systemConfigService.updateSystemConfig(config);
            if (success) {
                return ResponseResult.success("系统配置更新成功");
            } else {
                return ResponseResult.fail("系统配置更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("更新系统配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取主题设置
     */
    @GetMapping("/theme")
    public ResponseResult<Map<String, Object>> getThemeSettings() {
        try {
            Map<String, Object> theme = systemConfigService.getThemeSettings();
            return ResponseResult.success(theme);
        } catch (Exception e) {
            return ResponseResult.fail("获取主题设置失败: " + e.getMessage());
        }
    }

    /**
     * 更新主题设置
     */
    @PutMapping("/theme")
    public ResponseResult<String> updateThemeSettings(@RequestBody Map<String, Object> theme) {
        try {
            boolean success = systemConfigService.updateThemeSettings(theme);
            if (success) {
                return ResponseResult.success("主题设置更新成功");
            } else {
                return ResponseResult.fail("主题设置更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("更新主题设置失败: " + e.getMessage());
        }
    }

    /**
     * 获取语言设置
     */
    @GetMapping("/language")
    public ResponseResult<Map<String, Object>> getLanguageSettings() {
        try {
            Map<String, Object> language = systemConfigService.getLanguageSettings();
            return ResponseResult.success(language);
        } catch (Exception e) {
            return ResponseResult.fail("获取语言设置失败: " + e.getMessage());
        }
    }

    /**
     * 更新语言设置
     */
    @PutMapping("/language")
    public ResponseResult<String> updateLanguageSettings(@RequestBody Map<String, Object> language) {
        try {
            boolean success = systemConfigService.updateLanguageSettings(language);
            if (success) {
                return ResponseResult.success("语言设置更新成功");
            } else {
                return ResponseResult.fail("语言设置更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("更新语言设置失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户偏好设置
     */
    @GetMapping("/preferences")
    public ResponseResult<Map<String, Object>> getUserPreferences() {
        try {
            // TODO: 从SecurityContext获取当前用户ID
            Long userId = 1L; // 临时写死，实际应该从认证信息获取
            Map<String, Object> preferences = userPreferencesService.getUserPreferences(userId);
            return ResponseResult.success(preferences);
        } catch (Exception e) {
            return ResponseResult.fail("获取用户偏好设置失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户偏好设置
     */
    @PutMapping("/preferences")
    public ResponseResult<String> updateUserPreferences(@RequestBody Map<String, Object> preferences) {
        try {
            // TODO: 从SecurityContext获取当前用户ID
            Long userId = 1L; // 临时写死，实际应该从认证信息获取
            boolean success = userPreferencesService.updateUserPreferences(userId, preferences);
            if (success) {
                return ResponseResult.success("用户偏好设置更新成功");
            } else {
                return ResponseResult.fail("用户偏好设置更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.fail("更新用户偏好设置失败: " + e.getMessage());
        }
    }
}
