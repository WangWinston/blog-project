# 博客系统实施计划

> 创建日期：2026-02-28
> 基于设计文档：2026-02-28-blog-design.md

## 实施阶段概览

| 阶段 | 内容 | 预计工作量 |
|------|------|-----------|
| Phase 1 | 项目初始化与基础架构 | 基础搭建 |
| Phase 2 | 后端核心模块开发 | 核心功能 |
| Phase 3 | 前端门户开发 | 门户页面 |
| Phase 4 | 前端后台管理开发 | 后台管理 |
| Phase 5 | 部署与测试 | 部署上线 |

---

## Phase 1: 项目初始化与基础架构

### 1.1 创建项目结构

- [ ] 创建根目录 `blog-project/`
- [ ] 初始化前端项目 `blog-web/` (Vue 3 + Vite + TailwindCSS)
- [ ] 初始化后端项目 `blog-backend/` (Spring Boot 多模块)
- [ ] 创建部署配置目录 `nginx/`, `mysql/`

### 1.2 后端多模块搭建

- [ ] 创建 `blog-common` 模块（公共工具、常量、枚举、异常、配置）
- [ ] 创建 `blog-domain` 模块（领域模型、仓储接口、领域服务）
- [ ] 创建 `blog-infrastructure` 模块（持久化、缓存、安全、外部服务）
- [ ] 创建 `blog-application` 模块（应用服务、DTO、转换器）
- [ ] 创建 `blog-interfaces` 模块（控制器、请求响应DTO）
- [ ] 创建 `blog-bootstrap` 模块（启动类、配置文件）
- [ ] 配置模块依赖关系
- [ ] 添加核心依赖（commons-lang3, commons-collections4, MyBatis-Plus, Spring Security, JWT, Redis）

### 1.3 前端项目搭建

- [ ] 使用 Vite 创建 Vue 3 + TypeScript 项目
- [ ] 配置 TailwindCSS
- [ ] 配置 Pinia 状态管理
- [ ] 配置 Vue Router
- [ ] 配置 Axios 请求封装
- [ ] 创建项目目录结构（components, views, router, stores, api, utils, types）
- [ ] 安装核心依赖（vue-puzzle-vcode, markdown-it, highlight.js, echarts）

### 1.4 数据库初始化

- [ ] 编写数据库建表SQL脚本
  - [ ] user 表
  - [ ] profile 表
  - [ ] article 表
  - [ ] category 表
  - [ ] tag 表
  - [ ] article_tag 表
  - [ ] comment 表
  - [ ] article_version 表
  - [ ] scheduled_article 表
  - [ ] user_like 表
  - [ ] user_favorite 表
- [ ] 编写索引创建脚本
- [ ] 编写初始化数据脚本（管理员账号、默认分类）

### 1.5 Docker配置

- [ ] 编写后端 Dockerfile
- [ ] 编写前端 Dockerfile
- [ ] 编写 docker-compose.yml
- [ ] 编写 docker-compose.dev.yml
- [ ] 编写 Nginx 配置文件
- [ ] 创建 .env 环境变量文件

---

## Phase 2: 后端核心模块开发

### 2.1 公共模块 (blog-common)

- [ ] 定义通用响应结构 `Result<T>`
- [ ] 定义分页响应结构 `PageResult<T>`
- [ ] 定义全局异常类 `BlogException`
- [ ] 定义全局异常处理器 `GlobalExceptionHandler`
- [ ] 定义常用枚举（用户角色、文章状态、评论状态等）
- [ ] 定义常用常量（JWT密钥、缓存Key前缀等）
- [ ] 创建工具类（日期、字符串、加密等）

### 2.2 领域模块 (blog-domain)

- [ ] 定义用户聚合
  - [ ] User 实体
  - [ ] Profile 值对象
  - [ ] UserRepository 接口
- [ ] 定义文章聚合
  - [ ] Article 聚合根
  - [ ] Content 值对象
  - [ ] ArticleVersion 实体
  - [ ] ArticleRepository 接口
- [ ] 定义分类实体 Category
- [ ] 定义标签实体 Tag
- [ ] 定义评论实体 Comment
- [ ] 定义领域服务（文章发布、评论管理等）

### 2.3 基础设施模块 (blog-infrastructure)

- [ ] 持久化层实现
  - [ ] 用户仓储实现
  - [ ] 文章仓储实现
  - [ ] 分类仓储实现
  - [ ] 标签仓储实现
  - [ ] 评论仓储实现
  - [ ] 配置 MyBatis-Plus
- [ ] 缓存层实现
  - [ ] Redis 配置
  - [ ] 缓存工具类
  - [ ] 文章缓存实现
  - [ ] 用户缓存实现
