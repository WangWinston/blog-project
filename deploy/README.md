# Blog System 部署包

将此目录上传到服务器 `/usr/local/src/blog` 即可部署。

## 目录结构

```
/usr/local/src/blog/
├── docker-compose.yml   # Docker 编排文件
├── nginx.conf           # Nginx 配置
├── init.sql             # 数据库初始化脚本
├── .env                 # 环境变量配置
├── blog-backend.jar     # 后端 JAR 包
├── web/                 # 前端静态文件
├── ssl/                 # SSL 证书 (可选)
└── backups/             # 备份目录
```

## 快速部署

### 1. 服务器初始化 (首次部署)

```bash
# 上传部署脚本
scp deploy.sh root@your-server:/root/

# 执行初始化
ssh root@your-server
chmod +x deploy.sh
./deploy.sh
```

### 2. 上传应用文件

```bash
# 本地构建
cd blog-backend && mvn clean package -DskipTests
cd ../blog-web && npm run build

# 上传文件
scp deploy/docker-compose.yml root@your-server:/usr/local/src/blog/
scp deploy/nginx.conf root@your-server:/usr/local/src/blog/
scp deploy/init.sql root@your-server:/usr/local/src/blog/
scp deploy/.env.example root@your-server:/usr/local/src/blog/.env
scp blog-backend/blog-bootstrap/target/blog-bootstrap-*.jar root@your-server:/usr/local/src/blog/blog-backend.jar
scp -r blog-web/dist/* root@your-server:/usr/local/src/blog/web/
```

### 3. 配置并启动

```bash
ssh root@your-server

# 修改配置
cd /usr/local/src/blog
nano .env  # 配置 GitHub OAuth、密码等

# 启动服务
docker compose up -d

# 查看日志
docker compose logs -f
```

## 常用命令

```bash
cd /usr/local/src/blog

# 启动
docker compose up -d

# 停止
docker compose down

# 重启
docker compose restart

# 查看日志
docker compose logs -f blog-backend

# 查看状态
docker compose ps

# 进入 MySQL
docker exec -it blog-mysql mysql -u root -p

# 备份数据库
docker exec blog-mysql mysqldump -u root -p blog > backup.sql
```

## 服务器资源限制

针对 2核2G 服务器优化:
- Nginx: 128MB
- 后端: 600MB
- MySQL: 512MB
- Redis: 150MB

## 默认账号

- 用户名: `admin`
- 密码: `admin123`

## SSL证书配置

```bash
# 安装 certbot
apt install -y certbot python3-certbot-nginx

# 获取证书
certbot --nginx -d your-domain.com

# 自动续期
certbot renew --dry-run
```