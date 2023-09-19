<script setup>
import 'element-plus/dist/index.css'
</script>

<template>
  <div class="blog-header">
    <div class="blog-title">
      <a class="blog-title-text" href="https://github.com/SignedWhiskeyXD/BlogLite" target="_blank">
        BlogLite
      </a>
    </div>
    <el-menu mode="horizontal" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
             :default-active="menuDefaultActive" :router="true" class="header-menu w-mobile-no-display">
      <el-menu-item class="menu-item" v-for="menuItem in menuInfo" :index="menuItem.path">
        {{ menuItem.title }}
      </el-menu-item>
    </el-menu>
    <div class="search-bar w-mobile-no-display">
      <el-input class="search-bar-input" v-model="searchInput" :placeholder="'搜索本站内容'"/>
      <el-button class="search-bar-button" @click="routeToSearchResult">搜索</el-button>
    </div>
    <div class="w-mobile-show">
<!--      下拉菜单-->
    </div>
  </div>
</template>

<script>
import router from "@/router";

export default {
    data(){
        return{
            menuInfo: [
                {
                    title: '主页',
                    path: '/'
                },
                {
                    title: '专栏',
                    path: '/collection'
                }
            ],
            searchInput: ""
        }
    },
    methods: {
        routeToSearchResult(){
            router.push({path: '/', query: {search: this.searchInput}})
        }
    },
    computed:{
        menuDefaultActive(){
            return router.currentRoute.value.path
        }
    }
}
</script>

<style scoped>
.blog-title {
    padding-left: 3%;
}

.blog-title-text {
    align-items: center;
    font-size: 26px;
    color: #eee;
    margin: 10px;
    display: flex;
    justify-content: flex-start;
    text-decoration: none;
}

.blog-header {
    background-color: #545c64;
    display: flex;
    justify-content: space-between;
}

.header-menu {
    width: 800px;
}

.menu-item {
    min-width: 80px;
}

.search-bar {
    display: flex;
    padding-right: 1%;
    width: 400px;
}

.search-bar-input {
    align-self: center;
}

.search-bar-button {
    align-self: center;
}
</style>