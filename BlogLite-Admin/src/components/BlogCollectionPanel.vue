<script setup>

</script>

<template>
  <h2 style="color: #333333">专栏管理</h2>
  <el-button type="primary" size="large" @click="showDialogAdd">添加专栏</el-button>

  <el-table :data="collectionList">
    <el-table-column label="序号" type="index" width="100"/>
    <el-table-column label="专栏标题" prop="collectionName"/>
    <el-table-column label="文章数" prop="blogNum"/>
    <el-table-column label="总阅读数" prop="totalViews"/>
    <el-table-column lable="ops">
      <template v-slot="scope">
        <el-button type="primary" @click="showEditDialog(scope.row)">编辑</el-button>
        <el-button type="danger" @click="handleRemove(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog
      v-model="dialogAddVisible"
      title="新建专栏"
      width="30%"
      :before-close="handleDialogClose">

    <h4>专栏名</h4>
    <el-input v-model="newCollectionInfo.collectionName"/>
    <h4>简介</h4>
    <el-input v-model="newCollectionInfo.description"/>
    <h4>缩略图链接</h4>
    <el-input v-model="newCollectionInfo.imageLink"/>

    <template #footer>
          <span class="dialog-footer">
              <el-button type="primary" @click="handleAdd">
                提交
              </el-button>
              <el-button @click="dialogAddVisible = false">取消</el-button>
          </span>
    </template>
  </el-dialog>

  <el-dialog
      v-model="dialogEditVisible"
      title="编辑专栏信息"
      width="30%"
      :before-close="handleDialogClose"
  >
    <h4>专栏ID</h4>
    <el-input v-model="collectionModifyInfo.id" disabled="disabled"/>
    <h4>专栏名</h4>
    <el-input v-model="collectionModifyInfo.collectionName"/>
    <h4>简介</h4>
    <el-input v-model="collectionModifyInfo.description"/>
    <h4>缩略图链接</h4>
    <el-input v-model="collectionModifyInfo.imageLink"/>

    <template #footer>
          <span class="dialog-footer">
              <el-button type="primary" @click="handleModify(collectionModifyInfo)">
                修改
              </el-button>
              <el-button @click="dialogEditVisible = false">取消</el-button>
          </span>
    </template>
  </el-dialog>
</template>

<script>
import {
    createNewBlogCollection,
    getAllBlogCollections,
    modifyBlogCollection,
    removeBlogCollection
} from "@/fetch/BlogCollectionAPI";
import {ElMessage} from "element-plus";

export default {
    created() {
        this.getData();
    },
    methods: {
        getData(){
            getAllBlogCollections()
                .then(list => {
                    this.collectionList = list
                })
        },
        showDialogAdd(){
            for(let input in this.newCollectionInfo)
                input = ""
            this.dialogAddVisible = true
        },
        showEditDialog(collectionInfo){
            this.collectionModifyInfo = collectionInfo
            this.dialogEditVisible = true
        },
        handleAdd(){
            createNewBlogCollection(this.newCollectionInfo)
                .then(success => {
                    if(success){
                        ElMessage({
                            message: "添加成功！",
                            type: "success"
                        })
                        this.getData()
                    }else{
                        ElMessage({
                            message: "添加失败！",
                            type: "error"
                        })
                    }
                }).finally(() => {
                    this.dialogAddVisible = false
                })
        },
        handleRemove(collectionID){
            removeBlogCollection(collectionID)
                .then(success => {
                    if(success){
                        ElMessage({
                            message: "删除成功！",
                            type: "success"
                        })
                        this.getData()
                    }else{
                        ElMessage({
                            message: "删除失败！",
                            type: "error"
                        })
                    }
                })
        },
        handleModify(modifyInfo){
            modifyBlogCollection(modifyInfo)
                .then(success => {
                    if(success){
                        ElMessage({
                            message: "修改成功！",
                            type: "success"
                        })
                        this.getData()
                    }else{
                        ElMessage({
                            message: "修改失败！",
                            type: "error"
                        })
                    }
                })
                .finally(() => {
                    this.dialogEditVisible = false
                })
        },
        handleDialogClose(){
            this.dialogAddVisible = false
            this.dialogEditVisible = false
        }
    },
    data(){
        return{
            collectionList: [],

            dialogAddVisible: false,
            dialogEditVisible: false,

            newCollectionInfo: {
                collectionName: "",
                description: "",
                imageLink: "",
            },

            collectionModifyInfo: {
                id: 0,
                collectionName: "",
                description: "",
                imageLink: "",
            }
        }
    }
}
</script>

<style scoped>

</style>