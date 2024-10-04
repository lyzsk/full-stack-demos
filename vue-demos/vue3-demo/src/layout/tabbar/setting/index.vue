<template>
    <el-button
        size="small"
        icon="Refresh"
        circle
        @click="updateRefresh"
    ></el-button>
    <el-button
        size="small"
        icon="FullScreen"
        circle
        @click="fullScreen"
    ></el-button>
    <el-button size="small" icon="Setting" circle></el-button>
    <img
        :src="userStore.avatar"
        style="width: 24px; height: 24px; margin: 0 10px; border-radius: 50%"
    />
    <el-dropdown>
        <span class="el-dropdown-link">
            {{ userStore.username }}
            <el-icon class="el-icon--right">
                <arrow-down />
            </el-icon>
        </span>
        <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </template>
    </el-dropdown>
</template>

<script setup lang="ts">
import useUserStore from "@/store/modules/user";
import useLayoutsettingStore from "@/store/modules/setting";
import { useRouter, useRoute } from "vue-router";

let userStore = useUserStore();
let layoutsettingStore = useLayoutsettingStore();
let $router = useRouter();
let $route = useRoute();

const updateRefresh = () => {
    layoutsettingStore.refresh = !layoutsettingStore.refresh;
};
const fullScreen = () => {
    let isFullScreen = document.fullscreenElement;
    if (!isFullScreen) {
        document.documentElement.requestFullscreen();
    } else {
        document.exitFullscreen();
    }
};
const logout = () => {
    userStore.userLogout();
    $router.push({ path: "/login", query: { redirect: $route.path } });
};
</script>

<script lang="ts">
export default {
    name: "Setting",
};
</script>

<style scoped lang="scss"></style>
