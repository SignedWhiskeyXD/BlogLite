<template>
    <h2>写新文章</h2>
    <input type="file" @change="handleFileUpload" accept=".md">
    <el-row gutter="20">
        <el-col :span="12">
            <h3>标题</h3>
            <el-input v-model="blogInfo.title"></el-input>
        </el-col>
        <el-col :span="12">
            <h3>首图链接</h3>
            <el-input v-model="blogInfo.previewImage"></el-input>
        </el-col>
    </el-row>

    <h3>文章摘要</h3>
    <el-input v-model="blogInfo.contentAbstract"></el-input>
    <h3>文章正文</h3>
    <mavon-editor ref="md" v-model="blogInfo.content" class="md-editor"></mavon-editor>

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

    <h3>添加文章至专栏</h3>

    <div class="chosen-collections">
        <el-tag
          v-for="collectionName in blogInfo.collections"
          :key="collectionName"
          class="mx-1"
          type="success"
          closable
          @close="handleCloseCollectionTag(collectionName)"
          :disable-transitions="false"
        >
            {{ collectionName }}
        </el-tag>
    </div>

    <el-select placeholder="选择一个专栏" size="large"
               v-model="currentSelectedCollectionName" @change="handleSelectorChange">
        <el-option
          v-for="collection in availableCollections"
          :key="collection.id"
          :value="collection.collectionName"
        />
    </el-select>

    <el-form-item style="text-align: right;">
        <el-button type="primary" @click="handleSubmit">发布文章</el-button>
    </el-form-item>
</template>

<script>
import {editBlog, getBlogByID, pushNewBlog} from "@/fetch/BlogAPI";
import {ElMessage} from "element-plus";
import router from "@/router";
import {makeRequest} from "@/fetch/FetchTemplate";
import {getAllBlogCollections} from "@/fetch/BlogCollectionAPI";
export default {
    created() {
        getAllBlogCollections()
          .then(collections => {
              this.availableCollections = collections
          })

        if(this.$route.params.blog_id)
            this.getBlog(this.$route.params.blog_id)
    },
    methods:{
        getBlog(blogID){
            getBlogByID(blogID)
                .then(blogInfo => {
                    if(blogInfo){
                        this.blogInfo = blogInfo.blog
                        this.blogInfo.tagNames = blogInfo.tagNames
                        this.blogInfo.collections = blogInfo.collections
                    }else{
                        ElMessage.error('无法加载该文章')
                    }
                })
        },
        handleFileUpload(event){
            const file = event.target.files[0];
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();

                if (fileExtension === 'md') {
                    const reader = new FileReader();
                    reader.onload = () => {
                        this.blogInfo.title = fileName.split('.')[0];
                        this.blogInfo.content = reader.result;
                    };

                    reader.readAsText(file);
                } else {
                    ElMessage.error('请选择一个Markdown文件（.md）');
                }
            }
        },
        handleSubmit(){
            const blogUploadInfo = {
                title: this.blogInfo.title,
                contentAbstract: this.blogInfo.contentAbstract,
                content: this.blogInfo.content,
                previewImage: this.blogInfo.previewImage,
                tagNames: this.blogInfo.tagNames,
                collections: this.blogInfo.collections
            }

            if(this.$route.params.blog_id){
                editBlog(this.$route.params.blog_id, blogUploadInfo)
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
                pushNewBlog(blogUploadInfo)
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
        handleCloseCollectionTag(tagName){
            this.blogInfo.collections.splice(this.blogInfo.collections.indexOf(tagName), 1)
        },
        handleTagInputConfirm(){
            if(this.tagInputValue.length > 0)
                this.blogInfo.tagNames.push(this.tagInputValue)
            this.tagInputValue = ""
            this.tagInputVisible = false
        },
        handleSelectorChange(){
            const collectionName = this.currentSelectedCollectionName
            if(this.blogInfo.collections.includes(collectionName) === false) {
                this.blogInfo.collections.push(collectionName)

            }
            this.currentSelectedCollectionName = ""
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
                id: 0,
                title: "",
                previewImage: "",
                contentAbstract: "",
                content: "",
                views: 0,
                publishTime: "",

                tagNames: [],
                collections: []
            },
            tagInputVisible: false,
            tagInputValue: "",

            availableCollections: [],
            currentSelectedCollectionName: ""
        }
    }
}
</script>

<style>
.md-editor {
    max-height: 1000px;
}
</style>