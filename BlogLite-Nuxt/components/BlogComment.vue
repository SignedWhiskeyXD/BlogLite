<script setup lang="ts">
import type {CommentPage} from "~/model/Comment";
import type RestResponse from "~/model/RestResponse";
import {responseGuard} from "~/my-utils/response-guard";
import type CommentInput from "~/model/CommentInput";
import {preCheckInput} from "~/my-utils/input-checking";

const props = defineProps<{
    blogId: number
}>();

const isAdmin = false;

const commentButtonDisabled = ref(false);

const handleRemoveComment = (id: any) => {};

const handlePageNumChanged = () => {};

const {data} = await useFetch<RestResponse>('/api/comment', {
    query: {
        id: props.blogId,
        pageNum: 1
    },
    baseURL: 'http://localhost:52480'
});

const commentPageInfo = responseGuard<CommentPage>(data.value);

const commentInput = ref<CommentInput>({
    email: '',
    nickname: '',
    content: ''
});

async function handlePublishComment() {
    if (!preCheckInput(commentInput.value)) return;

    commentButtonDisabled.value = true;

    const {data: response} = useFetch<RestResponse>(`/api/comment/${props.blogId}`, {
        method: 'POST',
        body: commentInput.value
    });

    afterCommentSubmitted(response.value);

    setTimeout(() => {
        commentButtonDisabled.value = false;
    }, 3000);
}

function afterCommentSubmitted(response: RestResponse | null) {
    const code = response?.code;

    if(code === 200){
        ElMessage.success('评论已发送');
        commentInput.value = {content: '', email: '', nickname: ''};

        //TODO: refresh Comments...
    }else if(code === 201){
        ElMessage.warning('已发送，请等待站长审核');
        commentInput.value = {content: '', email: '', nickname: ''};
    }else if(code === 50400)
        ElMessage.error(`操作太频繁，每${response?.message}分钟才能发送一条评论`)
    else
        ElMessage.error('发送评论时出现错误');
}
</script>

<template>
  <h3 ref="blogCommentTitle">{{ commentPageInfo.total }}条评论</h3>

  <div class="blog-comment-page">
    <div v-for="comment in commentPageInfo.list" :key="comment.id" class="comment-item">
      <el-avatar :size="50" style="min-width: 50px" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"/>
      <div class="comment-wrapper">
        <div class="comment-identity">
          <el-text size="large">{{ comment.nickname }}  ({{ comment.email }})</el-text>
          <el-popconfirm title="你确定?" @confirm="handleRemoveComment(comment.id)">
            <template #reference>
              <div v-if="isAdmin" class="remove-blog hover-pointer">删除</div>
            </template>
          </el-popconfirm>
        </div>
        <div class="comment-content">
          <el-text size="large" style="color: #000000">{{ comment.content }}</el-text>
        </div>
        <el-divider/>
      </div>
    </div>
  </div>

  <el-pagination v-if="commentPageInfo.total > 10"
                 layouts="prev, pager, next" :total="commentPageInfo.total" background
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
          发送
          <span class="w-mobile-no-display">({{ commentInput.content.length }}/200)</span>
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.comment-input-wrapper {
    margin-bottom: 20px;
}

.comment-identity {
    margin-bottom: 10px;
    position: relative;
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

.remove-blog {
    position: absolute;
    right: 5px;
    top: 0;
    color: palevioletred;
}
</style>