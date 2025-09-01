# ES File Management System 数据库表结构说明

## 数据库信息
- **数据库名**: esfile
- **字符集**: utf8mb4
- **排序规则**: utf8mb4_unicode_ci
- **存储引擎**: InnoDB

## 表结构总览

### 1. 用户权限相关表

#### 1.1 sys_user (用户表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 用户ID，主键，自增 |
| username | VARCHAR(50) | 用户名，唯一 |
| password | VARCHAR(255) | 密码（加密存储） |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机号 |
| real_name | VARCHAR(50) | 真实姓名 |
| avatar | VARCHAR(255) | 头像URL |
| status | TINYINT | 状态：0-禁用，1-启用 |
| last_login_time | TIMESTAMP | 最后登录时间 |
| last_login_ip | VARCHAR(50) | 最后登录IP |
| login_fail_count | INT | 登录失败次数 |
| account_locked | TINYINT | 账户是否锁定：0-否，1-是 |
| lock_time | TIMESTAMP | 锁定时间 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

#### 1.2 sys_role (角色表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 角色ID，主键，自增 |
| role_name | VARCHAR(50) | 角色名称 |
| role_code | VARCHAR(50) | 角色编码，唯一 |
| description | VARCHAR(255) | 角色描述 |
| status | TINYINT | 状态：0-禁用，1-启用 |
| sort | INT | 排序 |
| is_system | TINYINT | 是否系统角色：0-否，1-是 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

#### 1.3 sys_permission (权限表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 权限ID，主键，自增 |
| permission_name | VARCHAR(50) | 权限名称 |
| permission_code | VARCHAR(50) | 权限编码，唯一 |
| permission_type | TINYINT | 权限类型：1-菜单，2-按钮，3-接口 |
| permission_path | VARCHAR(255) | 权限路径 |
| description | VARCHAR(255) | 权限描述 |
| parent_id | BIGINT | 父级权限ID |
| sort | INT | 排序 |
| status | TINYINT | 状态：0-禁用，1-启用 |
| icon | VARCHAR(100) | 图标 |
| component | VARCHAR(255) | 组件路径 |
| is_external | TINYINT | 是否外链：0-否，1-是 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

#### 1.4 sys_menu (菜单表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 菜单ID，主键，自增 |
| name | VARCHAR(50) | 菜单名称 |
| path | VARCHAR(255) | 菜单路径 |
| component | VARCHAR(255) | 组件路径 |
| icon | VARCHAR(100) | 菜单图标 |
| parent_id | BIGINT | 父菜单ID |
| sort | INT | 排序 |
| status | TINYINT | 状态：0-禁用，1-启用 |
| type | TINYINT | 菜单类型：1-目录，2-菜单，3-按钮 |
| permission | VARCHAR(100) | 权限标识 |
| is_frame | TINYINT | 是否外链：0-否，1-是 |
| is_cache | TINYINT | 是否缓存：0-否，1-是 |
| visible | TINYINT | 是否可见：0-否，1-是 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| create_by | VARCHAR(50) | 创建者 |
| update_by | VARCHAR(50) | 更新者 |
| remark | VARCHAR(500) | 备注 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

#### 1.5 sys_user_role (用户角色关联表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 关联ID，主键，自增 |
| user_id | BIGINT | 用户ID |
| role_id | BIGINT | 角色ID |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

#### 1.6 sys_role_permission (角色权限关联表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 关联ID，主键，自增 |
| role_id | BIGINT | 角色ID |
| permission_id | BIGINT | 权限ID |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

### 2. 文件管理相关表

#### 2.1 file_info (文件信息表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 文件ID，主键，自增 |
| file_name | VARCHAR(255) | 文件名 |
| original_file_name | VARCHAR(255) | 原始文件名 |
| file_path | VARCHAR(500) | 文件路径 |
| file_url | VARCHAR(500) | 文件URL |
| file_size | BIGINT | 文件大小（字节） |
| file_type | VARCHAR(100) | 文件类型 |
| file_extension | VARCHAR(50) | 文件扩展名 |
| file_md5 | VARCHAR(32) | 文件MD5值 |
| upload_user_id | BIGINT | 上传用户ID |
| upload_user_name | VARCHAR(50) | 上传用户名 |
| status | TINYINT | 文件状态：0-删除，1-正常 |
| is_public | TINYINT | 是否公开：0-私有，1-公开 |
| description | TEXT | 文件描述 |
| tags | VARCHAR(500) | 标签 |
| download_count | INT | 下载次数 |
| preview_count | INT | 预览次数 |
| bucket_name | VARCHAR(100) | MinIO存储桶名称 |
| object_name | VARCHAR(500) | MinIO对象名称 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

### 3. 系统配置相关表

#### 3.1 sys_config (系统配置表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 配置ID，主键，自增 |
| config_key | VARCHAR(100) | 配置键，唯一 |
| config_value | TEXT | 配置值 |
| description | VARCHAR(255) | 配置描述 |
| config_type | TINYINT | 配置类型：1-系统，2-业务，3-用户 |
| is_editable | TINYINT | 是否可编辑：0-否，1-是 |
| sort | INT | 排序 |
| create_time | TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | 更新时间 |
| deleted | TINYINT | 是否删除：0-否，1-是 |

