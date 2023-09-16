<script setup>
import 'element-plus/dist/index.css'
import {Postcard} from "@element-plus/icons-vue";
</script>

<template>
  <div class="blog-header">
    <el-row>
      <el-col :span="3" class="blog-title">
        <a class="blog-title-text" href="https://github.com/SignedWhiskeyXD/BlogLite" target="_blank">
          BlogLite
        </a>
      </el-col>
      <el-col :span="16">
        <el-menu mode="horizontal" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
                 :default-active="menuDefaultActive" :router="true">
          <el-menu-item class="menu-item" v-for="menuItem in menuInfo" :index="menuItem.path">
            {{ menuItem.title }}
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="5" class="search-bar">
        <el-input class="search-bar-input" v-model="searchInput" :placeholder="'搜索本站内容'"/>
        <el-button class="search-bar-button" @click="routeToSearchResult">搜索</el-button>
      </el-col>
    </el-row>

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
.blog-title-text {
    align-items: center;
    font-size: 26px;
    color: #eee;
    margin: 10px;
    display: flex;
    justify-content: center;
    text-decoration: none;
}

.blog-header {
    background-color: #545c64;
}

.menu-item {
    min-width: 80px;
}

.search-bar {
    display: flex;
}

.search-bar-input {
    align-self: center;
}

.search-bar-button {
    align-self: center;
}
</style>