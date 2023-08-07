<script setup>
import 'element-plus/dist/index.css'
</script>

<template>
  <div class="blog-header">
      <el-row>
          <el-col :span="4">
              <div class="blog-header-title">BlogLite</div>
          </el-col>
          <el-col :span="20">
              <el-menu mode="horizontal" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
                       default-active="1">
                  <el-menu-item index="1">主页</el-menu-item>
              </el-menu>
          </el-col>
      </el-row>

  </div>
</template>

<style scoped>
.blog-header-title {
    align-items: center;
    font-size: 24px;
    margin: 10px;
    //background-color: cornflowerblue;
}
</style>