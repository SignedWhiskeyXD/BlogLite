<script setup>

import {getRandomColor} from "@/utils/RandomColor";
</script>

<template>
  <div class="main-page-tags">
    <el-text class="tag-title" size="large" tag="b">所有标签</el-text>
    <el-divider style="scale: 90%; margin: 10px"/>
    <div class="main-page-tags-container">
      <el-tag v-for="tag in allTags" :key="tag.id" class="main-page-tag" :color="getRandomColor()">
        {{ tag.tagName }}
      </el-tag>
    </div>
  </div>
</template>

<script>
import {getAllBlogTags} from "@/fetch/BlogTagAPI";

export default {
    created() {
        getAllBlogTags()
            .then(tagList => {
                this.allTags = tagList
            })
    },
    data(){
        return{
            allTags: []
        }
    }
}
</script>

<style scoped>
.main-page-tags {
    background-color: white;
    margin-top: 20px;
    border: 1px solid var(--el-border-color);;
}

.main-page-tag {
    margin-left: 10px;
    margin-bottom: 8px;
    color: white;
}

.main-page-tags-container {
    margin: 5px;
}

.tag-title {
    display: flex;
    justify-content: center;
    margin-top: 10px;
}
</style>