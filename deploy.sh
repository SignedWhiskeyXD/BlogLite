#!/bin/bash
pm="cnpm"

# 构建过程比较消耗内存，因此应当先行停止运行的服务
systemctl stop bloglite-nuxt
systemctl stop bloglite-server
systemctl stop mysql

# 开始构建API服务器
server_src_dir="./BlogLite-Server"
server_deploy_dir="/root/bloglite-deployment"
cd "$server_src_dir"

# 执行 Maven 构建
echo "Starting Maven build..."
mvn clean install

if [ $? -eq 0 ]; then
    echo "Maven build completed successfully."
else
    echo "Maven build failed."
    exit 1
fi

# 拷贝构建好的jar包
jarPkg=$(ls -l ./target | grep .jar | grep -v .jar.original | awk '{print $9}')

cp ./target/$jarPkg ${server_deploy_dir}/${jarPkg}

cd -

# 开始构建Nuxt服务
nuxt_src_dir="./BlogLite-Nuxt"
nuxt_deploy_dir="/root/bloglite-deployment/bloglite-nuxt"
cd "$nuxt_src_dir"

# 构建
echo "Starting Nuxt build..."
$pm ci
$pm run build

if [ $? -eq 0 ]; then
    echo "Nuxt build completed successfully."
else
    echo "Nuxt build failed."
    exit 1
fi

# 拷贝Nuxt构建好的产物
cp -r ./.output/** $nuxt_deploy_dir

cd -

# 开始构建管理端
admin_src_dir="./BlogLite-Admin"
admin_dist_dir="/var/www/bloglite-admin"
cd "$admin_src_dir"

# 构建
echo "Starting Admin build..."
$pm install
$pm run build

if [ $? -eq 0 ]; then
    echo "Admin build completed successfully."
else
    echo "Admin build failed."
    exit 1
fi

cp -r ./dist/** $admin_dist_dir
nginx -s reload

# 重新启动服务
systemctl start mysql
systemctl start bloglite-server
systemctl start bloglite-nuxt

echo "Build script execution completed."