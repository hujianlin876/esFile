# ES文件管理系统 API接口文档

## 概述
本文档列出了ES文件管理系统的所有API接口，包括认证、文件管理、数据库管理、系统管理等功能模块。

## 基础信息
- **基础URL**: `http://localhost:8080/api`
- **认证方式**: JWT Token (Bearer Token)
- **响应格式**: JSON

## 接口分类

### 1. 认证相关接口 (AuthController)
**基础路径**: `/auth`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| POST | `/auth/login` | 用户登录 | 否 |
| POST | `/auth/register` | 用户注册 | 否 |
| POST | `/auth/logout` | 用户登出 | 是 |
| POST | `/auth/refresh` | 刷新Token | 是 |
| GET | `/auth/verify` | 验证Token | 是 |

### 2. 健康检查接口 (HealthController)
**基础路径**: `/health`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/health` | 基础健康检查 | 否 |
| GET | `/health/detailed` | 详细健康检查 | 否 |
| GET | `/health/database` | 数据库健康检查 | 否 |

### 3. 数据库管理接口 (DatabaseController)
**基础路径**: `/database`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| POST | `/database/execute` | 执行SQL | 否 |
| GET | `/database/stats` | 获取数据库状态 | 否 |
| GET | `/database/tables` | 获取表列表 | 否 |
| GET | `/database/tables/{tableName}/structure` | 获取表结构 | 否 |
| GET | `/database/tables/{tableName}/data` | 获取表数据 | 否 |
| POST | `/database/backup` | 备份数据库 | 否 |
| POST | `/database/restore/{backupId}` | 恢复数据库 | 否 |
| GET | `/database/sql-history` | 获取SQL历史记录 | 否 |
| DELETE | `/database/sql-history` | 清理SQL历史记录 | 否 |
| GET | `/database/connection-pool-status` | 获取连接池状态 | 否 |
| POST | `/database/connection-pool-reset` | 重置连接池 | 否 |

### 4. 文件管理接口

#### 4.1 文件上传 (FileUploadController)
**基础路径**: `/api/files`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| POST | `/api/files/upload` | 单文件上传 | 是 |
| POST | `/api/files/batch-upload` | 批量上传文件 | 是 |
| POST | `/api/files/folders` | 创建文件夹 | 是 |
| GET | `/api/files/upload/progress/{taskId}` | 获取上传进度 | 是 |
| POST | `/api/files/upload/cancel/{taskId}` | 取消上传 | 是 |

#### 4.2 文件查询 (FileQueryController)
**基础路径**: `/api/files`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/files` | 获取文件列表 | 是 |
| GET | `/api/files/{id}` | 获取文件详情 | 是 |
| POST | `/api/files/search` | 搜索文件 | 是 |
| GET | `/api/files/stats` | 获取文件统计信息 | 是 |
| GET | `/api/files/stats/types` | 获取文件类型统计 | 是 |
| GET | `/api/files/stats/storage` | 获取存储使用情况 | 是 |
| GET | `/api/files/duplicates` | 查找重复文件 | 是 |
| GET | `/api/files/folders/structure` | 获取文件夹结构 | 是 |

#### 4.3 文件操作 (FileOperationController)
**基础路径**: `/api/files`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/files/{id}/download` | 下载文件 | 是 |
| POST | `/api/files/batch-download` | 批量下载文件 | 是 |
| DELETE | `/api/files/{id}` | 删除文件 | 是 |
| POST | `/api/files/batch-delete` | 批量删除文件 | 是 |
| PUT | `/api/files/{id}` | 更新文件信息 | 是 |
| POST | `/api/files/{id}/move` | 移动文件 | 是 |
| POST | `/api/files/{id}/copy` | 复制文件 | 是 |
| DELETE | `/api/files/folders/{id}` | 删除文件夹 | 是 |
| POST | `/api/files/cleanup` | 清理临时文件 | 是 |

#### 4.4 文件预览和分享 (FileController)
**基础路径**: `/api/files`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/files/{id}/preview` | 获取文件预览 | 是 |
| GET | `/api/files/{id}/content` | 获取文件内容 | 是 |
| GET | `/api/files/{id}/thumbnail` | 获取文件缩略图 | 是 |
| POST | `/api/files/{id}/share` | 分享文件 | 是 |
| DELETE | `/api/files/shares/{shareId}` | 取消文件分享 | 是 |
| GET | `/api/files/shares` | 获取分享的文件列表 | 是 |
| POST | `/api/files/{id}/tags` | 添加文件标签 | 是 |
| DELETE | `/api/files/{id}/tags` | 移除文件标签 | 是 |
| GET | `/api/files/{id}/tags` | 获取文件标签 | 是 |
| GET | `/api/files/by-tag/{tag}` | 根据标签获取文件 | 是 |

