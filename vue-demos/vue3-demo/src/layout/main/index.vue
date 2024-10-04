<template>
    <!-- 路由组件出口位置 -->
    <router-view v-slot="{ Component }">
        <transition name="fade">
            <!-- 渲染 layout 一级路由组件的子路由 -->
            <component :is="Component" v-if="flag" />
        </transition>
    </router-view>
</template>

<script setup lang="ts">
import { ref, watch, nextTick } from "vue";
import useLayoutsettingSotre from "@/store/modules/setting";

let layoutSettingStore = useLayoutsettingSotre();
let flag = ref(true);

watch(
    () => layoutSettingStore.refresh,
    () => {
        flag.value = false;
        nextTick(() => {
            flag.value = true;
        });
    }
);
</script>

<style scoped lang="scss">
.fade-enter-from {
    opacity: 0;
    transform: scale(0);
}
.fade-enter-active {
    transition: all 0.3s;
}
.fade-enter-to {
    opacity: 1;
    transform: scale(1);
}
</style>
