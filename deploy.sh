#!/bin/bash
# 一键部署脚本
# 用法: ./deploy.sh [命令]
# 命令: start | stop | restart | logs | status | build | update

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 打印信息
info() { echo -e "${GREEN}[INFO]${NC} $1"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }

# 检查 Docker
check_docker() {
    if ! command -v docker &> /dev/null; then
        error "Docker 未安装，请先安装 Docker"
    fi
    if ! command -v docker &> /dev/null || ! docker compose version &> /dev/null; then
        error "Docker Compose 未安装，请先安装 Docker Compose"
    fi
}

# 检查 .env 文件
check_env() {
    if [ ! -f .env ]; then
        warn ".env 文件不存在，从 .env.example 复制..."
        cp .env.example .env
        info "请编辑 .env 文件配置你的参数"
    fi
}

# 启动服务
start() {
    info "启动服务..."
    check_env
    docker compose up -d
    info "服务已启动，访问 http://localhost"
    info "查看日志: docker compose logs -f"
}

# 停止服务
stop() {
    info "停止服务..."
    docker compose down
    info "服务已停止"
}

# 重启服务
restart() {
    stop
    start
}

# 查看日志
logs() {
    docker compose logs -f "$@"
}

# 查看状态
status() {
    docker compose ps
}

# 构建镜像
build() {
    info "构建镜像..."
    docker compose build --no-cache
    info "构建完成"
}

# 更新部署
update() {
    info "拉取最新代码..."
    git pull
    info "重新构建并启动..."
    docker compose up -d --build
    info "更新完成"
}

# 备份数据库
backup() {
    local file="backup_$(date +%Y%m%d_%H%M%S).sql"
    info "备份数据库到 $file..."
    docker exec blog-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD:-root123} blog > "$file"
    info "备份完成: $file"
}

# 显示帮助
show_help() {
    echo "博客系统部署脚本"
    echo ""
    echo "用法: $0 [命令]"
    echo ""
    echo "命令:"
    echo "  start     启动服务"
    echo "  stop      停止服务"
    echo "  restart   重启服务"
    echo "  logs      查看日志 (可选: logs blog-backend)"
    echo "  status    查看状态"
    echo "  build     构建镜像"
    echo "  update    更新部署 (git pull + rebuild)"
    echo "  backup    备份数据库"
    echo ""
    echo "首次部署:"
    echo "  1. git clone https://github.com/WangWinston/blog-project.git"
    echo "  2. cd blog-project"
    echo "  3. cp .env.example .env && nano .env"
    echo "  4. ./deploy.sh start"
}

# 主入口
case "$1" in
    start)   check_docker; start ;;
    stop)    check_docker; stop ;;
    restart) check_docker; restart ;;
    logs)    check_docker; logs "${@:2}" ;;
    status)  check_docker; status ;;
    build)   check_docker; build ;;
    update)  check_docker; update ;;
    backup)  check_docker; backup ;;
    *)       show_help ;;
esac