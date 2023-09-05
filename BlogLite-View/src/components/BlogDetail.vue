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
    <div class="blog-content markdown-body" v-html="blogDetail.contentHTML"/>
    <el-divider class="title-content-divider" border-style="dashed"/>
    <div class="blog-tags">
      <el-tag v-for="tag in blogDetail.tagNames" class="blog-tag"
              :key="tag" size="large">
        {{ tag }}
      </el-tag>
    </div>

    <h3>{{ commentPageInfo.total }}条评论</h3>

    <div class="blog-comment-page">
      <div v-for="comment in commentPageInfo.list" :key="comment.id" class="comment-item">
        <el-avatar :size="50" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"/>
        <div class="comment-wrapper">
          <div class="comment-identity">
            <el-text size="large">{{ comment.nickname }}  ({{ comment.email }})</el-text>
          </div>
          <div class="comment-content">
            <el-text size="large" style="color: #000000">{{ comment.content }}</el-text>
          </div>
          <el-divider/>
        </div>
      </div>
    </div>

    <el-pagination v-if="commentPageInfo.total > 0"
                   layout="prev, pager, next" :total="commentPageInfo.total" background
                   vmodel:current-page="currentPage" class="comment-pagination"
                   @current-change="handlePageNumChanged"
    />

    <div class="blog-comment-input">
      <el-row class="comment-input-wrapper">
        <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 5 }"
                  placeholder="说点什么吧" v-model="commentInput.content"></el-input>
      </el-row>
      <el-row class="comment-identify" :gutter="20">
        <el-col :span="10">
          <el-input placeholder="请输入昵称" v-model="commentInput.nickname">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="10" >
          <el-input placeholder="请输入邮箱" v-model="commentInput.email">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button style="width: 100%" @click="handlePublishComment" :disabled="commentButtonDisabled">
            发送 ({{ commentInput.content.length }}/200)
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>
  <el-backtop :right="100" :bottom="100" />
</template>

<script>
import {getBlogDetail} from "@/fetch/BlogDetailAPI";
import 'github-markdown-css/github-markdown.css'
import hljs from 'highlight.js';
import 'highlight.js/styles/default.css';
import {getCommentsByBlogID, publishComment} from "@/fetch/CommentAPI";
import {ElMessage} from "element-plus";

export default {
    created() {
        if(this.$route.params.blog_id) {
            this.blogDetail.id = this.$route.params.blog_id;
            this.getComments(this.blogDetail.id, this.currentPage);
            getBlogDetail(this.blogDetail.id)
                .then(blog => {
                    this.blogDetail = blog
                    setTimeout(() => hljs.highlightAll(), 100)
                });
        }
    },
    methods: {
        getComments(blogID, pageNum){
            // blogID = 0;
            getCommentsByBlogID(blogID, pageNum)
                .then(pageInfo => {
                    this.commentPageInfo = pageInfo;
                })
        },
        handlePageNumChanged(pageNum){
            this.getComments(this.blogDetail.id, pageNum);
        },
        handlePublishComment(){
            if(this.preCheckInput() === false) return;

            this.commentButtonDisabled = true;
            publishComment(this.blogDetail.id, this.commentInput)
                .then(code => {
                    if(code === 200) {
                        ElMessage.success('评论已发送');
                        this.clearCommentInput();
                    }
                    else if(code === 201){
                        ElMessage.warning('已发送，请等待站长审核')
                        this.clearCommentInput()
                    }
                    else
                        ElMessage.error('发送评论时出现错误');
                })
            setTimeout(() => {
                this.commentButtonDisabled = false;
            }, 3000)
        },
        preCheckInput() {
            let errorMessage = "";

            if (this.commentInput.nickname.length === 0)
                errorMessage = '请输入昵称';
            else if (this.commentInput.email.length === 0)
                errorMessage = '请输入邮箱';
            else if (this.commentInput.content.length === 0)
                errorMessage = '评论内容不可为空';
            else if (this.commentInput.content.length > 200)
                errorMessage = '评论字数超过限制';

            if(errorMessage.length > 0) {
                ElMessage.error(errorMessage)
                return false;
            }
            return true;
        },
        clearCommentInput(){
            this.commentInput.content = "";
            this.commentInput.nickname = "";
            this.commentInput.email = "";
        }
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
            },
            commentPageInfo: {},
            commentInput: {
                content: "",
                nickname: "",
                email: ""
            },
            currentPage: 1,
            commentButtonDisabled: false
        }
    }
}
</script>

<style scoped>
.blog-detail  {
    border: 1px solid var(--el-border-color);
    padding: 20px;
    background-color: #ffffff;
    margin: 20px 40px 30px;
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

.blog-content:deep(code){
    padding: 0;
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

.comment-input-wrapper {
    margin-bottom: 20px;
}

.comment-identity {
    margin-bottom: 10px;
}

.comment-item {
    display: flex;
}

.comment-wrapper {
    flex-grow: 1;
    margin-left: 10px;
}

.blog-comment-page {
    margin-left: 5px;
    margin-right: 5px;
}

.comment-pagination {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
}
</style>