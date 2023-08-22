<script setup>

</script>

<template>
  <ul class="blog-stream">
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

      <el-divider class="title-content-divider" border-style="dashed"/>
      <div class="blog-content" @click="routeToBlogDetail(blog.id)">
        {{ blog.contentAbstract }}
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
</template>

<script>
import router from "@/router";
import {getAllBlogsByCollectionID} from "@/fetch/BlogCollectionAPI";

export default {
    created() {
        if(this.$route.params.collection_id)
            getAllBlogsByCollectionID(this.$route.params.collection_id)
                .then(blogs => {
                    this.blogs = blogs
                })
    },
    methods: {
        routeToBlogDetail(blogID){
            router.push(`/blog/${blogID}`)
        }
    },
    data(){
        return{
            blogs: []
        }
    }
}
</script>

<style scoped>
.blog-item {
    border: 1px solid var(--el-border-color);
    border-radius: 8px;
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
</style>