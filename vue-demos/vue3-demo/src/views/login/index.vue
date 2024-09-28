<template>
    <div class="loginContainer">
        <el-row>
            <el-col :span="12" :xs="0">
                <el-form></el-form>
            </el-col>
            <el-col :span="12" :xs="24">
                <el-form
                    class="loginForm"
                    :model="loginForm"
                    :rules="rules"
                    ref="loginFormRef"
                >
                    <h1>hello</h1>
                    <h2>welcome to login page</h2>
                    <el-form-item prop="username">
                        <el-input
                            :prefix-icon="User"
                            v-model="loginForm.username"
                        ></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input
                            type="password"
                            :prefix-icon="Lock"
                            v-model="loginForm.password"
                            show-password
                        ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button
                            :loading="loading"
                            class="loginBtn"
                            type="primary"
                            size="default"
                            @click="login"
                            >登录</el-button
                        >
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </div>
</template>

<script setup lang="ts">
import { User, Lock } from "@element-plus/icons-vue";
import { ref, reactive } from "vue";
import useUserStore from "@/store/modules/user";
import { useRouter } from "vue-router";
import { ElNotification, messageConfig } from "element-plus";
import { getTime } from "@/utils/time";

let useStore = useUserStore();
let $router = useRouter();
let loading = ref(false);
let loginForm = reactive({ username: "admin", password: "123456" });
let loginFormRef = ref();
const login = async () => {
    await loginFormRef.value.validate();
    loading.value = true;
    try {
        await useStore.userLogin(loginForm);
        $router.push("/");
        ElNotification({
            type: "success",
            message: "欢迎回来",
            title: `Hi, ${getTime()}好`,
        });
        loading.value = false;
    } catch (error) {
        loading.value = false;
        ElNotification({
            type: "error",
            message: (error as Error).message,
        });
    }
};
const validatorUsername = (rule: any, value: any, callback: any) => {
    // if (/^\d{5,10}$/.test(value)) {
    if (value.length >= 5) {
        callback();
    } else {
        callback(new Error("账号长度至少五位"));
    }
};
const validatorPassword = (rule: any, value: any, callback: any) => {
    if (value.length >= 6) {
        callback();
    } else {
        callback(new Error("密码长度至少六位"));
    }
};
const rules = {
    username: [
        { required: true, message: "请输入用户名", trigger: "blur" },
        // {
        //     required: true,
        //     min: 6,
        //     max: 32,
        //     message: "账号长度至少六位",
        //     trigger: "change",
        // },
        {
            trigger: "change",
            validator: validatorUsername,
        },
    ],
    password: [
        { required: true, message: "请输入密码", trigger: "blur" },
        // {
        //     required: true,
        //     min: 6,
        //     max: 15,
        //     message: "密码长度至少六位",
        //     trigger: "change",
        // },
        {
            trigger: "change",
            validator: validatorPassword,
        },
    ],
};
</script>

<style scoped lang="scss">
.loginContainer {
    width: 100%;
    height: 100vh;
    background: url("@/assets/images/background.jpg") no-repeat;
    background-size: cover;
}
.loginForm {
    width: 80%;
    top: 30vh;
    position: relative;
    background: url("@/assets/images/login-bg.jpg") no-repeat;
    background-size: cover;
    padding: 20px;
    h1 {
        color: white;
        font-size: 40px;
    }
    h2 {
        color: white;
        font-size: 20px;
        margin: 20px 0;
    }
    .loginBtn {
        width: 100%;
    }
}
</style>
