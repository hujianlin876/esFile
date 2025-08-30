package com.esfile.controller.database;
import com.esfile.entity.dto.SqlExecuteDto;
import org.springframework.web.bind.annotation.*;
/**
 * 数据库操作控制器
 */
@RestController
@RequestMapping("/api/database")
public class DatabaseController {
    @Autowired
    private com.esfile.service.database.DatabaseService databaseService;

    /**
     * 执行SQL
     */
    @PostMapping("/execute")
    public Object execute(@RequestBody SqlExecuteDto sqlExecuteDto) {
        return databaseService.executeSql(sqlExecuteDto);
    }
}
