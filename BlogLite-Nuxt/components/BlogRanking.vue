<script setup lang="ts">

import type RestResponse from "~/model/RestResponse";
import type BlogPreview from "~/model/BlogPreview";
import {responseGuard} from "~/my-utils/response-guard";
import {routeToBlogDetail} from "~/my-utils/routing";


const {data: rankData} = await useFetch<RestResponse>('http://localhost:52480/api/statistic/rank');

const rankList = responseGuard<BlogPreview[]>(rankData.value);

</script>

<template>
  <div class="blog-ranking">
    <el-text class="rank-title" size="large" tag="b">文章排行</el-text>
    <el-divider style="margin: 0 0 5px"/>
    <div class="blog-ranking-wrapper">
      <div class="blog-rank-info" v-for="rankInfo in rankList" :key="rankInfo.id">
        <el-text class="blog-ranking-title" truncated @click="routeToBlogDetail(rankInfo.id)">
          {{ rankInfo.title }}
        </el-text>
      </div>
    </div>
  </div>
</template>

<style scoped>
.blog-ranking {
    background-color: white;
    margin-top: 20px;
    border: 1px solid var(--el-border-color);;
}

.rank-title {
    display: flex;
    justify-content: center;
    padding-top: 10px;
    padding-bottom: 10px;
    background-color: rgba(0, 0, 0, 0.05);
}

.blog-ranking-wrapper {
    padding-left: 5%;
    padding-right: 5%;
    margin-bottom: 10px;
}

.blog-ranking-title:hover {
    color: #79bbff;
    cursor: pointer;
}
</style>