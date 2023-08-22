<template>
    <el-container class="layout-container-demo" style="height: 1000px">
        <el-header style="text-align: right; font-size: 12px; height: 40px">
            <div class="toolbar">
                <el-dropdown>
                    <el-icon style="margin-right: 8px; margin-top: 1px"
                    ><setting
                    /></el-icon>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item>View</el-dropdown-item>
                            <el-dropdown-item>Add</el-dropdown-item>
                            <el-dropdown-item>Delete</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
                <span>Admin</span>
            </div>
        </el-header>

        <el-container>
            <el-aside width="200px">
                <el-scrollbar>
                    <el-menu :default-openeds="['1', '3']">
                        <el-sub-menu index="1">
                            <template #title>
                                <el-icon><message /></el-icon>内容管理
                            </template>
                            <router-link to="/blog/write">
                                <el-menu-item index="1-1">写文章</el-menu-item>
                            </router-link>
                            <router-link to="/blog/list">
                                <el-menu-item index="1-2">文章列表</el-menu-item>
                            </router-link>
                            <router-link to="/tag">
                                <el-menu-item index="1-3">标签管理</el-menu-item>
                            </router-link>
                            <router-link to="/collection">
                                <el-menu-item index="1-4">专栏管理</el-menu-item>
                            </router-link>
                        </el-sub-menu>
                    </el-menu>
                </el-scrollbar>
            </el-aside>

            <el-main>
                <el-scrollbar>
                    <router-view></router-view>
                </el-scrollbar>

            </el-main>
        </el-container>
    </el-container>
</template>

<script lang="ts" setup>
import { Menu as IconMenu, Message, Setting } from '@element-plus/icons-vue'


</script>

<style scoped>
.el-header{
    background: #79bbff;
}
.el-main{
    margin-left: 20px;
}
</style>