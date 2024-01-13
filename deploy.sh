#!/bin/bash

pm="cnpm"
server_deploy_dir="/root/bloglite-deployment"
nuxt_deploy_dir="/root/bloglite-deployment/bloglite-nuxt"
admin_dist_dir="/var/www/bloglite-admin"

# 标记变量，用于判断是否需要构建特定项目
build_server=false
build_nuxt=false
build_admin=false

printUsage() {
    echo "Usage: deploy.sh -[a, s, n]"
    echo "  -a: Build Admin side for Nginx"
    echo "  -s: Build API server using Maven"
    echo "  -n: Build Nuxt server using npm & Node.js"
}

# 解析命令行参数
while getopts "snah" opt; do
  case $opt in
    s)
      build_server=true
      ;;
    n)
      build_nuxt=true
      ;;
    a)
      build_admin=true
      ;;
    h)
      printUsage
      exit 1
      ;;
    \?)
      printUsage
      exit 1
      ;;
  esac
done

# 如果需要构建任一项目，则先停止相关服务
if $build_server || $build_nuxt || $build_admin; then
    systemctl stop bloglite-nuxt
    systemctl stop bloglite-server
    systemctl stop mysql
else
    echo "Did not specify any build target!"
    printUsage
fi

# 开始构建API服务器 Maven项目
if $build_server; then
    server_src_dir="./BlogLite-Server"
    cd "$server_src_dir"
    echo "Starting Maven build..."
    mvn clean install

    if [ $? -eq 0 ]; then
        echo "Maven build completed successfully."
        jarPkg=$(ls -l ./target | grep .jar | grep -v .jar.original | awk '{print $9}')
        cp ./target/$jarPkg ${server_deploy_dir}/${jarPkg}
    else
        echo "Maven build failed."
        exit 1
    fi

    cd -
fi

# 开始构建Nuxt项目
if $build_nuxt; then
    nuxt_src_dir="./BlogLite-Nuxt"
    cd "$nuxt_src_dir"
    echo "Starting Nuxt build..."
    $pm ci
    $pm run build

    if [ $? -eq 0 ]; then
        echo "Nuxt build completed successfully."
        cp -r ./.output/** $nuxt_deploy_dir
    else
        echo "Nuxt build failed."
        exit 1
    fi

    cd -
fi

# 开始构建管理员端项目
if $build_admin; then
    admin_src_dir="./BlogLite-Admin"
    cd "$admin_src_dir"
    echo "Starting Admin build..."
    $pm install
    $pm run build

    if [ $? -eq 0 ]; then
        echo "Admin build completed successfully."
        cp -r ./dist/** $admin_dist_dir
        nginx -s reload
    else
        echo "Admin build failed."
        exit 1
    fi

    cd -
fi

# 如果执行了任何构建，重新启动服务
if $build_server || $build_nuxt || $build_admin; then
    systemctl start mysql
    systemctl start bloglite-server
    systemctl start bloglite-nuxt
fi

echo "Build script execution completed."