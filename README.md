# Blog Project - 个人博客系统

一个现代化的个人博客系统，采用 Vue 3 + Spring Boot 技术栈开发。

## 技术栈

**前端:**
- Vue 3 + TypeScript
- Vite + TailwindCSS
- Pinia 状态管理
- Markdown 渲染 + 代码高亮

**后端:**
- Spring Boot 3 + Java 21
- DDD 多模块架构
- Spring Security + JWT
- MyBatis-Plus + MySQL 8
- Redis 缓存

## 快速部署

### 前置要求

- 服务器: Ubuntu 20.04+ (2核2G 以上)
- 已安装 Docker 和 Docker Compose
- 已安装 Git

### 一键部署

```bash
# 1. 克隆项目
git clone https://github.com/WangWinston/blog-project.git
cd blog-project

# 2. 初始化并启动
chmod +x setup.sh
./setup.sh init
```

脚本会自动：
1. 启动 MySQL 和 Redis (Docker)
2. 检查并安装 Node.js 20 和 Java 21
3. 安装项目依赖
4. 启动前端和后端应用

### 访问地址

- 前端门户: http://localhost:3000
- 后台管理: http://localhost:3000/admin
- 后端 API: http://localhost:8080

### 默认账号

- 用户名: `admin`
- 密码: `admin123`

> 首次登录后请立即修改密码

### 管理命令

```bash
./setup.sh start    # 启动所有服务
./setup.sh stop     # 停止所有服务
./setup.sh status   # 查看服务状态
./setup.sh logs     # 查看日志
```

## 环境变量

编辑 `.env` 文件配置：

```env
# MySQL
MYSQL_ROOT_PASSWORD=root123
MYSQL_PASSWORD=blog123

# JWT (生产环境请修改)
JWT_SECRET=your-secret-key-at-least-256-bits

# GitHub OAuth (可选)
GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=
```

## 项目结构

```
blog-project/
├── blog-backend/           # Spring Boot 后端
│   ├── blog-common/        # 公共模块
│   ├── blog-domain/        # 领域模型
│   ├── blog-infrastructure/# 基础设施
│   ├── blog-application/   # 应用服务
│   ├── blog-interfaces/    # 接口层
│   └── blog-bootstrap/     # 启动模块
├── blog-web/               # Vue 3 前端
├── mysql/                  # 数据库初始化脚本
├── docs/                   # 设计文档
├── setup.sh                # 部署脚本
└── docker-compose.infra.yml # 基础设施配置
```

## 本地开发

### 后端

```bash
cd blog-backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 前端

```bash
cd blog-web
npm install
npm run dev
```

## 功能特性

- 文章管理 (Markdown 编辑、定时发布)
- 分类和标签管理
- 评论系统 (审核、楼中楼)
- 用户系统 (本地注册、GitHub OAuth)
- 点赞、收藏功能
- 后台管理 Dashboard
- SEO 优化

## License

MIT