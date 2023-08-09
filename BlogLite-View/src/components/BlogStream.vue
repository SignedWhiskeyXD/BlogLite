<script setup>
import 'element-plus/dist/index.css'
</script>

<template>
  <el-scrollbar max-height="95vh">
    <ul class="blog-stream" v-infinite-scroll="getMoreBlogs" :infinite-scroll-disabled="isScrollDisabled"
        infinite-scroll-delay="500">
      <li v-for="blog in blogs" :key="blog.id"
          class="blog-item" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
        <div class="blog-title">
          <h2>{{ blog.title }}</h2>
        </div>
        <el-divider class="title-content-divider" border-style="dashed"/>
        <div class="blog-content" v-html="blog.contentHTML">
        </div>
        <el-divider class="title-content-divider" border-style="dashed"/>
        <div class="blog-tags">
          <el-tag v-for="tag in blog.tagNames" class="blog-tag"
                  :key="tag" size="large">
            {{ tag }}
          </el-tag>
        </div>
      </li>
    </ul>
  </el-scrollbar>
</template>

<script>
import {getBlogStream, getBlogStreamInitiation} from "@/fetch/BlogStreamAPI";

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
        }
    },
    data() {
        return {
            requestParams: {
                blogNumOnStart: 15,
                blogNumPerRequest: 10,
                nextRequestParam: 0
            },
            blogs: [],
            scrollDisabled: true
        }
    },
    computed: {
        isScrollDisabled(){
            return this.scrollDisabled && this.requestParams.nextRequestParam != null;
        }
    }
}
</script>

<style scoped>
.blog-item {
    border: 1px solid var(--el-border-color);
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

.title-content-divider {
    scale: 90%;
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
</style>