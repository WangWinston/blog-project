#!/bin/bash
# 博客系统混合部署脚本
# 用法: ./setup.sh [命令]
# 命令: init | start | stop | status | logs | help

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 配置
NODE_VERSION=20
JAVA_VERSION=21
MAVEN_VERSION=3.9.6
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

# 打印函数
info() { echo -e "${GREEN}[INFO]${NC} $1"; }
warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
error() { echo -e "${RED}[ERROR]${NC} $1"; exit 1; }
step() { echo -e "${BLUE}[STEP]${NC} $1"; }

# 检查命令是否存在
command_exists() {
    command -v "$1" &> /dev/null
}

# 检查 Docker
check_docker() {
    step "检查 Docker..."
    if ! command_exists docker; then
        error "Docker 未安装，请先安装 Docker\n  参考: https://docs.docker.com/engine/install/ubuntu/"
    fi
    if ! docker compose version &> /dev/null; then
        error "Docker Compose 未安装"
    fi
    info "Docker 已安装 ✓"
}

# 检查 Node.js
check_node() {
    step "检查 Node.js..."
    if command_exists node; then
        local version=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
        if [ "$version" -ge 18 ]; then
            info "Node.js $(node -v) 已安装 ✓"
            return 0
        fi
        warn "Node.js 版本过低 (当前: $(node -v))，需要升级"
    fi
    return 1
}

# 安装 Node.js
install_node() {
    step "安装 Node.js ${NODE_VERSION}..."
    info "添加 NodeSource 仓库..."
    curl -fsSL https://deb.nodesource.com/setup_${NODE_VERSION}.x | sudo -E bash -
    info "安装 Node.js..."
    sudo apt-get install -y nodejs
    info "Node.js $(node -v) 安装完成 ✓"
}

# 检查 Java
check_java() {
    step "检查 Java..."
    if command_exists java; then
        local version=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
        if [ "$version" -ge 17 ]; then
            info "Java $(java -version 2>&1 | head -1) 已安装 ✓"
            return 0
        fi
        warn "Java 版本过低，需要升级"
    fi
    return 1
}

# 安装 Java
install_java() {
    step "安装 OpenJDK ${JAVA_VERSION}..."
    sudo apt-get update
    sudo apt-get install -y openjdk-${JAVA_VERSION}-jdk
    info "Java 安装完成 ✓"
    java -version
}

# 检查 Maven
check_maven() {
    step "检查 Maven..."
    if command_exists mvn; then
        local version=$(mvn -version 2>&1 | head -1 | grep -oP 'Apache Maven \K[0-9]+\.[0-9]+\.[0-9]+')
        if [ -n "$version" ]; then
            info "Maven $version 已安装 ✓"
            return 0
        fi
    fi
    return 1
}

# 安装 Maven
install_maven() {
    step "安装 Maven ${MAVEN_VERSION}..."
    local maven_url="https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
    # 国内镜像备用地址
    local maven_mirror="https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz"
    local maven_dir="/opt/maven"

    info "下载 Maven (使用国内镜像加速)..."

    # 先尝试清华镜像，失败则回退到官方源
    if wget --timeout=60 --tries=3 --progress=bar:force "$maven_mirror" -O /tmp/maven.tar.gz 2>&1; then
        info "清华镜像下载成功 ✓"
    elif wget --timeout=60 --tries=3 --progress=bar:force "$maven_url" -O /tmp/maven.tar.gz 2>&1; then
        info "官方源下载成功 ✓"
    else
        error "Maven 下载失败，请检查网络连接或手动安装"
    fi

    info "解压安装 Maven..."
    sudo mkdir -p "$maven_dir"
    sudo tar -xzf /tmp/maven.tar.gz -C "$maven_dir" --strip-components=1

    # 添加到 PATH
    if ! grep -q "MAVEN_HOME" /etc/environment 2>/dev/null; then
        echo "MAVEN_HOME=$maven_dir" | sudo tee -a /etc/environment
        echo "PATH=\"\$PATH:\$MAVEN_HOME/bin\"" | sudo tee -a /etc/environment
    fi

    # 当前会话生效
    export MAVEN_HOME="$maven_dir"
    export PATH="$PATH:$MAVEN_HOME/bin"

    rm -f /tmp/maven.tar.gz
    info "Maven 安装完成 ✓"
    mvn -version
}

# 检查 .env 文件
check_env() {
    if [ ! -f "$PROJECT_DIR/.env" ]; then
        warn ".env 文件不存在，从 .env.example 复制..."
        cp "$PROJECT_DIR/.env.example" "$PROJECT_DIR/.env"
        info "已创建 .env 文件，请根据需要修改配置"
    fi
}

# 启动基础设施
start_infra() {
    step "启动基础设施 (MySQL, Redis)..."
    check_env
    docker compose -f "$PROJECT_DIR/docker-compose.infra.yml" up -d
    info "等待 MySQL 就绪..."
    sleep 10
    info "基础设施启动完成 ✓"
}

# 停止基础设施
stop_infra() {
    step "停止基础设施..."
    docker compose -f "$PROJECT_DIR/docker-compose.infra.yml" down
    info "基础设施已停止"
}

# 安装前端依赖
install_frontend() {
    step "安装前端依赖..."
    cd "$PROJECT_DIR/blog-web"
    npm install
    info "前端依赖安装完成 ✓"
}

# 安装后端依赖
install_backend() {
    step "安装后端依赖..."
    cd "$PROJECT_DIR/blog-backend"
    if [ -f "./mvnw" ]; then
        ./mvnw dependency:resolve -q
    else
        mvn dependency:resolve -q
    fi
    info "后端依赖安装完成 ✓"
}

