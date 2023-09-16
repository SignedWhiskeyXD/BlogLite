<script setup>
import 'element-plus/dist/index.css'
import { Picture as IconPicture } from '@element-plus/icons-vue'
</script>

<template>
  <div class="collection-title">
    <el-text v-if="queryParams.collectionID && queryParams.collectionName" size="large">
      浏览专栏【{{queryParams.collectionName}}】下的所有文章
    </el-text>
    <el-text v-else-if="queryParams.searchKeyword" size="large">
      "{{queryParams.searchKeyword}}" 的搜索结果
    </el-text>
  </div>
  <ul class="blog-stream" v-infinite-scroll="getMoreBlogs" :infinite-scroll-disabled="isScrollDisabled"
      infinite-scroll-delay="500">
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
      <div class="blog-content" @click="routeToBlogDetail(blog.id)">
        {{ blog.contentAbstract }}
      </div>
      <el-image class="blog-preview-image"
                :src="blog.previewImage">
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
  <div class="stream-loading" v-loading="streamLoading"/>
  <div class="at-bottom"  v-if="requestParams.nextRequestParam == null && this.$route.fullPath === '/'">
    作者是条懒狗，就写了这么多！
  </div>
</template>

<script>
import {getBlogStream, getBlogStreamInitiation} from "@/fetch/BlogStreamAPI";
import router from "@/router";
import {getAllBlogsByCollectionID} from "@/fetch/BlogCollectionAPI";
import {getBlogSearchResult} from "@/fetch/BlogSearchAPI";

export default {
    props:{
        queryParams: {
            collectionID: Number,
            collectionName: String,
            searchKeyword: String
        }
    },
    created() {
        this.judgeWhichContent();
    },
    methods: {
        judgeWhichContent(){
            if(this.queryParams.collectionID){
                this.initWithBlogCollection()
            }else if(this.queryParams.searchKeyword){
                this.initWithBlogSearchResult()
            }else{
                this.initWithBlogStream()
            }
        },
        initWithBlogStream(){
            this.blogs = [];
            getBlogStreamInitiation(this.requestParams.blogNumOnStart)
                .then(BlogStream => {
                    this.requestParams.nextRequestParam = BlogStream.nextRequestParam;
                    this.blogs = BlogStream.blogList
                    this.scrollDisabled = false
                })
        },
        initWithBlogCollection(){
            this.streamLoading = true
            this.blogs = [];
            getAllBlogsByCollectionID(this.queryParams.collectionID)
                .then(blogs => {
                    this.blogs = blogs;
                    this.streamLoading = false;
                })
        },
        initWithBlogSearchResult(){
            this.streamLoading = true;
            this.blogs = [];
            getBlogSearchResult(this.queryParams.searchKeyword)
                .then(resultList => {
                    this.blogs = resultList;
                    this.streamLoading = false;
                })
        },
        getMoreBlogs(){
            this.scrollDisabled = true
            this.streamLoading = true
            getBlogStream(this.requestParams.nextRequestParam, this.requestParams.blogNumPerRequest)
                .then(BlogStream => {
                    this.requestParams.nextRequestParam = BlogStream.nextRequestParam;
                    this.blogs = this.blogs.concat(BlogStream.blogList)
                    this.scrollDisabled = false
                    this.streamLoading = false
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
            streamLoading: false
        }
    },
    computed: {
        isScrollDisabled(){
            if(this.$route.fullPath !== '/') return true;
            return this.scrollDisabled || this.requestParams.nextRequestParam == null;
        },
    },
    watch: {
        queryParams(newVal, oldVal) {
            // 似乎JS没有什么好的比较两个对象相等的方式，大概只能用这种歪门邪道了
            if(JSON.stringify(newVal) !== JSON.stringify(oldVal))
                this.judgeWhichContent();
        }
    }
}
</script>

<style scoped>
.collection-title {
    display: flex;
    justify-content: center;
    margin-top: 10px;
}

.blog-item {
    border-radius: 20px;
    padding: 20px;
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