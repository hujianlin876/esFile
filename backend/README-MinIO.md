# MinIO存储集成说明

## 概述

ES文件管理系统已成功集成MinIO对象存储服务，提供高效、可靠的文件存储解决方案。

## 配置说明

### 1. MinIO服务器配置

在`application.yml`中配置MinIO连接信息：

```yaml
# MinIO配置
minio:
  endpoint: http://139.224.67.165:11004  # MinIO服务器地址
  access-key: hujianlin                   # 访问密钥
  secret-key: hujianlin                   # 秘密密钥
  bucket: elasticsearch                   # 存储桶名称
  secure: false                           # 是否启用HTTPS
  connect-timeout: 10000                  # 连接超时时间（毫秒）
  read-timeout: 60000                     # 读取超时时间（毫秒）
  write-timeout: 60000                    # 写入超时时间（毫秒）
```

### 2. 文件上传配置

```yaml
# 文件上传配置
file:
  upload:
    max-size: 104857600                   # 最大文件大小（100MB）
    chunk-size: 5242880                   # 分片大小（5MB）
    temp-dir: /tmp/upload                 # 临时目录
    allowed-types: jpg,jpeg,png,gif,bmp,pdf,doc,docx,xls,xlsx,ppt,pptx,txt,zip,rar,7z,mp4,avi,mov,mp3,wav
    chunk-concurrency: 3                  # 分片上传并发数
```

### 3. 存储策略配置

```yaml
# 存储策略配置
storage:
  path-template: "files/{userId}/{year}/{month}/{day}/{filename}"  # 存储路径模板
  deduplication: true                    # 启用文件去重
  compression: false                     # 启用文件压缩
  encryption: false                      # 启用文件加密
  retention-days: 3650                   # 文件保留策略（10年）
  versioning: true                       # 启用版本控制
  max-versions: 5                        # 最大版本数
```

## 核心组件

### 1. MinioStorageService接口

定义了完整的文件存储操作：

- `uploadFile()` - 上传文件
- `downloadFile()` - 下载文件
- `deleteFile()` - 删除文件
- `copyFile()` - 复制文件
- `moveFile()` - 移动文件
- `getPresignedUrl()` - 获取预签名URL

### 2. MinioStorageServiceImpl实现类

实现了MinIO存储服务的所有功能，包括：

- 自动桶管理
- 文件路径生成
- 错误处理和日志记录
- 大文件支持

### 3. MinioStorageController控制器

提供REST API接口：

- `POST /api/storage/upload` - 文件上传
- `GET /api/storage/download` - 文件下载
- `DELETE /api/storage/delete` - 文件删除
- `POST /api/storage/copy` - 文件复制
- `POST /api/storage/move` - 文件移动

### 4. MinioStorageUtil工具类

提供文件处理相关的工具方法：

- 文件类型验证
- 文件名生成
- MD5计算
- 文件大小格式化

## 使用方法

### 1. 文件上传

```java
@Autowired
private MinioStorageService minioStorageService;

// 上传文件
String objectName = "files/user123/2024/01/15/document.pdf";
String fileUrl = minioStorageService.uploadFile(multipartFile, objectName);
```

### 2. 文件下载

```java
// 下载文件
InputStream inputStream = minioStorageService.downloadFile(objectName);

// 获取预签名下载URL
String downloadUrl = minioStorageService.getPresignedDownloadUrl(objectName, 60);
```

### 3. 文件管理

```java
// 删除文件
boolean deleted = minioStorageService.deleteFile(objectName);

// 复制文件
boolean copied = minioStorageService.copyFile(sourceObjectName, targetObjectName);

// 移动文件
boolean moved = minioStorageService.moveFile(sourceObjectName, targetObjectName);
```

## 文件存储结构

```
elasticsearch/                    # 存储桶
├── files/                       # 文件根目录
│   ├── 123/                     # 用户ID
│   │   ├── 2024/               # 年份
│   │   │   ├── 01/             # 月份
│   │   │   │   ├── 15/         # 日期
│   │   │   │   │   ├── document_1234567890.pdf
│   │   │   │   │   └── image_1234567890.jpg
│   │   │   └── 16/
│   │   │       └── video_1234567890.mp4
│   └── anonymous/               # 匿名用户
│       └── 2024/01/15/
│           └── temp_1234567890.txt
```

## 特性说明

### 1. 自动桶管理

- 启动时自动检查桶是否存在
- 不存在时自动创建
- 设置合适的桶策略

### 2. 文件路径管理

- 按用户ID和日期组织文件结构
- 支持自定义路径
- 避免文件名冲突

### 3. 高级功能

- 预签名URL（支持直接上传/下载）
- 文件复制和移动
- 批量操作支持
- 完整的文件元数据管理

### 4. 错误处理和监控

- 详细的日志记录
- 异常处理和恢复
- 连接状态监控

## 安全特性

### 1. 访问控制

- 基于JWT的身份验证
- 用户权限验证
- 文件访问权限控制

### 2. 文件安全

- 文件类型验证
- 文件大小限制
- 恶意文件检测

### 3. 传输安全

- 支持HTTPS
- 预签名URL过期机制
- 防盗链保护

## 性能优化

### 1. 分片上传

- 支持大文件分片上传
- 可配置分片大小
- 并发上传支持

### 2. 缓存策略

- 文件元数据缓存
- 缩略图缓存
- 可配置缓存时间

### 3. 连接池

- MinIO连接池管理
- 超时配置
- 重试机制

## 监控和日志

### 1. 系统监控

- 存储使用统计
- 文件操作统计
- 性能指标监控

### 2. 访问日志

- 文件上传日志
- 文件下载日志
- 操作审计日志

### 3. 错误日志

- 连接异常日志
- 操作失败日志
- 系统错误日志

## 故障排除

### 1. 连接问题

- 检查MinIO服务器状态
- 验证网络连接
- 检查认证信息

### 2. 权限问题

- 验证桶访问权限
- 检查用户权限
- 确认文件权限

### 3. 性能问题

- 调整分片大小
- 优化并发数
- 检查网络带宽

## 扩展功能

### 1. 文件版本控制

- 支持文件版本管理
- 可配置版本数量
- 版本回滚功能

### 2. 文件生命周期管理

- 自动文件清理
- 存储策略配置
- 归档和删除

### 3. 多存储后端

- 支持多个MinIO实例
- 负载均衡
- 故障转移

## 总结

MinIO存储集成为ES文件管理系统提供了：

- **高性能**：支持大文件、分片上传、并发操作
- **高可靠**：自动桶管理、错误处理、重试机制
- **高安全**：身份验证、权限控制、传输加密
- **易扩展**：模块化设计、配置化、插件化架构

通过MinIO存储集成，系统现在具备了企业级的文件存储能力，可以满足各种文件管理需求。
