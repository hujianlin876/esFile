package com.esfile.service.database.impl;
import com.esfile.service.database.DatabaseService;
import org.springframework.stereotype.Service;
/**
 * 数据库操作服务实现类
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {
    @Override
    public String executeSql(String sql) {
        // TODO: SQL执行实现
        return "result";
    }
}
