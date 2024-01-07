<script setup lang="ts">
import type SiteInfo from "~/model/SiteInfo";
import type RestResponse from "~/model/RestResponse";
import {responseGuard} from "~/my-utils/response-guard";

const {data: siteInfoData} = await useFetch<RestResponse>('http://localhost:52480/api/statistic');

const siteInfo = responseGuard<SiteInfo>(siteInfoData.value);

</script>

<template>
  <div class="about-me">
    <el-row class="about-me-title center">
      <el-text size="large">WhiskeyXD的个人博客</el-text>
    </el-row>
    <el-row class="my-avatar center">
      <el-avatar size="large" src="https://avatars.githubusercontent.com/u/108565494"/>
    </el-row>
    <el-divider class="about-me-divider"/>
    <el-row class="statistic center">
      <el-col :span="12" class="blog-count">
        <el-text>文章总数</el-text>
        <div>
          <el-text size="large" tag="b">{{ siteInfo.totalBlogs }}</el-text>
        </div>
      </el-col>
      <el-col :span="12" class="thumb-ups">
        <el-text>浏览量</el-text>
        <div>
          <el-text size="large" tag="b">{{ siteInfo.totalViews }}</el-text>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.about-me{
    border: 1px solid var(--el-border-color);
    background-color: white;
    margin-top: 20px;
}

.about-me-title {
    margin-top: 18px;
}

.statistic {
    margin-bottom: 20px;
}

.center {
    justify-content: center;
}

.my-avatar {
    margin: 20px;
}

.about-me-divider {
    scale: 80%;
}

.blog-count {
    text-align: center;
    padding-left: 10%;
}

.thumb-ups {
    text-align: center;
    padding-right: 10%;
}
</style>