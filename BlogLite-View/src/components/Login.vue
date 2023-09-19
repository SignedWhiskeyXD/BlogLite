<template>
  <div class="login-container">
    <div>{{this.username}}</div>
    <el-form :model="formData" ref="loginForm" label-width="80px">
      <el-form-item label="email" prop="email">
        <el-input
            v-model="formData.username"
            autocomplete="off"
            :prefix-icon="User"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
            v-model="formData.password"
            type="password"
            autocomplete="off"
            :prefix-icon="Lock"
        />
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="submitForm">登录</el-button>

  </div>
</template>

<script>
import {login} from "@/fetch/LoginAPI";
import {Lock, User} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";

export default {
    props: {
        username: String
    },
    computed: {
        Lock() {
            return Lock
        },
        User() {
            return User
        },
    },
    data() {
        return {
            formData: {
                username: '',
                password: '',
            },
        };
    },
    methods: {
        submitForm() {
            // 构建请求体
            const requestBody = {
                email: this.formData.username,
                password: this.formData.password,
            };
            login(requestBody)
                .then(response => {
                    if(response.code === 200) {
                        ElMessage({
                            message: "登录成功",
                            type: "success"
                        })
                        window.localStorage.setItem('token', response.body.token)
                    }
                    else {
                        let errorMessage = "未知错误"
                        if(response.code === 53404)
                            errorMessage = "用户不存在！"
                        else if(response.code === 52403)
                            errorMessage = "用户名或密码错误"
                        ElMessage({
                            message: errorMessage,
                            type: "error"
                        })
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        }
    },
};
</script>

<style>
.login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.login-header {
    margin-bottom: 30px;
}

.login-btn {
    width: 100%;
    margin-top: 20px;
}

.login-btn:hover {
    background-color: #409eff;
}
</style>