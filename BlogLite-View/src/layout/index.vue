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
          <keep-alive>
            <component :is="Component"/>
          </keep-alive>
        </router-view>
      </el-main>
  </div>
</template>

<script>
import {makeRequest} from "@/fetch/FetchTemplate";

export default {
    created() {
        makeRequest('/api/statistic/siteUV', {
            method: 'POST'
        });
    }
}
</script>

<style scoped>
.blog-header {
    padding: 0;
}

.blog-main {
    padding: 0;
}
</style>