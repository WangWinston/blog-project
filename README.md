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

- 服务器: 2核2G 以上
- 已安装 Docker 和 Docker Compose
- 已安装 Git

### 一键部署

```bash
# 1. 克隆项目
git clone https://github.com/WangWinston/blog-project.git
cd blog-project

# 2. 配置环境变量
cp .env.example .env
nano .env  # 修改配置

# 3. 启动服务
chmod +x deploy.sh
./deploy.sh start
```

访问 `http://你的服务器IP` 即可看到博客首页。

### 部署脚本命令

```bash
./deploy.sh start    # 启动服务
./deploy.sh stop     # 停止服务
./deploy.sh restart  # 重启服务
./deploy.sh logs     # 查看日志
./deploy.sh status   # 查看状态
./deploy.sh update   # 更新部署
./deploy.sh backup   # 备份数据库
```

## 环境变量配置

编辑 `.env` 文件:

```env
# MySQL
MYSQL_ROOT_PASSWORD=root123    # MySQL root 密码
MYSQL_PASSWORD=blog123         # 博客数据库密码

# JWT
JWT_SECRET=your-secret-key     # JWT 密钥 (至少256位)

# GitHub OAuth (可选)
GITHUB_CLIENT_ID=your-client-id
GITHUB_CLIENT_SECRET=your-client-secret
GITHUB_REDIRECT_URI=http://your-domain/api/auth/github/callback
```

## 默认账号

- 用户名: `admin`
- 密码: `admin123`

> 首次登录后请立即修改密码

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
├── nginx/                  # Nginx 配置
├── mysql/                  # 数据库初始化
├── docker-compose.yml      # Docker 编排
├── deploy.sh               # 部署脚本
└── .env.example            # 环境变量示例
```

## 本地开发

### 后端

```bash
cd blog-backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 前端

```bash
cd blog-web
npm install
npm run dev
```

## 功能特性

- 文章管理 (Markdown 编辑、版本历史、定时发布)
- 分类和标签管理
- 评论系统 (审核、楼中楼)
- 用户系统 (本地注册、GitHub OAuth)
- 点赞、收藏功能
- 后台管理 Dashboard
- SEO 优化

## License

MIT