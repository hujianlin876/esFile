#!/bin/bash

# 后端启动脚本
# 使用方法: ./start.sh [profile]

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEFAULT_PROFILE="dev"

# 显示帮助信息
show_help() {
    echo "用法: $0 [profile]"
    echo ""
    echo "参数:"
    echo "  profile   环境配置 (dev|prod|test) 默认: $DEFAULT_PROFILE"
    echo ""
    echo "示例:"
    echo "  $0 dev     # 启动开发环境"
    echo "  $0 prod    # 启动生产环境"
    echo "  $0 test    # 启动测试环境"
    echo ""
    echo "其他命令:"
    echo "  $0 clean   # 清理并重新编译"
    echo "  $0 build   # 构建项目"
    echo "  $0 stop    # 停止服务"
    echo "  $0 status  # 查看服务状态"
    echo "  $0 logs    # 查看日志"
}

# 检查Java环境
check_java() {
    if ! command -v java &> /dev/null; then
        echo "错误: 未找到Java环境，请先安装Java"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    echo "Java版本: $JAVA_VERSION"
}

# 检查Maven环境
check_maven() {
    if ! command -v mvn &> /dev/null; then
        echo "错误: 未找到Maven环境，请先安装Maven"
        exit 1
    fi
    
    MAVEN_VERSION=$(mvn -version 2>&1 | head -n 1)
    echo "Maven版本: $MAVEN_VERSION"
}

# 清理项目
clean_project() {
    echo "清理项目..."
    mvn clean
}

# 构建项目
build_project() {
    echo "构建项目..."
    mvn clean compile
}

# 启动服务
start_service() {
    local profile="${1:-$DEFAULT_PROFILE}"
    
    echo "启动后端服务 (环境: $profile)..."
    echo "项目目录: $PROJECT_DIR"
    echo "日志目录: $PROJECT_DIR/logs"
    
    # 检查日志目录
    mkdir -p "$PROJECT_DIR/logs"
    
    # 启动服务
    mvn spring-boot:run -Dspring-boot.run.profiles="$profile" &
    
    # 保存进程ID
    echo $! > "$PROJECT_DIR/backend.pid"
    
    echo "服务已启动，进程ID: $(cat $PROJECT_DIR/backend.pid)"
    echo "查看日志: ./logs/tail-log.sh main -f"
    echo "查看状态: $0 status"
}

# 停止服务
stop_service() {
    local pid_file="$PROJECT_DIR/backend.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p "$pid" > /dev/null 2>&1; then
            echo "停止后端服务 (PID: $pid)..."
            kill "$pid"
            rm -f "$pid_file"
            echo "服务已停止"
        else
            echo "服务未运行"
            rm -f "$pid_file"
        fi
    else
        echo "未找到进程ID文件，尝试查找Java进程..."
        local java_pid=$(ps aux | grep "spring-boot:run" | grep -v grep | awk '{print $2}')
        if [ -n "$java_pid" ]; then
            echo "找到Java进程 (PID: $java_pid)，正在停止..."
            kill "$java_pid"
            echo "服务已停止"
        else
            echo "未找到运行的后端服务"
        fi
    fi
}

# 查看服务状态
check_status() {
    local pid_file="$PROJECT_DIR/backend.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p "$pid" > /dev/null 2>&1; then
            echo "✅ 后端服务正在运行 (PID: $pid)"
            echo "端口: 8080"
            echo "日志: $PROJECT_DIR/logs/"
        else
            echo "❌ 后端服务未运行 (PID文件存在但进程不存在)"
            rm -f "$pid_file"
        fi
    else
        local java_pid=$(ps aux | grep "spring-boot:run" | grep -v grep | awk '{print $2}')
        if [ -n "$java_pid" ]; then
            echo "✅ 后端服务正在运行 (PID: $java_pid)"
            echo "端口: 8080"
            echo "日志: $PROJECT_DIR/logs/"
        else
            echo "❌ 后端服务未运行"
        fi
    fi
    
    # 检查端口
    if lsof -i :8080 > /dev/null 2>&1; then
        echo "✅ 端口8080正在监听"
    else
        echo "❌ 端口8080未监听"
    fi
}

# 查看日志
show_logs() {
    if [ -f "$PROJECT_DIR/logs/tail-log.sh" ]; then
        "$PROJECT_DIR/logs/tail-log.sh" main 50
    else
        echo "日志查看脚本不存在，直接查看日志文件:"
        if [ -f "$PROJECT_DIR/logs/esfile-backend-dev.log" ]; then
            tail -n 50 "$PROJECT_DIR/logs/esfile-backend-dev.log"
        else
            echo "日志文件不存在"
        fi
    fi
}

# 主函数
main() {
    local action="${1:-start}"
    
    case $action in
        "start"|"s")
            check_java
            check_maven
            start_service "$2"
            ;;
        "stop"|"st")
            stop_service
            ;;
        "restart"|"r")
            stop_service
            sleep 2
            check_java
            check_maven
            start_service "$2"
            ;;
        "status"|"st")
            check_status
            ;;
        "logs"|"l")
            show_logs
            ;;
        "clean"|"c")
            clean_project
            ;;
        "build"|"b")
            build_project
            ;;
        "help"|"h"|"-h"|"--help")
            show_help
            ;;
        *)
            echo "错误: 未知的操作 '$action'"
            echo ""
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"
