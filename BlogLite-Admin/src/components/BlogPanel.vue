<script>
import {editBlog, getBlogByID, pushNewBlog} from "@/fetch/BlogAPI";
import {ElMessage} from "element-plus";
import router from "@/router";
import {makeRequest} from "@/fetch/FetchTemplate";
export default {
    created() {
        if(this.$route.params.blog_id)
            this.getBlog(this.$route.params.blog_id)
    },
    methods:{
        getBlog(blogID){
            getBlogByID(blogID)
                .then(blogInfo => {
                    if(blogInfo){
                        this.blogInfo = blogInfo.blog
                    }else{
                        ElMessage.error('无法加载该文章')
                    }
                })
        },
        handleSubmit(){
            if(this.$route.params.blog_id){
                editBlog(this.$route.params.blog_id, this.blogInfo)
                    .then(result => {
                        if(result){
                            ElMessage.success('修改成功！')
                            router.push('/blog/list')
                        }else{
                            ElMessage.error('修改文章时出现错误')
                        }
                    })
            }
            else{
                pushNewBlog(this.blogInfo)
                    .then(result => {
                        if (result) {
                            ElMessage.success('发布成功！')
                            //router.push('/blog/list')
                        } else {
                            ElMessage.error('发布文章时出现错误')
                        }
                    })
            }
        },
        showTagInput(){
            this.tagInputVisible = true
        },
        handleClose(tagName){
            this.blogInfo.tagNames.splice(this.blogInfo.tagNames.indexOf(tagName), 1)
        },
        handleTagInputConfirm(){
            if(this.tagInputValue.length > 0)
                this.blogInfo.tagNames.push(this.tagInputValue)
            console.log(this.blogInfo.tagNames)
            this.tagInputValue = ""
            this.tagInputVisible = false
        },
        $imgAdd(pos, $file){
            const formData = new FormData();
            formData.append('uploadImage', $file);
            makeRequest('/api/admin/img/upload', {
                method: 'PUT',
                body: formData
            }).then(response => {
                this.$refs.md.$img2Url(pos, response.body)
            })
        }
    },
    data(){
        return {
            blogInfo:{
                title: "",
                contentAbstract: "",
                content: "",
                tagNames: []
            },

            tagInputVisible: false,
            tagInputValue: ""
        }
    }
}
</script>

<template>
    <h2>写新文章</h2>
    <h3>标题</h3>
    <el-input v-model="blogInfo.title"></el-input>
    <h3>文章摘要</h3>
    <el-input v-model="blogInfo.contentAbstract"></el-input>
    <h3>文章正文</h3>
    <mavon-editor ref="md" v-model="blogInfo.content" @imgAdd="$imgAdd"></mavon-editor>

    <h3>添加标签</h3>
    <el-tag
            v-for="tag in blogInfo.tagNames"
            :key="tag"
            class="mx-1"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)"
    >
        {{ tag }}
    </el-tag>
    <el-input
            v-if="tagInputVisible"
            ref="InputRef"
            v-model="tagInputValue"
            class="ml-1 w-20"
            size="small"
            @keyup.enter="handleTagInputConfirm"
            @blur="handleTagInputConfirm"
    />
    <el-button v-else class="button-new-tag ml-1" size="small" @click="showTagInput">
        + New Tag
    </el-button>

    <el-form-item style="text-align: right;">
        <el-button type="primary" @click="handleSubmit">发布文章</el-button>
    </el-form-item>
</template>

<style>
</style>