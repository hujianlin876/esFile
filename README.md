# ES文件管理系统

基于Spring Boot + Vue3的现代化文件管理系统，集成Elasticsearch、MinIO、Redis、MySQL等组件，提供完整的文件管理、权限控制和在线预览功能。
严格按照规范来  代码太多要做拆分 不要一个文件搞的很大
## 项目结构

```
esfile/
├── backend/                 # 后端项目 (Spring Boot)
├── frontend/                # 前端项目 (Vue 3)
├── docs/                    # 项目文档
└── docker/                  # Docker配置
```

## 技术架构

### 后端技术栈
- Spring Boot 2.7.18 + Java 8
- MyBatis 3.5+ + MySQL 8.0
- Redis 6.0+ + Elasticsearch 7.17.15
- MinIO 8.5.2 + Spring Security + JWT

### 前端技术栈
- Vue 3.3+ + TypeScript 5.0+
- Element Plus 2.3+ + Vite 4.0+
- Pinia 2.1+ + Vue Router 4.2+

## 快速开始

### 1. 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

### 2. 启动前端服务
```bash
cd frontend
npm install
npm run dev
```

### 3. 访问系统
- 前端: http://localhost:3000
- 后端: http://localhost:8080

## 功能特性

- 🔐 完整的RBAC权限控制系统
- 📁 强大的文件管理系统
- 🔍 基于ES的智能搜索
- 👀 多格式文件在线预览
- 💾 数据库操作模块
- 🚀 高性能架构设计

## 开发规范

- 严格遵守阿里Java开发手册
- 代码行数不超过400行
- 前端CSS与JS代码分离
- 完整的异常处理和日志记录

## 许可证

MIT License

