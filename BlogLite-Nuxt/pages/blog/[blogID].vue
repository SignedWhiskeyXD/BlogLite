<script setup lang="ts">
import type BlogDetail from "~/model/BlogDetail";
import type RestResponse from "~/model/RestResponse";
import {responseGuard} from "~/my-utils/response-guard";
import 'github-markdown-css/github-markdown.css'

const blogID = getBlogIDFromRoute();

const {data: blogData} = await useFetch<RestResponse>(`http://localhost:52480/api/blog/${blogID}`);

const blogDetail = responseGuard<BlogDetail>(blogData.value);

function getBlogIDFromRoute(): number {
    const paramError = createError({
        statusCode: 404,
        statusMessage: 'Blog ID Param Error!'
    });
    const id = useRoute().params.blogID;
    if(Array.isArray(id))
        throw paramError;

    const ret = parseInt(id as string);
    if(isNaN(ret))
        throw paramError;

    return ret;
}

</script>

<template>
  <div class="blog-detail-wrapper">
    <div class="blog-detail-wrapper-secondary">
      <main class="blog-detail" :style="{boxShadow: `var(--el-box-shadow-dark)`}" ref="blogDetailSection">
        <div>
          <div class="blog-title">
            <h2>{{ blogDetail.title }}</h2>
          </div>
          <el-row class="blog-date-views">
            <el-col :span="12" class="blog-date">
              <el-text>发布时间 {{ blogDetail.publishTime }}</el-text>
            </el-col>
            <el-col :span="12" class="blog-views">
              <el-text>浏览次数 {{ blogDetail.views }}</el-text>
            </el-col>
          </el-row>

          <el-divider class="title-content-divider" border-style="dashed"/>
          <div class="blog-content markdown-body" v-html="blogDetail.contentHTML"/>
          <el-divider class="title-content-divider" border-style="dashed"/>

          <BlogComment :blog-id="blogID"/>

        </div>
      </main>
      <aside class="blog-detail-sidebar w-mobile-no-display">
        <div class="blog-tags" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
          <h4 style="text-align: center">文章标签</h4>
          <el-divider style="margin-bottom: 10px"/>
          <el-tag v-for="tag in blogDetail.tagNames" class="blog-tag"
                  :key="tag" size="large">
            {{ tag }}
          </el-tag>
          <el-text v-if="!(blogDetail.tagNames.length > 0)" size="large">
            这篇文章没有标签诶
          </el-text>
        </div>
      </aside>
    </div>
    <client-only>
      <div class="fixed-buttons">
        <div class="button-to-top" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
          <el-icon style="scale: 200%"><ArrowUpBold /></el-icon>
        </div>
        <div class="button-to-comment" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
          <el-icon style="scale: 200%"><ChatLineSquare /></el-icon>
        </div>
      </div>
    </client-only>
  </div>
</template>

<style scoped>
.blog-detail-wrapper {
    display: flex;
    justify-content: center;
}

@media screen and (max-width: 768px) {
    .blog-detail-wrapper {
        justify-content: flex-start;
    }
}

.blog-detail-wrapper-secondary {
    width: 1280px;
    display: flex;
}

.blog-detail  {
    border: 1px solid var(--el-border-color);
    padding: 20px;
    background-color: #ffffff;
    margin: 20px 15px 30px;
    width: 75%;
    min-width: 400px;
    flex-grow: 1;
}

.blog-detail-sidebar {
    width: 25%;
}

.blog-tags {
    border: 1px solid var(--el-border-color);
    background-color: #ffffff;
    padding: 5px 20px 20px;
    margin-top: 20px;
}

.blog-title {
    text-align: center;
}

.blog-content {
    margin-left: 20px;
}

.blog-content:deep(img){
    max-width: 100%;
    justify-self: center;
}

.blog-content:deep(code){
    padding: 0;
}

.title-content-divider {
    scale: 90%;
    margin-top: 10px;
}

.blog-tag {
    margin-right: 10px;
    margin-bottom: 8px;
}

.blog-date {
    text-align: center;
    padding-left: 10%;
}

.blog-views {
    text-align: center;
    padding-right: 10%;
}

.fixed-buttons {
    position: fixed;
    bottom: 40px;
    right: 40px;
}

.button-to-comment {
    background-color: white;
    width: 60px;
    height: 60px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.button-to-comment:hover {
    cursor: pointer;
}

.button-to-top {
    background-color: white;
    width: 60px;
    height: 60px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 20px;
}

.button-to-top:hover {
    cursor: pointer;
}
</style>