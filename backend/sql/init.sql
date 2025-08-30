-- ES File Management System Database Initialization Script
-- 数据库: esfile
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_unicode_ci

-- 创建数据库
CREATE DATABASE IF NOT EXISTS esfile DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE esfile;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(255) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    sort INT DEFAULT 0 COMMENT '排序',
    is_system TINYINT DEFAULT 0 COMMENT '是否系统角色：0-否，1-是',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    INDEX idx_role_code (role_code),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(50) NOT NULL UNIQUE COMMENT '权限编码',
    permission_type TINYINT NOT NULL COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    permission_path VARCHAR(255) COMMENT '权限路径',
    description VARCHAR(255) COMMENT '权限描述',
    parent_id BIGINT DEFAULT 0 COMMENT '父级权限ID',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    icon VARCHAR(100) COMMENT '图标',
    component VARCHAR(255) COMMENT '组件路径',
    is_external TINYINT DEFAULT 0 COMMENT '是否外链：0-否，1-是',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    INDEX idx_permission_code (permission_code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_permission_type (permission_type),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 文件信息表
CREATE TABLE IF NOT EXISTS file_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文件ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    original_file_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_path VARCHAR(500) COMMENT '文件路径',
    file_url VARCHAR(500) COMMENT '文件URL',
    file_size BIGINT NOT NULL COMMENT '文件大小（字节）',
    file_type VARCHAR(100) COMMENT '文件类型',
    file_extension VARCHAR(50) COMMENT '文件扩展名',
    file_md5 VARCHAR(32) NOT NULL COMMENT '文件MD5值',
    upload_user_id BIGINT NOT NULL COMMENT '上传用户ID',
    upload_user_name VARCHAR(50) COMMENT '上传用户名',
    status TINYINT DEFAULT 1 COMMENT '文件状态：0-删除，1-正常',
    is_public TINYINT DEFAULT 0 COMMENT '是否公开：0-私有，1-公开',
    description TEXT COMMENT '文件描述',
    tags VARCHAR(500) COMMENT '标签',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    preview_count INT DEFAULT 0 COMMENT '预览次数',
    bucket_name VARCHAR(100) COMMENT 'MinIO存储桶名称',
    object_name VARCHAR(500) COMMENT 'MinIO对象名称',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    INDEX idx_file_name (file_name),
    INDEX idx_file_type (file_type),
    INDEX idx_file_md5 (file_md5),
    INDEX idx_upload_user_id (upload_user_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS sys_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    description VARCHAR(255) COMMENT '配置描述',
    config_type TINYINT DEFAULT 1 COMMENT '配置类型：1-系统，2-业务，3-用户',
    is_editable TINYINT DEFAULT 1 COMMENT '是否可编辑：0-否，1-是',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    INDEX idx_config_key (config_key),
    INDEX idx_config_type (config_type),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 插入初始数据

-- 插入超级管理员用户（密码：admin123）
INSERT INTO sys_user (username, password, email, real_name, status) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@esfile.com', '超级管理员', 1);

-- 插入系统角色
INSERT INTO sys_role (role_name, role_code, description, status, sort, is_system) VALUES 
('超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', 1, 1, 1),
('系统管理员', 'SYSTEM_ADMIN', '系统管理员，负责系统管理', 1, 2, 1),
('普通用户', 'NORMAL_USER', '普通用户，基础功能权限', 1, 3, 1);

-- 插入系统权限
INSERT INTO sys_permission (permission_name, permission_code, permission_type, permission_path, description, parent_id, sort, status) VALUES 
-- 系统管理
('系统管理', 'SYSTEM_MANAGE', 1, '/system', '系统管理模块', 0, 1, 1),
('用户管理', 'USER_MANAGE', 1, '/system/user', '用户管理', 1, 1, 1),
('角色管理', 'ROLE_MANAGE', 1, '/system/role', '角色管理', 1, 2, 1),
('权限管理', 'PERMISSION_MANAGE', 1, '/system/permission', '权限管理', 1, 3, 1),
('系统配置', 'SYSTEM_CONFIG', 1, '/system/config', '系统配置', 1, 4, 1),

-- 文件管理
('文件管理', 'FILE_MANAGE', 1, '/file', '文件管理模块', 0, 2, 1),
('文件上传', 'FILE_UPLOAD', 1, '/file/upload', '文件上传', 6, 1, 1),
('文件下载', 'FILE_DOWNLOAD', 1, '/file/download', '文件下载', 6, 2, 1),
('文件预览', 'FILE_PREVIEW', 1, '/file/preview', '文件预览', 6, 3, 1),
('文件搜索', 'FILE_SEARCH', 1, '/file/search', '文件搜索', 6, 4, 1),

-- 数据库操作
('数据库操作', 'DATABASE_OPERATION', 1, '/database', '数据库操作模块', 0, 3, 1),
('SQL执行', 'SQL_EXECUTE', 1, '/database/sql', 'SQL执行', 11, 1, 1);

-- 插入用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 插入角色权限关联（超级管理员拥有所有权限）
INSERT INTO sys_role_permission (role_id, permission_id) 
SELECT 1, id FROM sys_permission WHERE deleted = 0;

-- 插入系统配置
INSERT INTO sys_config (config_key, config_value, description, config_type) VALUES 
('SYSTEM_NAME', 'ES File Management System', '系统名称', 1),
('SYSTEM_VERSION', '1.0.0', '系统版本', 1),
('FILE_UPLOAD_MAX_SIZE', '1073741824', '文件上传最大大小（字节）', 2),
('FILE_ALLOWED_TYPES', 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,ppt,pptx,txt,zip,rar', '允许上传的文件类型', 2),
('MINIO_BUCKET_NAME', 'elasticsearch', 'MinIO存储桶名称', 1),
('ELASTICSEARCH_INDEX_PREFIX', 'esfile', 'Elasticsearch索引前缀', 1);
