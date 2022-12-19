<template>
    <el-container>
        <el-aside width="200px">
            <SideMenu></SideMenu>
        </el-aside>
        <el-container>
            <el-header>
                <strong>xxx管理系统</strong>
                <div class="header-avatar">
                    <el-avatar size="medium" :src="userInfo.avatar"></el-avatar>

                    <el-dropdown>
                        <span class="el-dropdown-link">
                            {{ userInfo.username
                            }}<i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item>
                                <router-link :to="{ name: 'UserCenter' }"
                                    >个人中心</router-link
                                >
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="logout"
                                >退出</el-dropdown-item
                            >
                        </el-dropdown-menu>
                    </el-dropdown>

                    <el-link href="https://markerhub.com" target="_blank"
                        >网站</el-link
                    >
                    <el-link
                        href="https://space.bilibili.com/13491144"
                        target="_blank"
                        >B站</el-link
                    >
                </div>
            </el-header>
            <!-- 添加router-view显示index.vue的内容 -->
            <el-main>
                <Tabs></Tabs>
                <div style="margin: 0 15px"><router-view /></div>
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
// 作为子路由导入
import SideMenu from "./inc/SideMenu.vue";
import Tabs from "./inc/Tabs.vue";
export default {
    name: "Home",
    components: {
        SideMenu,
        Tabs,
    },
    data() {
        return {
            userInfo: {
                id: "",
                username: "",
                avatar: "",
            },
        };
    },
    created() {
        this.getUserInfo();
    },
    methods: {
        getUserInfo() {
            this.$axios.get("/sys/userInfo").then((res) => {
                this.userInfo = res.data;
            });
        },
        logout() {
            this.$axios.post("/logout").then((res) => {
                localStorage.clear();
                sessionStorage.clear();
                this.$store.commit("resetState");
                this.$router.push("/login");
            });
        },
    },
};
</script>

<style scoped>
/* 布局改成全屏 */
.el-container {
    padding: 0;
    margin: 0;
    height: 100%;
}

/* 更改header右侧 */
.header-avatar {
    float: right;
    width: 210px;
    display: flex;
    justify-content: space-around;
    align-items: center;
}

/* 下拉菜单 */
.el-dropdown-link {
    cursor: pointer;
}

/* 整体布局 */
.el-header {
    background-color: #b3c0d1;
    color: #333;
    text-align: center;
    line-height: 60px;
}

.el-aside {
    background-color: #d3dce6;
    color: #333;
    line-height: 200px;
}

.el-main {
    background-color: #e9eef3;
    color: #333;
    /* text-align: center; */
    /* line-height: 160px; */
    padding: 0;
}

a {
    text-decoration: none;
}
</style>
