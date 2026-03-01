# 博客系统设计文档

> 创建日期：2026-02-28

## 1. 项目概述

构建一个功能完整的博客Web应用，包含门户和后台管理两大模块，支持响应式设计，页面清新干净。

### 1.1 核心特性

- 用户系统：本地注册登录、GitHub OAuth登录
- 文章管理：Markdown编辑、版本历史、定时发布、导入导出
- 内容组织：分类（树形）、标签、热门文章推荐
- 互动功能：评论（楼中楼）、点赞、收藏
- 滑动验证码：前端验证 + 后端轻量校验
- 后台Dashboard：数据看板、统计图表

### 1.2 技术栈

| 层级 | 技术选型 |
|------|---------|
| 前端 | Vue 3 + Vite + TailwindCSS + Pinia + TypeScript |
| 后端 | Spring Boot 3.x + Spring Security + JWT + MyBatis-Plus |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis 7.x |
| 反向代理 | Nginx |
| 部署 | Docker Compose |

---

## 2. 整体架构

### 2.1 系统架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                      Docker Compose                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────────────┐    ┌──────────────┐      │
│   │         blog-web                │    │    nginx     │      │
│   │  ┌──────────┐  ┌──────────┐     │    │  (反向代理)   │      │
│   │  │ 门户页面  │  │ 后台页面  │     │    │   :80/443   │      │
│   │  │  /portal │  │  /admin   │     │    └──────────────┘      │
│   │  └──────────┘  └──────────┘     │            │              │
│   │      (Vue 3 SPA)                 │            │              │
│   │         :80                      │            │              │
│   └─────────────────────────────────┘            │              │
│                       │                           │              │
│                       └───────────────────────────┘              │
│                                    ▼                             │
│                          ┌──────────────┐                       │
│                          │blog-backend  │                       │
│                          │(Spring Boot) │                       │
│                          │    :8080     │                       │
│                          └──────────────┘                       │
│                              │          │                        │
│                    ┌─────────▼──┐  ┌────▼────────┐               │
│                    │   MySQL    │  │   Redis    │               │
│                    │   8.0      │  │   7.x      │               │
│                    └────────────┘  └────────────┘               │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 前端路由规划

```
/                           → 重定向到 /portal

/portal                     → 门户首页
/portal/article/:id         → 文章详情
/portal/category/:id        → 分类文章列表
/portal/tag/:id             → 标签文章列表
/portal/search              → 搜索结果
/portal/login               → 登录/注册
/portal/user                → 用户中心（需登录）

/admin                      → 后台首页（Dashboard）
/admin/login                → 后台登录
/admin/articles             → 文章管理
/admin/articles/edit/:id?   → 文章编辑
/admin/categories           → 分类管理
/admin/tags                 → 标签管理
/admin/comments             → 评论管理
/admin/users                → 用户管理
/admin/settings             → 系统设置
```

---

## 3. 后端架构（DDD）

### 3.1 模块结构

```
blog-backend/
├── blog-common/                   # 公共模块
│   ├── util/                     # 工具类
│   ├── constant/                 # 常量定义
│   ├── enums/                    # 枚举类
│   ├── exception/                # 全局异常定义
│   └── config/                   # 通用配置
│
├── blog-domain/                  # 领域层
│   ├── model/                    # 领域模型（实体、值对象、聚合根）
│   ├── repository/               # 仓储接口
│   ├── service/                  # 领域服务
│   └── event/                    # 领域事件
│
├── blog-infrastructure/          # 基础设施层
│   ├── persistence/              # 持久化实现（MyBatis-Plus）
│   ├── cache/                    # Redis缓存实现
│   ├── security/                 # 安全认证实现
│   └── external/                 # 外部服务集成（GitHub OAuth）
│
├── blog-application/             # 应用层
│   ├── service/                  # 应用服务
│   ├── dto/                      # 数据传输对象
│   └── assembler/                # DTO转换器
│
├── blog-interfaces/              # 接口层
│   ├── controller/               # REST API控制器
│   ├── assembler/                # 请求/响应转换
│   └── dto/                      # 请求/响应DTO
│
└── blog-bootstrap/               # 启动模块
    ├── BlogApplication.java      # 启动类
    └── application.yml           # 配置文件
```

### 3.2 模块依赖关系

