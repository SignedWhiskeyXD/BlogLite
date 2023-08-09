<script setup>
import BlogStream from "@/components/BlogStream.vue";
import AboutMe from "@/components/AboutMe.vue";
import MainPageTags from "@/components/MainPageTags.vue";
</script>

<template>
  <el-row class="home-page">
      <el-col :span="5" :offset="1" class="blog-left">
          <AboutMe/>
      </el-col>
      <el-col :span="12" class="blog-middle">
          <BlogStream/>
      </el-col>
      <el-col :span="5" class="blog-right">
        <main-page-tags/>
      </el-col>
  </el-row>
</template>

<style scoped>
.home-page {
    background-color: lightcyan;
}
</style>