<script>
import {pushNewBlog} from "@/fetch/BlogAPI";
import {ElMessage} from "element-plus";
import router from "@/router";

export default {
    methods:{
        handleSubmit(){
            pushNewBlog(this.title, this.markdownText)
                .then(result => {
                    if(result){
                        ElMessage.success('发布成功！')
                        //router.push('/blog/list')
                    }else{
                        ElMessage.error('发布文章时出现错误')
                    }
                })
        }
    },
    data(){
        return {
            title: "",
            markdownText: ""
        }
    }
}
</script>

<template>
    <h2>写新文章</h2>
    <h3>标题</h3>
    <el-input v-model="title"></el-input>
    <h3>文章正文</h3>
    <mavon-editor v-model="markdownText"></mavon-editor>
    <el-form-item style="text-align: right;">
        <el-button type="primary" @click="handleSubmit">发布文章</el-button>
    </el-form-item>
</template>

<style>
</style>