<script setup>

</script>

<template>
  <div class="blog-detail-wrapper">
    <div class="blog-detail-wrapper-secondary">
      <main class="blog-detail" :style="{boxShadow: `var(--el-box-shadow-dark)`}" ref="blogDetailSection">
        <el-skeleton animated :rows="20" v-if="!(blogReady && minLoadTimeFlag)"
                     v-loading="!(blogReady && minLoadTimeFlag)"/>
        <div v-else>
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

          <h3 ref="blogCommentTitle">{{ commentPageInfo.total }}条评论</h3>

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

          <el-pagination v-if="commentPageInfo.total > 10"
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
      </main>
      <aside class="blog-detail-sidebar">
        <div class="blog-tags" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
          <h4 style="text-align: center">文章标签</h4>
          <el-divider style="margin-bottom: 10px"/>
          <el-tag v-for="tag in blogDetail.tagNames" class="blog-tag"
                  :key="tag" size="large">
            {{ tag }}
          </el-tag>
          <el-text v-if="!(blogDetail.tagNames.length > 0) && blogReady" size="large">
            这篇文章没有标签诶
          </el-text>
        </div>
      </aside>
    </div>
    <div class="fixed-buttons">
      <div class="button-to-top" :style="{boxShadow: `var(--el-box-shadow-dark)`}" @click="scrollToTop">
        <el-icon style="scale: 200%"><ArrowUpBold /></el-icon>
      </div>
      <div class="button-to-comment" :style="{boxShadow: `var(--el-box-shadow-dark)`}"
           @click="scrollToCommentSection">
        <el-icon style="scale: 200%"><ChatLineSquare /></el-icon>
      </div>
    </div>
  </div>
</template>

<script>
import {getBlogDetail} from "@/fetch/BlogDetailAPI";
import 'github-markdown-css/github-markdown.css'
import {getCommentsByBlogID, publishComment} from "@/fetch/CommentAPI";
import {ElMessage} from "element-plus";
import {getCommentIdentity, saveCommentIdentity} from "@/utils/storageUtils";

export default {
    name: "BlogDetail",
    props: {
        blog_id: "0"
    },
    created() {
        if(this.blog_id > 0) {
            this.blogDetail.id = this.blog_id;
            this.fetchData();
        }
        const saveUserInfo = getCommentIdentity();
        this.commentInput.nickname = saveUserInfo.nickname;
        this.commentInput.email = saveUserInfo.email;
        console.log(this.commentInput)
    },
    // 文章详情页，也就是本组件是被keep-alive缓存的，需要用下面的两个方法，在复用本组件时按需改变内容
    beforeRouteUpdate(to, from) {
        // 在不同的文章详情页之间路由时，activated生命周期钩子不会调用，应该使用Vue Router的导航守卫
        // this 已经可用, props中的id与to route中的路由参数是一致的
        this.blogDetail.id = to.params.blog_id;
        this.fetchData();
    },
    activated() {
        // 从主页导航至该组件时，会调用本方法，应当根据路由Boolean Props决定组件的内容
        if(this.blog_id != this.blogDetail.id){
            this.blogDetail.id = this.blog_id;
            this.fetchData();
        }
    },
    methods: {
        fetchData(){
            this.blogReady = false;
            this.minLoadTimeFlag = false;
            setTimeout(() => {this.minLoadTimeFlag = true}, 400);

            this.getComments(this.blogDetail.id, this.currentPage);
            getBlogDetail(this.blogDetail.id)
                .then(blog => {
                    this.blogDetail = blog
                    this.blogReady = true
                });
        },
        getComments(blogID, pageNum){
            getCommentsByBlogID(blogID, pageNum)
                .then(pageInfo => {
                    this.commentPageInfo = pageInfo;
                })
        },
        scrollToCommentSection(){
            const commentSection = this.$refs.blogCommentTitle;
            commentSection.scrollIntoView({ behavior: 'smooth' });
        },
        scrollToTop(){
            const blogDetail = this.$refs.blogDetailSection;
            blogDetail.scrollIntoView({ behavior: 'smooth' })
        },
        handlePageNumChanged(pageNum){
            this.getComments(this.blogDetail.id, pageNum);
        },
        handlePublishComment(){
            if(this.preCheckInput() === false) return;

            this.commentButtonDisabled = true;
            publishComment(this.blogDetail.id, this.commentInput)
                .then(response => {
                    if(response.code === 200) {
                        ElMessage.success('评论已发送');
                        saveCommentIdentity(this.commentInput)
                        this.clearCommentInput();
                        this.currentPage = 1;
                        this.getComments(this.blogDetail.id, 1);
                    }
                    else if(response.code === 201){
                        ElMessage.warning('已发送，请等待站长审核')
                        saveCommentIdentity(this.commentInput)
                        this.clearCommentInput()
                    }
                    else if(response.code === 50400)
                        ElMessage.error(`操作太频繁，每${response.message}分钟才能发送一条评论`)
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
            else if (!this.commentInput.email.match("[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}"))
                errorMessage = '请输入合法的邮箱地址'
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
            blogReady: false,
            minLoadTimeFlag: false,
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
.blog-detail-wrapper {
    display: flex;
    justify-content: center;
}

.blog-detail-wrapper-secondary {
    width: 1280px;
    display: flex;
}

.blog-detail  {
    border: 1px solid var(--el-border-color);
    padding: 20px;
    background-color: #ffffff;
    margin: 20px 40px 30px;
    width: 75%;
}

.blog-detail-sidebar {
    width: 25%;
}

.blog-tags {
    border: 1px solid var(--el-border-color);
    background-color: #ffffff;
    padding: 5px 20px 20px;
    margin-top: 20px;
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
    margin-bottom: 8px;
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

.fixed-buttons {
    position: fixed;
    bottom: 40px;
    right: 40px;
}

.button-to-comment {
    background-color: white;
    width: 60px;
    height: 60px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.button-to-comment:hover {
    cursor: pointer;
}

.button-to-top {
    background-color: white;
    width: 60px;
    height: 60px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 20px;
}

.button-to-top:hover {
    cursor: pointer;
}
</style>