- [ ] 安全认证实现
  - [ ] JWT 工具类
  - [ ] Spring Security 配置
  - [ ] 认证过滤器
  - [ ] 用户详情服务实现
- [ ] 外部服务集成
  - [ ] GitHub OAuth 客户端

### 2.4 应用模块 (blog-application)

- [ ] 用户服务
  - [ ] 用户注册
  - [ ] 用户登录
  - [ ] GitHub OAuth 登录
  - [ ] 用户信息管理
- [ ] 文章服务
  - [ ] 文章CRUD
  - [ ] 文章发布/下架
  - [ ] 文章版本管理
  - [ ] 定时发布
  - [ ] 文章导入导出
- [ ] 分类服务
  - [ ] 分类CRUD
  - [ ] 分类树构建
- [ ] 标签服务
  - [ ] 标签CRUD
  - [ ] 标签关联管理
- [ ] 评论服务
  - [ ] 评论发表
  - [ ] 评论审核
  - [ ] 评论树构建
- [ ] 互动服务
  - [ ] 点赞
  - [ ] 收藏
  - [ ] 浏览量统计
- [ ] Dashboard服务
  - [ ] 统计数据聚合

### 2.5 接口模块 (blog-interfaces)

- [ ] 门户API控制器
  - [ ] 文章接口（列表、详情、热门、相关）
  - [ ] 分类接口
  - [ ] 标签接口
  - [ ] 评论接口
  - [ ] 用户接口
- [ ] 认证API控制器
  - [ ] 登录接口
  - [ ] 注册接口
  - [ ] GitHub OAuth 接口
  - [ ] 登出接口
- [ ] 后台API控制器
  - [ ] 文章管理接口
  - [ ] 分类管理接口
  - [ ] 标签管理接口
  - [ ] 评论管理接口
  - [ ] 用户管理接口
  - [ ] Dashboard统计接口
  - [ ] 系统设置接口

### 2.6 定时任务

- [ ] 定时发布任务（每分钟扫描）
- [ ] 浏览量同步任务（每5分钟）
- [ ] 点赞/收藏同步任务（每5分钟）

---

## Phase 3: 前端门户开发

### 3.1 公共组件

- [ ] AppHeader 组件（导航栏）
- [ ] AppFooter 组件（页脚）
- [ ] LoadingSpinner 组件（加载动画）
- [ ] Pagination 组件（分页）
- [ ] EmptyState 组件（空状态）

### 3.2 门户专用组件

- [ ] ArticleCard 组件（文章卡片）
- [ ] ArticleList 组件（文章列表）
- [ ] TagCloud 组件（标签云）
- [ ] CategoryNav 组件（分类导航）
- [ ] CommentSection 组件（评论区）
- [ ] CommentItem 组件（评论项，支持楼中楼）
- [ ] HotArticles 组件（热门文章）
- [ ] RecentComments 组件（最新评论）
- [ ] SiteStats 组件（网站统计）
- [ ] SliderCaptcha 组件（滑动验证码）

### 3.3 门户页面

- [ ] Home 页面
  - [ ] 热门推荐轮播
  - [ ] 分类Tab切换
  - [ ] 文章卡片列表
  - [ ] 侧边栏（热门文章、标签云、最新评论、统计）
- [ ] ArticleDetail 页面
  - [ ] 文章内容（Markdown渲染）
  - [ ] 代码高亮
  - [ ] 文章信息（作者、时间、分类、标签）
  - [ ] 点赞、收藏按钮
  - [ ] 相关文章推荐
  - [ ] 评论区
- [ ] Category 页面（分类文章列表）
- [ ] Tag 页面（标签文章列表）
- [ ] Search 页面（搜索结果）
- [ ] Login 页面
  - [ ] 登录表单
  - [ ] 注册表单
  - [ ] GitHub 登录按钮
  - [ ] 滑动验证码
- [ ] UserCenter 页面
  - [ ] 用户信息
  - [ ] 我的收藏
  - [ ] 我的评论

### 3.4 状态管理

- [ ] user store（用户状态、登录状态）
- [ ] article store（文章缓存）
- [ ] app store（应用状态、主题）

### 3.5 API封装

- [ ] portal API（门户相关接口）
- [ ] auth API（认证相关接口）

---

## Phase 4: 前端后台管理开发

### 4.1 后台专用组件

- [ ] DashboardCard 组件（统计卡片）
- [ ] ArticleEditor 组件（Markdown编辑器）
- [ ] ArticleTable 组件（文章表格）
- [ ] CategoryTree 组件（分类树）
- [ ] StatsChart 组件（统计图表）
- [ ] QuickActions 组件（快捷操作）

