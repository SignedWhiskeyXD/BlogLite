<template>
    <div class="login-container">
        <div class="login-header">
            <img alt="Vue logo" class="myLogo" src="../assets/huaji.jpg" width="125" height="125" />
        </div>
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
import router from "@/router";
import {Lock, User} from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";

export default {
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

            // 发送网络请求
            login(requestBody)
                .then(response => {
                    if(response.code === 200) {
                        ElMessage.success('登录成功')
                        window.localStorage.setItem('token', response.body.token)
                        window.localStorage.setItem('email', response.body.email)
                        router.push('/admin')
                    }
                    else {
                        let errorMessage = "未知错误"
                        if(response.code === 53404)
                            errorMessage = "用户不存在！"
                        else if(response.code === 52403)
                            errorMessage = "用户名或密码错误"
                        ElMessage.error(errorMessage)
                    }
                })
            .catch((error) => {
                // 处理错误情况
                console.error('Error:', error);
            });
        }
    },
};
</script>

<style scoped>
.login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
    background-color: #f2f2f2;
}

.login-header {
    margin-bottom: 30px;
}

.myLogo {
    border-radius: 50%;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
}

.el-form-item__label {
    color: #333;
    font-weight: bold;
}

.el-input__inner {
    border-radius: 4px;
    border: 1px solid #ccc;
}

.login-btn {
    width: 100%;
    margin-top: 20px;
}

.login-btn:hover {
    background-color: #409eff;
}
</style>