# 启动前端
start_frontend() {
    step "启动前端应用..."
    cd "$PROJECT_DIR/blog-web"
    # 后台运行
    nohup npm run dev > "$PROJECT_DIR/logs/frontend.log" 2>&1 &
    echo $! > "$PROJECT_DIR/logs/frontend.pid"
    info "前端已启动 (PID: $(cat $PROJECT_DIR/logs/frontend.pid))"
    info "访问: http://localhost:3000"
}

# 启动后端
start_backend() {
    step "启动后端应用..."
    cd "$PROJECT_DIR/blog-backend"
    # 后台运行
    nohup ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev > "$PROJECT_DIR/logs/backend.log" 2>&1 &
    echo $! > "$PROJECT_DIR/logs/backend.pid"
    info "后端已启动 (PID: $(cat $PROJECT_DIR/logs/backend.pid))"
    info "访问: http://localhost:8080"
}

# 停止应用
stop_apps() {
    step "停止应用..."
    if [ -f "$PROJECT_DIR/logs/frontend.pid" ]; then
        kill $(cat "$PROJECT_DIR/logs/frontend.pid") 2>/dev/null || true
        rm -f "$PROJECT_DIR/logs/frontend.pid"
        info "前端已停止"
    fi
    if [ -f "$PROJECT_DIR/logs/backend.pid" ]; then
        kill $(cat "$PROJECT_DIR/logs/backend.pid") 2>/dev/null || true
        rm -f "$PROJECT_DIR/logs/backend.pid"
        info "后端已停止"
    fi
}

# 查看状态
show_status() {
    echo ""
    echo "=== 服务状态 ==="
    echo ""
    echo "基础设施:"
    docker compose -f "$PROJECT_DIR/docker-compose.infra.yml" ps 2>/dev/null || echo "  未启动"
    echo ""
    echo "应用:"
    if [ -f "$PROJECT_DIR/logs/frontend.pid" ]; then
        if kill -0 $(cat "$PROJECT_DIR/logs/frontend.pid") 2>/dev/null; then
            echo "  前端: 运行中 (PID: $(cat $PROJECT_DIR/logs/frontend.pid))"
        else
            echo "  前端: 已停止"
        fi
    else
        echo "  前端: 未启动"
    fi
    if [ -f "$PROJECT_DIR/logs/backend.pid" ]; then
        if kill -0 $(cat "$PROJECT_DIR/logs/backend.pid") 2>/dev/null; then
            echo "  后端: 运行中 (PID: $(cat $PROJECT_DIR/logs/backend.pid))"
        else
            echo "  后端: 已停止"
        fi
    else
        echo "  后端: 未启动"
    fi
    echo ""
}

# 查看日志
show_logs() {
    local service=$1
    case $service in
        frontend|f)
            tail -f "$PROJECT_DIR/logs/frontend.log"
            ;;
        backend|b)
            tail -f "$PROJECT_DIR/logs/backend.log"
            ;;
        mysql|m)
            docker compose -f "$PROJECT_DIR/docker-compose.infra.yml" logs -f mysql
            ;;
        redis|r)
            docker compose -f "$PROJECT_DIR/docker-compose.infra.yml" logs -f redis
            ;;
        *)
            echo "用法: $0 logs [frontend|backend|mysql|redis]"
            ;;
    esac
}

# 初始化
init() {
    info "开始初始化博客系统..."

    # 创建日志目录
    mkdir -p "$PROJECT_DIR/logs"

    # 检查 Docker
    check_docker

    # 启动基础设施
    start_infra

    # 检查/安装 Node.js
    if ! check_node; then
        install_node
    fi

    # 检查/安装 Java
    if ! check_java; then
        install_java
    fi

    # 检查/安装 Maven
    if ! check_maven; then
        install_maven
    fi

    # 安装依赖
    install_frontend
    install_backend

    # 启动应用
    start_frontend
    start_backend

    echo ""
    info "========================================="
    info "  博客系统初始化完成！"
    info "========================================="
    info ""
    info "前端: http://localhost:3000"
    info "后端: http://localhost:8080"
    info "后台: http://localhost:3000/admin"
    info ""
    info "默认账号: admin / admin123"
    info ""
    info "查看状态: ./setup.sh status"
    info "查看日志: ./setup.sh logs [frontend|backend]"
    info ""
}

# 启动所有服务
start_all() {
    info "启动所有服务..."
    mkdir -p "$PROJECT_DIR/logs"
    start_infra
    start_frontend
    start_backend
    show_status
}

# 停止所有服务
stop_all() {
    info "停止所有服务..."
    stop_apps
    stop_infra
    info "所有服务已停止"
}

# 显示帮助
show_help() {
    echo ""
    echo "博客系统混合部署脚本"
    echo ""
    echo "用法: $0 [命令]"
    echo ""
    echo "命令:"
    echo "  init      初始化环境并启动所有服务 (首次部署)"
    echo "  start     启动所有服务"
    echo "  stop      停止所有服务"
    echo "  status    查看服务状态"
    echo "  logs      查看日志 (可选: frontend|backend|mysql|redis)"
    echo "  help      显示帮助"
    echo ""
    echo "首次部署:"
    echo "  1. git clone https://github.com/WangWinston/blog-project.git"
    echo "  2. cd blog-project"
    echo "  3. chmod +x setup.sh"
    echo "  4. ./setup.sh init"
    echo ""
}

# 主入口
case "$1" in
    init)
        init
        ;;
    start)
        start_all
        ;;
    stop)
        stop_all
        ;;
    status)
        show_status
        ;;
    logs)
        show_logs "$2"
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        show_help
        ;;
esac