```
bootstrap → interfaces → application → { domain, infrastructure }
                                        domain → common
                                        infrastructure → domain
```

| 模块 | 依赖 |
|------|------|
| blog-common | 无（最底层） |
| blog-domain | blog-common |
| blog-infrastructure | blog-domain |
| blog-application | blog-domain, blog-infrastructure |
| blog-interfaces | blog-application |
| blog-bootstrap | blog-interfaces (传递依赖全部模块) |

### 3.3 判空规范

| 场景 | 使用方式 |
|------|---------|
| 对象判等 | `Objects.equals(a, b)` |
| 字符串判空 | `StringUtils.isBlank(str)` (commons-lang3) |
| 集合判空 | `CollectionUtils.isEmpty(list)` (commons-collections4) |
| 对象判空 | `ObjectUtils.isEmpty(obj)` (commons-lang3) |

---

## 4. 数据库设计

### 4.1 核心表

| 表名 | 说明 | 核心字段 |
|------|------|---------|
| user | 用户表 | username, password, email, role, github_id |
| profile | 用户资料扩展表 | bio, website, location |
| article | 文章表 | title, content(markdown), status, is_top, is_recommend |
| category | 分类表（树形结构） | name, slug, parent_id |
| tag | 标签表 | name, slug, color |
| article_tag | 文章标签关联表 | article_id, tag_id |
| comment | 评论表（支持楼中楼） | article_id, user_id, parent_id, content |
| article_version | 文章版本历史 | article_id, version, content, change_log |
| scheduled_article | 定时发布任务表 | article_id, publish_time, status |
| user_like | 用户点赞记录 | user_id, article_id |
| user_favorite | 用户收藏记录 | user_id, article_id |

### 4.2 文章状态流转

```
新建 → 草稿 → 已发布 → 已下架
        ↓
    定时发布
```

- **草稿**：可随时保存，不对外展示
- **已发布**：门户可见，支持置顶、推荐
- **已下架**：门户不可见，可重新发布
- **定时发布**：到指定时间自动发布
- **版本历史**：每次编辑保存新版本，可回滚

---

## 5. 前端架构

### 5.1 项目结构

```
blog-web/
├── src/
│   ├── components/               # 公共组件
│   │   ├── common/               # 通用组件
│   │   ├── portal/               # 门户专用组件
│   │   └── admin/                # 后台专用组件
│   ├── views/                    # 页面视图
│   │   ├── portal/               # 门户页面
│   │   └── admin/                # 后台页面
│   ├── router/                   # 路由配置
│   ├── stores/                   # Pinia状态管理
│   ├── api/                      # API请求
│   ├── composables/              # 组合式函数
│   ├── utils/                    # 工具函数
│   └── types/                    # TypeScript类型
```

### 5.2 页面布局

**门户页面**：
- Header：Logo、导航、搜索、登录/用户头像
- 首页：热门推荐轮播 + 分类Tab切换 + 文章卡片列表 + 侧边栏（热门文章、标签云、最新评论）
- Footer：版权信息

**后台Dashboard**：
- TopNav：Logo、功能菜单、用户头像
- 统计卡片：文章总数、待发布、待审评论、用户数
- 图表区：访问趋势图、热门文章Top10
- 快捷操作：最新评论、快捷入口

---

## 6. 核心功能模块

### 6.1 用户认证

**登录方式**：
1. 本地登录：用户名/邮箱 + 密码
2. GitHub OAuth：授权码模式

**JWT Token结构**：
```json
{
  "sub": "user_id",
  "username": "xxx",
  "role": "USER/ADMIN",
  "exp": 过期时间,
  "iat": 签发时间
}
```

### 6.2 滑动验证码

**实现方案**：前端验证 + 后端轻量校验

**流程**：
1. 前端显示滑块验证码（vue-puzzle-vcode组件）
2. 用户拖动滑块完成验证
3. 验证通过，生成验证标记（Base64编码时间戳+随机数）
4. 提交登录/注册时携带验证标记
5. 后端校验标记格式和时间戳有效性（5分钟内）

**应用场景**：登录、注册

### 6.3 评论系统

**数据结构**：支持楼中楼回复，parent_id关联父评论

**查询策略**：一次性查出所有评论，前端构建树结构

### 6.4 API设计

