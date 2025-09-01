# ES文件管理系统部署指南

## 环境变量配置

为了提高安全性，本项目使用环境变量来管理敏感配置信息。请按照以下步骤进行配置：

### 1. 复制环境变量模板

```bash
cp env-example.txt .env
```

### 2. 编辑环境变量文件

根据您的实际环境修改 `.env` 文件中的配置：

```bash
# 编辑环境变量
vim .env
```

### 3. 环境变量说明

#### 数据库配置
- `DB_URL`: 数据库连接URL
- `DB_USERNAME`: 数据库用户名
- `DB_PASSWORD`: 数据库密码

#### Redis配置
- `REDIS_HOST`: Redis服务器地址
- `REDIS_PORT`: Redis端口
- `REDIS_PASSWORD`: Redis密码

#### Elasticsearch配置
- `ES_CLUSTER_NAME`: ES集群名称
- `ES_CLUSTER_NODES`: ES节点地址
- `ES_USERNAME`: ES用户名
- `ES_PASSWORD`: ES密码

#### MinIO配置
- `MINIO_ENDPOINT`: MinIO服务地址
- `MINIO_ACCESS_KEY`: MinIO访问密钥
- `MINIO_SECRET_KEY`: MinIO秘密密钥
- `MINIO_BUCKET`: 存储桶名称

#### JWT配置
- `JWT_SECRET`: JWT签名密钥（至少32位字符）
- `JWT_EXPIRATION`: Token过期时间（毫秒）

### 4. 运行环境选择

#### 开发环境
```bash
java -jar target/esfile-backend-1.0.0.jar --spring.profiles.active=dev
```

#### 测试环境
```bash
java -jar target/esfile-backend-1.0.0.jar --spring.profiles.active=test
```

#### 生产环境
```bash
java -jar target/esfile-backend-1.0.0.jar --spring.profiles.active=prod
```

### 5. Docker部署

#### 使用Docker Compose

创建 `docker-compose.yml` 文件：

```yaml
version: '3.8'
services:
  esfile-backend:
    image: esfile-backend:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=${DB_URL}
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
      - REDIS_HOST=${REDIS_HOST}
      - REDIS_PORT=${REDIS_PORT}
      - REDIS_PASSWORD=${REDIS_PASSWORD}
      - ES_CLUSTER_NODES=${ES_CLUSTER_NODES}
      - ES_USERNAME=${ES_USERNAME}
      - ES_PASSWORD=${ES_PASSWORD}
      - MINIO_ENDPOINT=${MINIO_ENDPOINT}
      - MINIO_ACCESS_KEY=${MINIO_ACCESS_KEY}
      - MINIO_SECRET_KEY=${MINIO_SECRET_KEY}
      - JWT_SECRET=${JWT_SECRET}
    volumes:
      - ./logs:/app/logs
      - ./uploads:/tmp/upload
```

#### 启动服务

```bash
docker-compose up -d
```

### 6. 安全建议

1. **生产环境密码**: 在生产环境中务必更改所有默认密码
2. **JWT密钥**: 使用强密码生成器生成JWT密钥
3. **文件权限**: 确保 `.env` 文件只有应用程序用户可读
4. **网络安全**: 配置防火墙规则，仅开放必要端口
5. **SSL/TLS**: 在生产环境中启用HTTPS

### 7. 故障排除

#### 常见问题

1. **数据库连接失败**
   - 检查数据库服务是否启动
   - 验证连接配置和凭据
   - 确保防火墙允许连接

2. **Redis连接失败**
   - 检查Redis服务状态
   - 验证认证配置
   - 检查网络连通性

3. **MinIO连接失败**
   - 确认MinIO服务运行状态
   - 验证访问密钥配置
   - 检查存储桶是否存在

#### 日志查看

```bash
# 查看应用日志
tail -f logs/esfile-backend.log

# 查看错误日志
tail -f logs/esfile-backend-error.log
```

### 8. 监控和维护

- 定期备份数据库
- 监控磁盘空间使用情况
- 检查日志文件大小
- 更新依赖和安全补丁

## 联系支持

如有问题，请查看项目文档或联系技术支持。

