<script setup>
import BlogHeader from './Header.vue'
import HomePage from "@/layout/HomePage.vue";
</script>

<template>
  <div class="common-layout">
      <el-header class="blog-header">
          <BlogHeader/>
      </el-header>
      <el-main class="blog-main">
          <HomePage/>
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