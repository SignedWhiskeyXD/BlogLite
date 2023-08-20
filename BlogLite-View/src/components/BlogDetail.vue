<script setup>

</script>

<template>
  <div class="blog-detail" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
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
    <div class="blog-content" v-html="blogDetail.contentHTML"/>
    <el-divider class="title-content-divider" border-style="dashed"/>
    <div class="blog-tags">
      <el-tag v-for="tag in blogDetail.tagNames" class="blog-tag"
              :key="tag" size="large">
        {{ tag }}
      </el-tag>
    </div>
  </div>
  <el-backtop :right="100" :bottom="100" />
</template>

<script>
import {getBlogDetail} from "@/fetch/BlogDetailAPI";

export default {
    created() {
        if(this.$route.params.blog_id)
            getBlogDetail(this.$route.params.blog_id)
                .then(blog => {
                    this.blogDetail = blog
                })
    },
    data(){
        return {
            blogDetail: {
                id: 0,
                title: "",
                publishTime: "",
                views: 0,
                contentHTML: "",
                tagNames: []
            }
        }
    }
}
</script>

<style scoped>
.blog-detail {
    border: 1px solid var(--el-border-color);
    padding: 20px;
    background-color: #ffffff;
    margin-top: 20px;
    margin-left: 40px;
    margin-right: 40px;
}

.blog-title {
    text-align: center;
}

.blog-content {
    margin-left: 20px;
}

.blog-content:deep(p){
    display: grid;
}

.blog-content:deep(img){
    max-width: 100%;
    justify-self: center;
}

.title-content-divider {
    scale: 90%;
    margin-top: 10px;
}

.blog-tag {
    margin-right: 10px;
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