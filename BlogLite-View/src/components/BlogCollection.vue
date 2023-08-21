<script setup>

</script>

<template>
  <div class="collection-list-wrapper">
    <ul class="collection-list">
      <li v-for="collection in collectionList" class="collection-item">
        <img class="collection-img" :src="collection.image" alt="img"/>

        <div class="collection-info">
          <h3>{{ collection.collectionName }}</h3>
          <p>{{ collection.description }}</p>
          <p>
            <span class="collection-blogNum">文章数量：{{ collection.blogNum }}</span>
            <span class="collection-totalViews">总浏览数：{{ collection.totalViews }}</span>
          </p>
        </div>

        <div class="collection-tags"/>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
    created() {
        while(this.collectionList.length < 10)
            this.collectionList.push(this.collectionList.at(0))
    },
    data(){
        return{
            collectionList: [{
                image: "https://picx.zhimg.com/4b70deef7_l.jpg?source=d16d100b",
                collectionName: "专栏",
                description: "这是一篇专栏的描述，随便写点啥凑凑字数",
                blogNum: 114,
                totalViews: 514
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
    width: 1200px;
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