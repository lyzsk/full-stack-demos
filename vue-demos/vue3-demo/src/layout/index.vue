<template>
    <div class="layout_container">
        <div
            class="layout_slider"
            :class="{ fold: layoutSettingStore.fold ? true : false }"
        >
            <Logo />
            <el-scrollbar class="scrollbar">
                <el-menu
                    :default-active="$route.path"
                    :collapse="layoutSettingStore.fold ? true : false"
                >
                    <Menu :menuList="userStore.menuRoutes"></Menu>
                </el-menu>
            </el-scrollbar>
        </div>
        <div
            class="layout_tabbar"
            :class="{ fold: layoutSettingStore.fold ? true : false }"
        >
            <Tabbar />
        </div>
        <div
            class="layout_main"
            :class="{ fold: layoutSettingStore.fold ? true : false }"
        >
            <Main></Main>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRoute } from "vue-router";
import Logo from "./logo/index.vue";
import Menu from "./menu/index.vue";
import Main from "./main/index.vue";
import Tabbar from "./tabbar/index.vue";
import useUserStore from "@/store/modules/user";
import useLayoutSettingStore from "@/store/modules/setting";

let userStore = useUserStore();
let $route = useRoute();
let layoutSettingStore = useLayoutSettingStore();
</script>

<script lang="ts">
export default {
    name: "Layout",
};
</script>

<style scoped lang="scss">
.layout_container {
    width: 100%;
    height: 100vh;

    .layout_slider {
        color: #fff;
        width: $base-menu-width;
        height: 100vh;
        background-color: $base-menu-color;
        transition: all 0.3s;
        .scrollbar {
            width: 100%;
            height: calc(100vh - $base-menu-logo-height);
            .el-menu {
                --el-menu-bg-color: #001429;
                --el-menu-text-color: #fff;
                --el-menu-hover-bg-color: #001121;
                // --active-text-color: #409eff;
                --active-text-color: #7fdc96;
                border-right: none;
            }
        }
        &.fold {
            width: $base-menu-min-width;
        }
    }

    .layout_tabbar {
        position: fixed;
        width: calc(100% - $base-menu-width);
        height: $base-tabbar-height;
        top: 0;
        left: $base-menu-width;
        transition: all 0.3s;
        &.fold {
            left: $base-menu-min-width;
            width: calc(100vw - $base-menu-min-width);
        }
    }

    .layout_main {
        position: absolute;
        width: calc(100% - $base-menu-width);
        height: calc(100vh - $base-tabbar-height);
        top: $base-tabbar-height;
        left: $base-menu-width;
        padding: 20px;
        overflow: auto;
        transition: all 0.3s;
        &.fold {
            left: $base-menu-min-width;
            width: calc(100vw - $base-menu-min-width);
        }
    }
}
</style>
