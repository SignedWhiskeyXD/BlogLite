<script setup>
import BlogStream from "@/components/BlogStream.vue";
</script>

<template>
  <el-row gutter="20">
      <el-col :span="5" :offset="1" class="blog-left">左边</el-col>
      <el-col :span="12" class="blog-middle">
          <BlogStream/>
      </el-col>
      <el-col :span="5" class="blog-right">右边</el-col>
  </el-row>
</template>

<style scoped>
.blog-left {
    background-color: cornflowerblue;

}

.blog-middle {

}

.blog-right {
    background-color: aquamarine;
}
</style>