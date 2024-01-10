<script setup lang="ts">

</script>

<template>
  <div class="common-layout">
    <el-header class="blog-header">
      <Header/>
    </el-header>
    <el-main class="blog-main">
      <slot/>
    </el-main>
    <div class="icp-info">
      <a href="https://beian.miit.gov.cn/" target="_blank"
         style="text-decoration: none; color: rgba(0, 0, 0, 0.4);">
        鄂ICP备2023017016号
      </a>
    </div>
  </div>
</template>

<style scoped>
.blog-header {
    position: relative;
    width: 100%;
    z-index: 1919;
}

.blog-main {
    padding: 0;
    height: calc(100vh - 60px);
    overflow-x: hidden;
}

.icp-info {
    position: fixed;
    right: 20px;
    bottom: 5px;
}
</style>