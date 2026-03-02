# 博客系统部署指南

## 目录

1. [环境要求](#环境要求)
2. [快速部署](#快速部署)
3. [开发环境配置](#开发环境配置)
4. [生产环境部署](#生产环境部署)
5. [配置说明](#配置说明)
6. [常见问题](#常见问题)

---

## 环境要求

### 开发环境

| 软件 | 版本要求 | 说明 |
|------|---------|------|
| Node.js | 18+ | 前端开发 |
| Java JDK | 21 | 后端开发 |
| Maven | 3.9+ | Java构建工具 |
| Docker | 24+ | 容器化部署 |
| Docker Compose | 2+ | 容器编排 |
| Git | 2.x | 版本控制 |

### 服务器要求（生产环境）

| 配置项 | 最低要求 | 推荐配置 |
|--------|---------|---------|
| CPU | 2核 | 4核 |
| 内存 | 2GB | 4GB |
| 磁盘 | 20GB | 50GB SSD |
| 系统 | Ubuntu 20.04+ | Ubuntu 22.04 LTS |

---

## 快速部署

### 一键部署（生产环境）

```bash
# 1. 克隆项目
git clone https://github.com/WangWinston/blog-project.git
cd blog-project

# 2. 创建环境配置
cp .env.example .env
# 编辑 .env 文件，修改必要的配置

# 3. 启动所有服务
docker compose up -d

# 4. 查看服务状态
docker compose ps
```

### 访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| 前端门户 | http://localhost | 博客首页 |
| 后台管理 | http://localhost/admin | 管理后台 |
| API文档 | http://localhost/api/doc.html | Knife4j文档 |

### 默认账号

- 用户名: `admin`
- 密码: `admin123`

> **重要**: 首次登录后请立即修改密码！

---

## 开发环境配置

### 1. 启动基础设施

```bash
# 启动 MySQL 和 Redis
docker compose -f docker-compose.infra.yml up -d

# 查看服务状态
docker compose -f docker-compose.infra.yml ps
```

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env
```

编辑 `.env` 文件：

```env
# MySQL 配置
MYSQL_ROOT_PASSWORD=root123
MYSQL_PASSWORD=blog123

# JWT 配置（生产环境请修改）
JWT_SECRET=your-secret-key-at-least-256-bits-long-for-hs256
JWT_EXPIRATION=7200000

# GitHub OAuth（可选）
GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=
```

### 3. 启动后端服务

```bash
cd blog-backend

# 使用 Maven Wrapper 启动（推荐）
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 或使用系统 Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

后端启动成功后访问：
- API服务: http://localhost:8080
- API文档: http://localhost:8080/doc.html
- 健康检查: http://localhost:8080/actuator/health

### 4. 启动前端服务

```bash
cd blog-web

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端启动成功后访问：
- 前端门户: http://localhost:3000
- 后台管理: http://localhost:3000/admin

---

## 生产环境部署

### 部署架构

```
┌─────────────────────────────────────────────────────────┐
│                      Docker Network                      │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  ┌─────────────────────────────────────────────────┐    │
│  │            Frontend (Nginx)                      │    │
│  │            Port: 80                              │    │
│  │  ┌─────────────┐    ┌──────────────────────┐    │    │
│  │  │  Vue SPA    │    │  API Proxy /api/*    │    │    │
│  │  │  静态资源    │    │  → backend:8080      │    │    │
│  │  └─────────────┘    └──────────────────────┘    │    │
│  └─────────────────────────────────────────────────┘    │
│                          │                               │
│                          ▼                               │
│  ┌─────────────────────────────────────────────────┐    │
│  │            Backend (Spring Boot)                 │    │
│  │            Port: 8080                            │    │
│  │  ┌─────────────┐    ┌──────────────────────┐    │    │
│  │  │  REST API   │    │  JWT Auth            │    │    │
│  │  │  /api/*     │    │  Spring Security     │    │    │
│  │  └─────────────┘    └──────────────────────┘    │    │
│  └─────────────────────────────────────────────────┘    │
│                    │           │                         │
│          ┌────────┘           └────────┐                │
│          ▼                             ▼                │
│  ┌───────────────┐            ┌───────────────┐        │
│  │    MySQL      │            │    Redis      │        │
│  │   Port: 3306  │            │   Port: 6379  │        │
│  │   数据持久化   │            │   缓存服务     │        │
│  └───────────────┘            └───────────────┘        │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

### 部署步骤

#### 1. 服务器准备

```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装 Docker
curl -fsSL https://get.docker.com | sh

# 安装 Docker Compose
sudo apt install docker-compose-plugin -y

# 添加当前用户到 docker 组
sudo usermod -aG docker $USER
```

#### 2. 部署应用

```bash
# 克隆项目
git clone https://github.com/WangWinston/blog-project.git
cd blog-project

# 创建环境配置
cp .env.example .env

# 编辑配置（重要！）
vim .env
```

#### 3. 修改生产配置

```env
# MySQL 配置（请修改密码！）
MYSQL_ROOT_PASSWORD=your-strong-root-password
MYSQL_PASSWORD=your-strong-blog-password

# JWT 配置（请修改密钥！）
JWT_SECRET=your-production-secret-key-must-be-at-least-256-bits-long

# GitHub OAuth（可选）
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret
GITHUB_REDIRECT_URI=https://your-domain.com/api/auth/github/callback
```

#### 4. 启动服务

```bash
# 构建并启动所有服务
docker compose up -d --build

# 查看日志
docker compose logs -f

# 查看服务状态
docker compose ps
```

### 服务管理命令

```bash
# 启动所有服务
docker compose up -d

# 停止所有服务
docker compose down

# 重启特定服务
docker compose restart backend
docker compose restart frontend

# 查看日志
docker compose logs -f backend
docker compose logs -f frontend

# 重新构建并启动
docker compose up -d --build
```

---

## 配置说明

### 端口配置

| 服务 | 开发端口 | 生产端口 | 说明 |
|------|---------|---------|------|
| Frontend | 3000 | 80 | 前端服务 |
| Backend | 8080 | 8080 (内部) | 后端API |
| MySQL | 3306 | 3306 (内部) | 数据库 |
| Redis | 6379 | 6379 (内部) | 缓存 |

### 环境变量

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `MYSQL_ROOT_PASSWORD` | root123 | MySQL root密码 |
| `MYSQL_PASSWORD` | blog123 | MySQL blog用户密码 |
| `JWT_SECRET` | - | JWT签名密钥（必须修改） |
| `JWT_EXPIRATION` | 7200000 | Token过期时间(毫秒) |
| `GITHUB_CLIENT_ID` | - | GitHub OAuth客户端ID |
| `GITHUB_CLIENT_SECRET` | - | GitHub OAuth密钥 |

### Nginx配置

配置文件位置: `blog-web/nginx.conf`

主要功能：
- SPA路由支持 (try_files fallback)
- API代理 `/api` → `backend:8080`
- Gzip压缩
- 静态资源缓存
- 文件上传限制 10MB

---

## 常见问题

### Q1: 后端启动失败，数据库连接错误

**解决方案**: 确保MySQL已启动并初始化完成

```bash
# 检查MySQL状态
docker compose -f docker-compose.infra.yml ps

# 查看MySQL日志
docker compose -f docker-compose.infra.yml logs mysql

# 等待MySQL完全启动（约30秒）
```

### Q2: 前端无法访问后端API

**解决方案**: 检查Vite代理配置和CORS设置

1. 确认后端已启动: http://localhost:8080/actuator/health
2. 检查 `vite.config.ts` 中的代理配置
3. 检查 `SecurityConfig.java` 中的CORS配置

### Q3: Docker构建失败

**解决方案**: 清理Docker缓存后重试

```bash
# 清理未使用的镜像和容器
docker system prune -a

# 重新构建
docker compose build --no-cache
```

### Q4: 登录后Token无效

**解决方案**: 检查JWT配置

1. 确保 `JWT_SECRET` 在前后端一致
2. 检查Token是否正确存储在localStorage
3. 查看浏览器控制台是否有错误

### Q5: 文件上传失败

**解决方案**: 检查文件大小限制

- 后端限制: `application.yml` 中 `spring.servlet.multipart.max-file-size`
- Nginx限制: `nginx.conf` 中 `client_max_body_size`

---

## 更新日志

| 日期 | 版本 | 更新内容 |
|------|------|---------|
| 2026-03-03 | 1.0.0 | 初始版本 |

---

## 联系方式

如有问题，请提交 Issue: https://github.com/WangWinston/blog-project/issues