<script setup>

</script>

<template>
  <div class="collection-list-wrapper">
    <ul class="collection-list">
      <li v-for="collection in collectionList" class="collection-item"
          @click="routeToBlogList(collection.id, collection.collectionName)">
        <img class="collection-img" :src="collection.imageLink" alt="img"/>

        <div class="collection-info">
          <h3>{{ collection.collectionName }}</h3>
          <p>{{ collection.description }}</p>
          <p>
            <span class="collection-blogNum">文章数量：{{ collection.blogNum }}</span>
            <span class="collection-totalViews">总浏览数：{{ collection.totalViews }}</span>
          </p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import {getAllBlogCollections} from "@/fetch/BlogCollectionAPI";
import router from "@/router";

export default {
    created() {
        getAllBlogCollections()
            .then(collectionInfo => {
                this.collectionList = collectionInfo
            })
    },
    methods: {
        routeToBlogList(collectionID, collectionName){
            router.push({path: '/', query: {collectionID, collectionName}})
        }
    },
    data(){
        return{
            collectionList: [{
                id: 0,
                imageLink: "",
                collectionName: "",
                description: "",
                blogNum: 0,
                totalViews: 0
            }]
        }
    }
}
</script>

<style scoped>
.collection-list {
    list-style: none;
}

.collection-item {
    border: 1px solid var(--el-border-color);
    width: 1000px;
    background-color: white;
    margin-top: 20px;
    padding: 10px;
    display: flex;
    flex-direction: row;
}

.collection-item:hover {
    cursor: pointer;
}

.collection-list-wrapper {
    display: flex;
    justify-content: center;
}

.collection-info {
    margin-left: 20px;
}

.collection-img {
    height: 140px;
    width: 140px;
}

.collection-totalViews {
    margin-left: 50px;
}
</style>