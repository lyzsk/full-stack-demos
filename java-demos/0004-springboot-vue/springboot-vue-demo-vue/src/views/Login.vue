<template>
    <el-row class="row-bg" type="flex" justify="center">
        <el-col :xl="6" :lg="7">
            <h2>欢迎来到xxx管理系统</h2>
            <el-image
                :src="require('@/assets/wechat_payment_qr_code.jpg')"
                style="height: 180px; width: 180px"
                fit="fill"
                :lazy="true"
            ></el-image>

            <p>扫描二维码, 向我捐赠</p>
        </el-col>

        <el-col :span="1" :offset="0">
            <el-divider direction="vertical"></el-divider>
        </el-col>

        <el-col :xl="6" :lg="7">
            <el-form
                :model="loginForm"
                :rules="rules"
                ref="loginForm"
                label-width="80px"
            >
                <el-form-item
                    label="用户名"
                    prop="username"
                    style="width: 380px"
                >
                    <el-input v-model="loginForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password" style="width: 380px">
                    <el-input
                        v-model="loginForm.password"
                        type="password"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="验证码"
                    prop="code"
                    style="width: 380px"
                    maxlength="5"
                    @click="getCaptcha"
                >
                    <!-- 验证码 -->
                    <el-input
                        v-model="loginForm.code"
                        style="width: 170px; float: left"
                    ></el-input>
                    <!-- 验证码图片 -->
                    <el-image
                        :src="captchaImg"
                        class="captchaImg"
                        @click="getCaptcha"
                        fit="fill"
                        :lazy="true"
                    ></el-image>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm('loginForm')"
                        >立即创建</el-button
                    >
                    <el-button @click="resetForm('loginForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<script>
import qs from "qs";
export default {
    data() {
        return {
            loginForm: {
                username: "admin",
                password: "666666",
                code: "11111",
                token: "",
            },
            rules: {
                username: [
                    {
                        required: true,
                        message: "请输入用户名",
                        trigger: "blur",
                    },
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: "blur" },
                ],
                code: [
                    {
                        required: true,
                        message: "请输入验证码",
                        trigger: "blur",
                    },
                    {
                        min: 5,
                        max: 5,
                        message: "长度为 5 个字符",
                        trigger: "blur",
                    },
                ],
            },
            captchaImg: null,
        };
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    // TODO: 先改成form表单形式, 有空研究下改后端成json形式
                    // this.$axios.post("/login", this.loginForm).then((res) => {
                    // qs.stringify() 是把一个参数对象格式化为一个字符串
                    this.$axios
                        .post("/login?" + qs.stringify(this.loginForm))
                        .then((res) => {
                            const jwt = res.headers["authorization"];
                            this.$store.commit("SET_TOKEN", jwt);
                            this.$router.push("/index");
                        });
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        getCaptcha() {
            this.$axios.get("/captcha").then((res) => {
                console.log("get captcha");
                console.log(res);

                this.loginForm.token = res.data.data.token;
                this.captchaImg = res.data.data.captchaImg;
                this.loginForm.code = "";
            });
        },
        // Result: code, msg, data
    },
    created() {
        this.getCaptcha();
    },
};
</script>

<style scoped>
.el-row {
    background-color: #fafafa;
    height: 100%;
    display: flex;
    align-items: center;
    text-align: center;
    justify-content: center;
}

.el-divider {
    height: 200px;
}

.captchaImg {
    float: left;
    margin-left: 8px;
    border-radius: 4px;
}
</style>
