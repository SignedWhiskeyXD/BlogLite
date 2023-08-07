<script setup>
import BlogHeader from './Header.vue'
import HomePage from "@/layout/HomePage.vue";
</script>

<template>
  <div class="common-layout">
      <el-header>
          <BlogHeader/>
      </el-header>
      <el-main>
          <HomePage/>
      </el-main>
  </div>
</template>

<style scoped>

</style>