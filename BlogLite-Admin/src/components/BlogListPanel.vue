<script>
import {getBlogs, removeBlogByID} from "@/fetch/BlogAPI";
  import {ElMessage} from "element-plus";

  export default {
      created() {
          this.handleRequestBlogs()
      },
      methods: {
           handleRequestBlogs(){
               getBlogs(this.pageQueryParams.currentPage,
                        this.pageQueryParams.currentPageSize)
                   .then(response => {
                       if(response != null)
                           this.currentPageInfo = response
                       else
                           ElMessage.error('加载文章列表失败！')
                   })
          },
          handlePageNumChanged(newPage){
              this.pageQueryParams.currentPage = newPage
              this.handleRequestBlogs()
          },
          async handleRemoveBlog(id){
              let result = await removeBlogByID(id);
              if(result){
                  ElMessage.success('删除成功')
                  this.handleRequestBlogs()
                  console.log(this.currentPageInfo.list)
              }else{
                  ElMessage.error('删除失败！')
              }
          },
          showEditBlogDialog(selectedBlog){
              this.currentBlogInDialog = selectedBlog
              this.dialogEditBlogVisible = true
          },
          handleEditBlogDialogClosed(){
              this.dialogEditBlogVisible = false
          }
      },
      data(){
          return{
              dialogEditBlogVisible: false,
              currentBlogInDialog: {},

              currentPageInfo: {
                  total: 0
              },
              pageQueryParams: {
                  currentPage: 1,
                  currentPageSize: 10
              }
          }
      }
  }
</script>

<template>
    <h2 style="color: #333333">所有文章管理</h2>

    <el-table :data="currentPageInfo.list">
        <el-table-column label="序号" type="index" width="100"/>
        <el-table-column label="博客ID" prop="id"/>
        <el-table-column label="标题" prop="title"/>
        <el-table-column lable="ops">
            <template v-slot="scope">
                <el-button type="primary" @click="showEditBlogDialog(scope.row)">编辑</el-button>
                <el-button type="danger" @click="handleRemoveBlog(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next" :total="currentPageInfo.total" background
                   vmodel:current-page="pageQueryParams.currentPage"
                   @current-change="handlePageNumChanged"
    />

    <el-dialog
            v-model="dialogEditBlogVisible"
            title="编辑文章"
            width="30%"
            :before-close="handleEditBlogDialogClosed"
    >
        <template #footer>
          <span class="dialog-footer">
              <el-button @click="dialogEditBlogVisible = false">取消</el-button>
          </span>
        </template>
    </el-dialog>
</template>

<style scoped>

</style>