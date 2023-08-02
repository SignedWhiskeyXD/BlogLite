<script>
import {Location, Document, Setting} from "@element-plus/icons-vue";

export default {
    components: {Document, Setting, Location},
    methods:{
        handleOpen(key, keyPath){
            console.log(key, keyPath)
        },
        handleClose(key, keyPath){
            console.log(key, keyPath)
        }
    }
}
</script>

<template>
    <h2>管理面板</h2>
    <router-link to="/blog/list">文章管理</router-link>
    <router-link to="/blog/write">写文章</router-link>
    <router-link to="/tag">标签管理</router-link>
    <router-link to="/layout">测试布局</router-link>
    <div class="admin-panel">
        <router-view></router-view>
    </div>

</template>

<style scoped>
.sideMenu{
    height: 100%;
    width: 200px;
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
}
.admin-panel{
    flex: 1;
}
</style>