### 4. 日志相关表

#### 4.1 sys_operation_log (操作日志表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 日志ID，主键，自增 |
| user_id | BIGINT | 操作用户ID |
| username | VARCHAR(50) | 操作用户名 |
| operation | VARCHAR(100) | 操作类型 |
| method | VARCHAR(255) | 请求方法 |
| request_uri | VARCHAR(500) | 请求URI |
| request_method | VARCHAR(10) | 请求方法 |
| request_params | TEXT | 请求参数 |
| response_result | TEXT | 响应结果 |
| ip_address | VARCHAR(50) | IP地址 |
| user_agent | VARCHAR(500) | 用户代理 |
| status | TINYINT | 操作状态：0-失败，1-成功 |
| error_message | TEXT | 错误信息 |
| execution_time | BIGINT | 执行时间（毫秒） |
| create_time | TIMESTAMP | 创建时间 |

#### 4.2 sys_sql_log (SQL执行日志表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 日志ID，主键，自增 |
| user_id | BIGINT | 执行用户ID |
| username | VARCHAR(50) | 执行用户名 |
| sql_statement | TEXT | SQL语句 |
| sql_type | VARCHAR(20) | SQL类型：SELECT, INSERT, UPDATE, DELETE, DDL |
| execution_time | BIGINT | 执行时间（毫秒） |
| affected_rows | INT | 影响行数 |
| result_count | INT | 结果行数 |
| status | TINYINT | 执行状态：0-失败，1-成功 |
| error_message | TEXT | 错误信息 |
| ip_address | VARCHAR(50) | IP地址 |
| create_time | TIMESTAMP | 创建时间 |

#### 4.3 sys_file_log (文件操作日志表)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 日志ID，主键，自增 |
| user_id | BIGINT | 操作用户ID |
| username | VARCHAR(50) | 操作用户名 |
| file_id | BIGINT | 文件ID |
| file_name | VARCHAR(255) | 文件名 |
| operation | VARCHAR(50) | 操作类型：UPLOAD, DOWNLOAD, PREVIEW, DELETE |
| operation_result | TINYINT | 操作结果：0-失败，1-成功 |
| error_message | TEXT | 错误信息 |
| ip_address | VARCHAR(50) | IP地址 |
| user_agent | VARCHAR(500) | 用户代理 |
| create_time | TIMESTAMP | 创建时间 |

## 索引设计

### 主要索引
1. **用户表索引**
   - `idx_username` - 用户名索引
   - `idx_email` - 邮箱索引
   - `idx_status` - 状态索引

2. **角色表索引**
   - `idx_role_code` - 角色编码索引
   - `idx_status` - 状态索引

3. **权限表索引**
   - `idx_permission_code` - 权限编码索引
   - `idx_parent_id` - 父级权限索引
   - `idx_permission_type` - 权限类型索引

4. **菜单表索引**
   - `idx_parent_id` - 父菜单索引
   - `idx_status` - 状态索引
   - `idx_type` - 菜单类型索引

5. **文件表索引**
   - `idx_file_name` - 文件名索引
   - `idx_file_type` - 文件类型索引
   - `idx_file_md5` - 文件MD5索引
   - `idx_upload_user_id` - 上传用户索引

6. **日志表索引**
   - `idx_user_id` - 用户ID索引
   - `idx_create_time` - 创建时间索引
   - `idx_operation` - 操作类型索引

## 初始数据

### 默认用户
- **超级管理员**: admin / admin123

### 默认角色
- **超级管理员**: 拥有所有权限
- **系统管理员**: 负责系统管理
- **普通用户**: 基础功能权限

### 默认权限
- 系统管理模块权限
- 文件管理模块权限
- 数据库操作模块权限

### 默认菜单
- 系统管理菜单树
- 文件管理菜单树
- 数据库操作菜单树

### 系统配置
- 系统基本信息配置
- 文件上传配置
- 安全配置

## 使用说明

### 1. 数据库初始化
```sql
-- 执行初始化脚本
source /path/to/init.sql;
```

### 2. 数据库备份
```bash
mysqldump -u username -p esfile > esfile_backup.sql
```

### 3. 数据库恢复
```bash
mysql -u username -p esfile < esfile_backup.sql
```

### 4. 表结构查看
```sql
-- 查看所有表
SHOW TABLES;

-- 查看表结构
DESCRIBE table_name;

-- 查看建表语句
SHOW CREATE TABLE table_name;
```

## 注意事项

1. **字符集**: 所有表使用utf8mb4字符集，支持emoji等特殊字符
2. **软删除**: 所有业务表都支持软删除，通过deleted字段标记
3. **时间字段**: 统一使用TIMESTAMP类型，自动更新
4. **索引优化**: 根据查询需求合理设计索引
5. **数据备份**: 定期备份数据库，确保数据安全
