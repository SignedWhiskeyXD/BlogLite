<script>
import {addTag, getBlogTags, removeBlogTagByID, renameBlogTagByID} from "@/fetch/fetchBlogTags";

export default {
    created() {
        this.handleRequest(this.tagQueryParams.currentPage, this.tagQueryParams.currentPageSize)
    },
    methods: {
        handleRequest() {
            getBlogTags(this.tagQueryParams.currentPage, this.tagQueryParams.currentPageSize)
                .then(tagData => {
                    this.handleClear()
                    this.currentPageInfo = tagData;
                })
        },
        handlePageNumChanged(newPage){
            this.tagQueryParams.currentPage = newPage
            this.handleRequest()
        },
        handleRemove(id) {
            removeBlogTagByID(id).then(response => {
                if (response.ok) {
                    this.handleRequest()
                } else {
                    console.log("删除失败！")
                }
            })
        },
        handleAdd(newTagName){
            addTag(newTagName).then(response => {
                if (response.ok) {
                    this.handleRequest()
                } else {
                    console.log("添加失败！")
                }
            }).finally(()=>{
                this.dialogAddVisible = false
            })
        },
        handleRename(id, newName) {
            renameBlogTagByID(id, newName)
                .then(response =>{
                    if(response.ok){
                        this.handleRequest()
                        console.log("重命名成功")
                    }else{
                        console.log("删除失败")
                    }
                }).finally(()=>{
                this.dialogVisible = false
            })
        },
        handleClear() {
            this.currentPageInfo.list = []
        },
        showAddDialog(){
            this.dialogAddInput = "";
            this.dialogAddVisible = true;
        },
        showRenameDialog(tagInfo) {
            this.renameInput = tagInfo.tagName;
            this.dialogTag = tagInfo;
            this.dialogVisible = true;
        },

        handleClose() {
            this.dialogVisible = false
            this.dialogAddVisible = false
        },
    },
    data() {
        return {
            dialogVisible: false,
            dialogAddVisible: false,
            dialogTag: {},
            renameInput: "",
            dialogAddInput: "",

            currentPageInfo: {
                total: 0
            },
            tagQueryParams: {
                currentPage: 1,
                currentPageSize: 10
            }
        }
    }
}
</script>

<template>
    <h2 style="color: #333333">标签管理组件</h2>
    <el-button type="primary" size="large" @click="showAddDialog">添加标签</el-button>

    <el-table :data="currentPageInfo.list">
        <el-table-column label="序号" type="index" width="100"/>
        <el-table-column label="标签ID" prop="id"/>
        <el-table-column label="标签名" prop="tagName"/>
        <el-table-column lable="ops">
            <template v-slot="scope">
                <el-button type="primary" @click="showRenameDialog(scope.row)">重命名</el-button>
                <el-button type="danger" @click="handleRemove(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination layout="prev, pager, next" :total="currentPageInfo.total" background
                   vmodel:current-page="tagQueryParams.currentPage"
                   @current-change="handlePageNumChanged"

    />

    <el-dialog
            v-model="dialogAddVisible"
            title="新建标签"
            width="30%"
            :before-close="handleClose"
    >
        <el-input v-model="dialogAddInput"/>
        <template #footer>
          <span class="dialog-footer">
              <el-button type="primary" @click="handleAdd(dialogAddInput)">
                提交
              </el-button>
              <el-button @click="dialogAddVisible = false">取消</el-button>
          </span>
        </template>
    </el-dialog>

    <el-dialog
            v-model="dialogVisible"
            title="重命名标签"
            width="30%"
            :before-close="handleClose"
    >
        <el-input v-model="renameInput"/>
        <template #footer>
          <span class="dialog-footer">
              <el-button type="primary" @click="handleRename(dialogTag.id, renameInput)">
                重命名
              </el-button>
              <el-button @click="dialogVisible = false">取消</el-button>
          </span>
        </template>
    </el-dialog>
</template>

<style scoped>

</style>