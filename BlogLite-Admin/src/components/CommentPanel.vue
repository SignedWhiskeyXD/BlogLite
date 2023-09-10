<script setup>

</script>

<template>
  <h2>待审核的评论</h2>
  <el-button :disabled="!(commentList.length > 0)" type="primary" size="large"
             @click="handleReviewSubmit">提交</el-button>

  <el-table :data="commentList" @selection-change="handleSelectionChange">
    <el-table-column type="selection"/>
    <el-table-column label="序号" type="index" width="100"/>
    <el-table-column lable="所在文章" prop="blogTitle"/>
    <el-table-column lable="昵称" prop="nickname"/>
    <el-table-column lable="邮箱" prop="email"/>
    <el-table-column lable="邮箱" prop="content"/>
    <el-table-column lable="发布时间" prop="publishTime"/>
  </el-table>
</template>

<script>
import {getCommentsToReview, submitReviewResult} from "@/fetch/CommentAPI";
import {ElMessage} from "element-plus";

export default {
    created() {
        this.getCommentList();
    },
    methods: {
        getCommentList(){
            getCommentsToReview(this.commentLimit)
                .then(list => {
                    this.commentList = list;
                })
        },
        handleSelectionChange(selected){
            this.selectedCommentID = [];
            for(const commentRow of selected){
                this.selectedCommentID.push(commentRow.commentID);
            }
        },
        handleReviewSubmit(){
            const reviewInfo = {
                enableIDs: this.selectedCommentID,
                deleteIDs: []
            }
            for(const comment of this.commentList){
                if(!reviewInfo.enableIDs.includes(comment.commentID))
                    reviewInfo.deleteIDs.push(comment.commentID);
            }

            submitReviewResult(reviewInfo)
                .then(success => {
                    if(success){
                        ElMessage.success("提交成功");
                        this.commentList = [];
                        this.getCommentList();
                    }else{
                        ElMessage.error("提交失败！");
                    }
                })
        }
    },
    data() {
        return {
            commentLimit: 10,
            commentList: [],
            selectedCommentID: []
        }
    }
}
</script>

<style scoped>

</style>