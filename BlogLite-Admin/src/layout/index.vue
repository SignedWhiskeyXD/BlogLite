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
                    <el-menu :default-openeds="['1', '3']" :router="true">
                        <el-menu-item index="/dashboard" style="justify-content: center">
                            站点统计
                        </el-menu-item>
                        <el-sub-menu index="2" >
                            <template #title>
                                <el-icon><Message /></el-icon>内容管理
                            </template>
                            <el-menu-item class="menu-item" v-for="menuItem in contentMenu" :index="menuItem.path">
                                {{ menuItem.title }}
                            </el-menu-item>
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

<script>
import { Menu as IconMenu, Message, Setting } from '@element-plus/icons-vue'

export default {
    data(){
        return {
            contentMenu: [
                {
                    title: '写文章',
                    path: '/blog/write'
                },
                {
                    title: '文章列表',
                    path: '/blog/list'
                },
                {
                    title: '标签管理',
                    path: '/tag'
                },
                {
                    title: '专栏管理',
                    path: '/collection'
                },
                {
                    title: '评论管理',
                    path: '/comment'
                },
            ]
        }
    }
}

</script>

<style scoped>
.el-header{
    background: #79bbff;
}
.el-main{
    margin-left: 20px;
}
</style>