# 混合部署脚本设计

## 概述

设计一个混合部署脚本，基础设施使用 Docker，应用直接在宿主机运行，便于开发和调试。

## 架构

```
┌─────────────────────────────────────────────────────────┐
│                     宿主机 (Ubuntu)                       │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐                       │
│  │   Node.js   │  │    Java     │                       │
│  │   (前端)     │  │   (后端)    │                       │
│  │   :3000     │  │   :8080     │                       │
│  └──────┬──────┘  └──────┬──────┘                       │
│         │                │                               │
│  ┌──────┴────────────────┴──────┐                       │
│  │          Docker               │                       │
│  │  ┌─────────┐  ┌─────────┐    │                       │
│  │  │  MySQL  │  │  Redis  │    │                       │
│  │  │  :3306  │  │  :6379  │    │                       │
│  │  └─────────┘  └─────────┘    │                       │
│  └───────────────────────────────┘                       │
└─────────────────────────────────────────────────────────┘
```

## 脚本功能

### 主要命令

| 命令 | 描述 |
|------|------|
| `./setup.sh init` | 初始化环境并启动所有服务 |
| `./setup.sh start` | 启动所有服务 |
| `./setup.sh stop` | 停止所有服务 |
| `./setup.sh status` | 查看服务状态 |
| `./setup.sh logs` | 查看日志 |

### 执行流程

```
init 命令流程:
1. 检查 Docker
2. 启动基础设施 (MySQL, Redis)
3. 检查 Node.js 环境
   - 已安装且版本 >= 18? → 跳过
   - 否则 → 安装 Node.js 20
4. 检查 Java 环境
   - 已安装且版本 >= 17? → 跳过
   - 否则 → 安装 OpenJDK 21
5. 安装前端依赖 (npm install)
6. 安装后端依赖 (mvn dependency:resolve)
7. 启动应用
```

## 环境要求

### 目标系统
- Ubuntu 20.04+ / Debian 11+

### 软件版本
- Node.js: 20 LTS
- Java: OpenJDK 21
- Docker: 24+
- Docker Compose: 2+

## 配置文件

### docker-compose.infra.yml

仅包含基础设施服务：

```yaml
services:
  mysql:
    image: mysql:8.0
    ports: ["3306:3306"]
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: blog
      MYSQL_USER: blog
      MYSQL_PASSWORD: blog123
    volumes:
      - mysql-data:/var/lib/mysql
      - ./mysql/init:/docker-entrypoint-initdb.d

  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]
```

### .env

环境变量配置文件，包含数据库密码、JWT 密钥等。

## 安装方式

### Node.js 安装

```bash
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt-get install -y nodejs
```

### Java 安装

```bash
sudo apt-get install -y openjdk-21-jdk
```

## 应用启动

### 前端 (开发模式)

```bash
cd blog-web
npm install
npm run dev  # 监听 :3000
```

### 后端 (开发模式)

```bash
cd blog-backend
./mvnw spring-boot:run  # 监听 :8080
```

## 错误处理

- 环境检查失败 → 提示用户并退出
- 安装失败 → 提示错误信息
- 服务启动失败 → 显示日志并提示

## 后续扩展

- 支持生产模式部署 (npm run build + nginx)
- 支持 PM2 管理进程
- 支持 systemd 服务注册