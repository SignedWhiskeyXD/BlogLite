<script setup>

</script>

<template>
  <div class="blog-ranking">
    <el-text class="rank-title" size="large" tag="b">文章排行</el-text>
    <el-divider style="scale: 90%; margin: 10px"/>
    <div class="blog-ranking-wrapper">
      <div class="blog-rank-info" v-for="rankInfo in rankList" :key="rankInfo.id">
        <el-text class="blog-ranking-title" truncated @click="routeToBlogDetail(rankInfo.id)">
          {{ rankInfo.title }}
        </el-text>
      </div>
    </div>

  </div>
</template>

<script>
import {getBlogRanking} from "@/fetch/SiteInfoAPI";
import router from "@/router";

export default {
    created() {
        getBlogRanking()
            .then(list => {
                this.rankList = list;
            })
    },
    methods: {
        routeToBlogDetail(blogID){
            router.push(`/blog/${blogID}`)
        },
    },
    data() {
        return {
            rankList: []
        }
    }
}
</script>

<style scoped>
.blog-ranking {
    background-color: white;
    margin-top: 20px;
    border: 1px solid var(--el-border-color);;
}

.rank-title {
    display: flex;
    justify-content: center;
    margin-top: 10px;
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