### 5. 系统管理接口

#### 5.1 用户管理 (UserController)
**基础路径**: `/api/system/users`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/system/users` | 获取用户列表 | 是 |
| GET | `/api/system/users/{id}` | 获取用户详情 | 是 |
| POST | `/api/system/users` | 创建用户 | 是 |
| PUT | `/api/system/users/{id}` | 更新用户 | 是 |
| DELETE | `/api/system/users/{id}` | 删除用户 | 是 |
| POST | `/api/system/users/batch-delete` | 批量删除用户 | 是 |
| POST | `/api/system/users/{id}/reset-password` | 重置用户密码 | 是 |
| POST | `/api/system/users/{id}/change-password` | 修改用户密码 | 是 |
| POST | `/api/system/users/{id}/toggle-status` | 切换用户状态 | 是 |
| POST | `/api/system/users/batch-update-status` | 批量更新用户状态 | 是 |
| POST | `/api/system/users/{id}/assign-roles` | 分配用户角色 | 是 |
| GET | `/api/system/users/{id}/roles` | 获取用户角色 | 是 |
| POST | `/api/system/users/import` | 导入用户 | 是 |
| GET | `/api/system/users/export` | 导出用户 | 是 |
| GET | `/api/system/users/stats` | 获取用户统计信息 | 是 |

#### 5.2 角色管理 (RoleController)
**基础路径**: `/api/role`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/role` | 查询所有角色 | 是 |
| POST | `/api/role` | 新增角色 | 是 |
| DELETE | `/api/role/{id}` | 删除角色 | 是 |

#### 5.3 权限管理 (PermissionController)
**基础路径**: `/api/system/permissions`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/system/permissions/roles` | 获取角色列表 | 是 |
| GET | `/api/system/permissions` | 获取权限列表 | 是 |
| GET | `/api/system/permissions/tree` | 获取权限树结构 | 是 |
| POST | `/api/system/permissions/roles/{roleId}/permissions` | 分配角色权限 | 是 |
| GET | `/api/system/permissions/roles/{roleId}/permissions` | 获取角色权限 | 是 |

### 6. 仪表板接口 (DashboardController)
**基础路径**: `/api/dashboard`

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/dashboard/stats` | 获取仪表板统计数据 | 是 |
| GET | `/api/dashboard/charts` | 获取图表数据 | 是 |
| GET | `/api/dashboard/activities` | 获取最近活动 | 是 |
| GET | `/api/dashboard/system-health` | 获取系统健康状态 | 是 |
| GET | `/api/dashboard/performance` | 获取性能指标 | 是 |

## 测试状态

### ✅ 已测试通过的接口
1. **认证接口**:
   - POST `/auth/register` - 用户注册 ✅
   - POST `/auth/login` - 用户登录 ✅

2. **健康检查接口**:
   - GET `/health` - 基础健康检查 ✅

3. **数据库管理接口**:
   - POST `/database/execute` - SQL执行模块 ✅

### 🔄 待测试的接口
1. **认证接口**:
   - POST `/auth/logout` - 用户登出
   - POST `/auth/refresh` - 刷新Token
   - GET `/auth/verify` - 验证Token

2. **健康检查接口**:
   - GET `/health/detailed` - 详细健康检查
   - GET `/health/database` - 数据库健康检查

3. **数据库管理接口**:
   - GET `/database/stats` - 获取数据库状态
   - GET `/database/tables` - 获取表列表
   - GET `/database/tables/{tableName}/structure` - 获取表结构
   - GET `/database/tables/{tableName}/data` - 获取表数据
   - POST `/database/backup` - 备份数据库
   - POST `/database/restore/{backupId}` - 恢复数据库
   - GET `/database/sql-history` - 获取SQL历史记录
   - DELETE `/database/sql-history` - 清理SQL历史记录
   - GET `/database/connection-pool-status` - 获取连接池状态
   - POST `/database/connection-pool-reset` - 重置连接池

4. **文件管理接口** (所有文件相关接口)

5. **系统管理接口** (所有系统管理相关接口)

6. **仪表板接口** (所有仪表板相关接口)

## 注意事项
1. 所有需要认证的接口都需要在请求头中包含有效的JWT Token
2. 文件上传接口支持multipart/form-data格式
3. 分页接口默认页码为1，每页大小为20
4. 所有接口都返回统一的ResponseResult格式
5. 错误信息会包含在响应的message字段中

## 下一步计划
1. 逐个测试所有接口
2. 修复发现的问题
3. 完善接口文档
4. 添加接口测试用例