| 模块 | 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|------|
| **门户** | GET | /api/portal/articles | 文章列表（分页） | 公开 |
| | GET | /api/portal/articles/{id} | 文章详情 | 公开 |
| | GET | /api/portal/articles/hot | 热门文章 | 公开 |
| | POST | /api/portal/comments | 发表评论 | 登录 |
| | POST | /api/portal/articles/{id}/like | 点赞 | 登录 |
| | POST | /api/portal/articles/{id}/favorite | 收藏 | 登录 |
| **认证** | POST | /api/auth/login | 本地登录 | 公开 |
| | POST | /api/auth/register | 注册 | 公开 |
| | GET | /api/auth/github | GitHub OAuth | 公开 |
| **后台** | GET | /api/admin/articles | 文章列表 | 管理员 |
| | POST | /api/admin/articles | 创建文章 | 管理员 |
| | PUT | /api/admin/articles/{id} | 更新文章 | 管理员 |
| | DELETE | /api/admin/articles/{id} | 删除文章 | 管理员 |
| | POST | /api/admin/articles/{id}/publish | 发布文章 | 管理员 |
| | POST | /api/admin/articles/{id}/schedule | 定时发布 | 管理员 |
| | GET | /api/admin/articles/{id}/versions | 版本历史 | 管理员 |
| | POST | /api/admin/articles/{id}/rollback | 版本回滚 | 管理员 |
| | GET | /api/admin/dashboard/stats | 统计数据 | 管理员 |

---

## 7. 缓存策略

### 7.1 缓存层次

```
请求 → 本地缓存(L1, Caffeine) → Redis缓存(L2) → 数据库
```

### 7.2 Redis缓存Key设计

| Key模式 | 说明 | TTL |
|---------|------|-----|
| `article:detail:{id}` | 文章详情 | 1小时 |
| `article:list:page:{page}` | 文章列表页 | 10分钟 |
| `article:hot` | 热门文章Top10 | 30分钟 |
| `category:tree` | 分类树 | 1天 |
| `tag:all` | 所有标签 | 1天 |
| `user:info:{id}` | 用户信息 | 30分钟 |

### 7.3 浏览量计数

```
Key: article:view:count
Type: Hash
Value: {articleId: viewCount, ...}

流程：
1. 用户访问文章 → HINCRBY article:view:count {id} 1
2. 定时任务（每5分钟）→ 批量同步到数据库
```

### 7.4 点赞/收藏

```
点赞：article:like:{articleId} → Set<userId>
收藏：user:favorite:{userId} → Set<articleId>

定时任务同步到数据库
```

---

## 8. 部署方案

### 8.1 目录结构

```
blog-project/
├── blog-web/                      # 前端项目
├── blog-backend/                  # 后端项目
├── nginx/                         # Nginx配置
│   ├── nginx.conf
│   └── ssl/
├── mysql/                         # MySQL初始化
│   └── init/init.sql
├── docker-compose.yml
├── docker-compose.dev.yml
└── .env
```

### 8.2 Docker Compose服务

| 服务 | 镜像 | 端口 |
|------|------|------|
| nginx | nginx:alpine | 80, 443 |
| blog-backend | 自定义构建 | 8080 |
| mysql | mysql:8.0 | 3306 |
| redis | redis:7-alpine | 6379 |

### 8.3 环境配置

| 环境 | 配置文件 |
|------|---------|
| 开发 | application-dev.yml |
| 测试 | application-test.yml |
| 生产 | application-prod.yml |

---

## 9. 第三方依赖

### 9.1 后端依赖

| 依赖 | 用途 |
|------|------|
| commons-lang3 | 字符串、对象工具 |
| commons-collections4 | 集合工具 |
| MyBatis-Plus | ORM框架 |
| Spring Security | 安全认证 |
| JWT (jjwt) | Token生成验证 |
| Spring Data Redis | Redis操作 |

### 9.2 前端依赖

| 依赖 | 用途 |
|------|------|
| Vue 3 | 前端框架 |
| Vite | 构建工具 |
| TailwindCSS | CSS框架 |
| Pinia | 状态管理 |
| Vue Router | 路由管理 |
| Axios | HTTP请求 |
| vue-puzzle-vcode | 滑动验证码 |
| markdown-it | Markdown渲染 |
| highlight.js | 代码高亮 |
| ECharts | 图表展示 |