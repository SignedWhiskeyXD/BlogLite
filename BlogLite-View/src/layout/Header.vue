<script setup>
import 'element-plus/dist/index.css'
import {Postcard} from "@element-plus/icons-vue";
</script>

<template>
  <div class="blog-header">
    <el-row>
      <el-col :span="4" class="blog-title">
        <a class="blog-title-text" href="https://github.com/SignedWhiskeyXD/BlogLite" target="_blank">
          BlogLite
        </a>
      </el-col>
      <el-col :span="20">
        <el-menu mode="horizontal" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
                 :default-active="menuDefaultActive" :router="true">
          <el-menu-item class="menu-item" v-for="menuItem in menuInfo" :index="menuItem.path">
            {{ menuItem.title }}
          </el-menu-item>
        </el-menu>
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
            ]
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

.blog-title {
    background-color: #545c64;
}

.menu-item {
    min-width: 80px;
}
</style>