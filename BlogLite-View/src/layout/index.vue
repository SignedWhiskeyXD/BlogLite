<script setup>
import BlogHeader from './Header.vue'
</script>

<template>
  <div class="common-layout">
      <el-header class="blog-header">
          <BlogHeader/>
      </el-header>
      <el-main class="blog-main">
        <router-view v-slot="{ Component }">
          <keep-alive include="HomePage">
            <component :is="Component"/>
          </keep-alive>
        </router-view>
      </el-main>
  </div>
</template>

<style scoped>
.blog-header {
    padding: 0;
}

.blog-main {
    padding: 0;
}
</style>