### 4.2 后台页面

- [ ] AdminLogin 页面
  - [ ] 登录表单
  - [ ] 滑动验证码
- [ ] Dashboard 页面
  - [ ] 统计卡片（文章数、用户数、评论数、访问量）
  - [ ] 访问趋势图
  - [ ] 热门文章Top10
  - [ ] 最新评论
  - [ ] 快捷操作入口
- [ ] ArticleManage 页面
  - [ ] 文章列表表格
  - [ ] 筛选（状态、分类、标签）
  - [ ] 搜索
  - [ ] 批量操作
- [ ] ArticleEdit 页面
  - [ ] Markdown编辑器（实时预览）
  - [ ] 封面上传
  - [ ] 分类选择
  - [ ] 标签选择
  - [ ] 发布设置（立即发布、定时发布、保存草稿）
  - [ ] 版本历史查看
- [ ] CategoryManage 页面
  - [ ] 分类树展示
  - [ ] 分类CRUD
  - [ ] 拖拽排序
- [ ] TagManage 页面
  - [ ] 标签列表
  - [ ] 标签CRUD
  - [ ] 颜色选择
- [ ] CommentManage 页面
  - [ ] 评论列表
  - [ ] 评论审核
  - [ ] 评论删除
- [ ] UserManage 页面
  - [ ] 用户列表
  - [ ] 用户搜索
  - [ ] 用户状态管理
- [ ] Settings 页面
  - [ ] 网站基本信息
  - [ ] SEO设置
  - [ ] GitHub OAuth配置

### 4.3 API封装

- [ ] admin API（后台管理相关接口）

---

## Phase 5: 部署与测试

### 5.1 单元测试

- [ ] 后端单元测试（Service层）
- [ ] 后端集成测试（Controller层）

### 5.2 前端测试

- [ ] 组件测试
- [ ] E2E测试（关键流程）

### 5.3 部署准备

- [ ] 后端打包配置
- [ ] 前端构建配置
- [ ] Nginx配置优化
- [ ] SSL证书配置（生产环境）

### 5.4 部署验证

- [ ] 本地Docker部署测试
- [ ] 功能验收测试
- [ ] 性能测试（可选）

---

## 实施顺序建议

```
Phase 1 (项目初始化)
    │
    ├── 1.1 项目结构 ──→ 1.2 后端模块 ──→ 1.3 前端项目
    │                                         │
    ├── 1.4 数据库初始化 ←────────────────────┘
    │
    └── 1.5 Docker配置
            │
            ▼
Phase 2 (后端开发) ──→ 建议按模块顺序开发
    │
    │   2.1 common → 2.2 domain → 2.3 infrastructure
    │                                    │
    │                                    ▼
    │                          2.4 application → 2.5 interfaces
    │
    └── 2.6 定时任务
            │
            ▼
Phase 3 (门户前端) ──→ 3.1公共组件 → 3.2门户组件 → 3.3页面
    │
    └── 3.4状态管理、3.5API封装（可并行）
            │
            ▼
Phase 4 (后台前端) ──→ 4.1后台组件 → 4.2页面
    │
    └── 4.3 API封装
            │
            ▼
Phase 5 (部署测试)
```

---

## 关键技术点

### 后端关键实现

1. **DDD分层**：严格遵循依赖关系，领域层不依赖基础设施
2. **JWT认证**：无状态认证，支持Token刷新
3. **GitHub OAuth**：标准OAuth2.0授权码流程
4. **缓存策略**：Cache-Aside模式，定时同步浏览量/点赞数据
5. **版本管理**：文章每次编辑保存新版本

### 前端关键实现

1. **响应式布局**：TailwindCSS响应式类，适配移动端
2. **Markdown渲染**：markdown-it + highlight.js代码高亮
3. **滑动验证码**：vue-puzzle-vcode组件
4. **状态管理**：Pinia轻量级状态管理
5. **路由守卫**：权限控制，未登录跳转登录页

---

## 验收标准

### 功能验收

- [ ] 用户可以注册、登录（本地+GitHub）
- [ ] 管理员可以发布、编辑、删除文章
- [ ] 文章支持Markdown格式，代码高亮正常
- [ ] 用户可以评论、点赞、收藏文章
- [ ] 后台Dashboard统计数据正确
- [ ] 定时发布功能正常
- [ ] 文章版本历史可查看、可回滚

### 性能验收

- [ ] 页面首屏加载 < 3秒
- [ ] API响应时间 < 500ms
- [ ] 缓存命中率 > 80%

### 安全验收

- [ ] JWT Token有效期为2小时
- [ ] 密码加密存储
- [ ] XSS/SQL注入防护
- [ ] CSRF防护