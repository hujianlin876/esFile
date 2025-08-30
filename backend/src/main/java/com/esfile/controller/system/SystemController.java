package com.esfile.controller.system;
import org.springframework.web.bind.annotation.*;
/**
 * 系统控制器
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {
    @Autowired
    private com.esfile.service.system.SystemService systemService;

    /**
     * 获取系统配置
     */
    @GetMapping("/config")
    public Object getConfig() {
        return systemService.getSystemConfig();
    }

    /**
     * 更新系统配置
     */
    @PostMapping("/config")
    public boolean updateConfig(@RequestBody com.esfile.entity.mybatis.SystemConfig config) {
        return systemService.updateSystemConfig(config);
    }
}
