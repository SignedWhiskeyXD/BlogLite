<script setup lang="ts">
import type RestResponse from "~/model/RestResponse";
import {responseGuard} from "~/my-utils/response-guard";
import type BlogStream from "~/model/BlogStream";
import type BlogCard from "~/model/BlogCard";
import {Picture} from "@element-plus/icons-vue";

let requestParams = {
    blogNumOnStart: 5,
    blogNumPerRequest: 5,
    nextRequestParam: 0
};

const scrollDisabled = ref(true);

const blogs = ref<BlogCard[]>([]);

const noFetchingMore = computed<boolean>(() => requestParams.nextRequestParam == null || scrollDisabled.value);

await initBlogs();

async function initBlogs() {
    const {data: initStreamData} = await useFetch<RestResponse>(
        `http://localhost:52480/api/blogstream/init?initNum=${requestParams.blogNumOnStart}`
    );

    const initStream = responseGuard<BlogStream>(initStreamData.value);
    requestParams.nextRequestParam = initStream.nextRequestParam;
    blogs.value = initStream.blogList;

    scrollDisabled.value = false;
}

async function getMoreBlogs() {
    scrollDisabled.value = true;
    const {data: streamData} = await useFetch<RestResponse>('/api/blogstream', {
        query: {
            startID: requestParams.nextRequestParam,
            num: requestParams.blogNumPerRequest
        }
    });

    const newStream = responseGuard<BlogStream>(streamData.value);
    requestParams.nextRequestParam = newStream.nextRequestParam;
    blogs.value = blogs.value.concat(newStream.blogList);
    scrollDisabled.value = false;
}

</script>

<template>
<!--  <div class="collection-title">
    <el-text v-if="queryParams.collectionID && queryParams.collectionName" size="large">
      浏览专栏【{{queryParams.collectionName}}】下的所有文章
    </el-text>
    <el-text v-else-if="queryParams.searchKeyword" size="large">
      "{{queryParams.searchKeyword}}" 的搜索结果
    </el-text>
  </div>-->
  <ul class="blog-stream" v-infinite-scroll="getMoreBlogs" :infinite-scroll-disabled="noFetchingMore"
      infinite-scroll-delay="800" :infinite-scroll-distance="-200">
    <li v-for="blog in blogs" :key="blog.id"
        class="blog-item transition-shadow">
      <div class="blog-title">
        <h2>{{ blog.title }}</h2>
      </div>
      <el-row class="blog-date-views">
        <el-col :span="12" class="blog-date">
          <el-text>发布时间 {{ blog.publishTime }}</el-text>
        </el-col>
        <el-col :span="12" class="blog-views">
          <el-text>浏览次数 {{ blog.views }}</el-text>
        </el-col>
      </el-row>

      <el-divider class="title-content-divider"/>
      <a class="blog-content" :href="`/blog/${blog.id}`">
        {{ blog.contentAbstract }}
      </a>
      <el-image class="blog-preview-image"
                :src="blog.previewImage">
        <template #error>
          <el-icon class="image-error"><Picture/></el-icon>
        </template>
      </el-image>
      <el-divider class="title-content-divider" border-style="dashed"/>
      <div class="blog-tags">
        <el-tag v-for="tag in blog.tagNames" class="blog-tag"
                :key="tag" size="large">
          {{ tag }}
        </el-tag>
      </div>
    </li>
  </ul>

  <div class="at-bottom" v-if="noFetchingMore">
    作者是条懒狗，就写了这么多！
  </div>
  <el-skeleton class="blog-item transition-shadow"
               v-else :rows="6" animated />
</template>

<style scoped>
.collection-title {
    display: flex;
    justify-content: center;
    margin-top: 10px;
}

.blog-item {
    border-radius: 20px;
    padding: 10px 10px 8px;
    margin-bottom: 40px;
    background-color: #ffffff;
}

.transition-shadow {
    box-shadow: 0 5px 10px rgba(0,0,0,0.15);
    transition: box-shadow 0.3s ease-in-out;
}

.transition-shadow:hover {
    box-shadow: 0 20px 30px rgba(0,0,0,0.3);
}

.blog-title {
    text-align: center;
}

.blog-content {
    margin-left: 20px;
    text-decoration: none;
    color: rgba(0, 0, 0, 0.7);
}

.title-content-divider {
    scale: 90%;
    margin: 10px 0 10px;
}

.blog-tag {
    margin-right: 10px;
    margin-bottom: 10px ;
}

.blog-stream {
    list-style: none;
    padding-left: 0;
    margin: 10px 15px 0;
}

.blog-date {
    text-align: center;
    padding-left: 10%;
}

.blog-views {
    text-align: center;
    padding-right: 10%;
}

.blog-preview-image {
    width: 100%;
    margin-top: 10px;
    scale: 95%;
}

.image-error {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    min-height: 300px;
    background: var(--el-fill-color-light);
    color: var(--el-text-color-secondary);
}

@media screen and (min-width: 1600px) {
    .image-error {
        min-height: 480px;
    }
}

.blog-tags {
    padding: 0 10px 0;
}

.at-bottom {
    text-align: center;
    margin-bottom: 30px;
}
</style>