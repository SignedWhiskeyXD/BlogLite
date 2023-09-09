<script setup>
import 'element-plus/dist/index.css'
import { Picture as IconPicture } from '@element-plus/icons-vue'
</script>

<template>
  <ul class="blog-stream" v-infinite-scroll="getMoreBlogs" :infinite-scroll-disabled="isScrollDisabled"
      infinite-scroll-delay="500">
    <li v-for="blog in blogs" :key="blog.id"
        class="blog-item" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
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
      <div class="blog-content" @click="routeToBlogDetail(blog.id)">
        {{ blog.contentAbstract }}
      </div>
      <el-image class="blog-preview-image"
                :src="blog.previewImage"
                :preview-src-list="blog.previewImage">
        <template #error>
          <el-icon class="image-error"><IconPicture/></el-icon>
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
  <div class="at-bottom"  v-if="requestParams.nextRequestParam == null">
    作者是条懒狗，就写了这么多！
  </div>
</template>

<script>
import {getBlogStream, getBlogStreamInitiation} from "@/fetch/BlogStreamAPI";
import router from "@/router";

export default {
    created() {
        getBlogStreamInitiation(this.requestParams.blogNumOnStart)
            .then(BlogStream => {
                this.requestParams.nextRequestParam = BlogStream.nextRequestParam;
                this.blogs = BlogStream.blogList
                this.scrollDisabled = false
            })
    },
    methods: {
        getMoreBlogs(){
            this.scrollDisabled = true
            getBlogStream(this.requestParams.nextRequestParam, this.requestParams.blogNumPerRequest)
                .then(BlogStream => {
                    this.requestParams.nextRequestParam = BlogStream.nextRequestParam;
                    this.blogs = this.blogs.concat(BlogStream.blogList)
                    this.scrollDisabled = false
                })
        },
        routeToBlogDetail(blogID){
            router.push(`/blog/${blogID}`)
        },
    },
    data() {
        return {
            requestParams: {
                blogNumOnStart: 15,
                blogNumPerRequest: 10,
                nextRequestParam: 0
            },
            blogs: [],
            scrollDisabled: true,
            scrollBarPos: 0
        }
    },
    computed: {
        isScrollDisabled(){
            if(this.$route.path !== '/') return true;
            return this.scrollDisabled || this.requestParams.nextRequestParam == null;
        }
    }
}
</script>

<style scoped>
.blog-item {
    //border: 1px solid var(--el-border-color);
    border-radius: 20px;
    padding: 20px;
    margin-bottom: 40px;
    background-color: #ffffff;
}

.blog-title {
    text-align: center;
}

.blog-content {
    margin-left: 20px;
}

.blog-content:hover {
    cursor: pointer;
}

.title-content-divider {
    scale: 90%;
    margin-top: 10px;
}

.blog-tag {
    margin-right: 10px;
}

.blog-stream {
    list-style: none;
    padding-left: 0;
    margin-left: 40px;
    margin-right: 40px;
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
    margin-top: 20px;
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

.at-bottom {
    text-align: center;
    margin-bottom: 30px;
}
</style>