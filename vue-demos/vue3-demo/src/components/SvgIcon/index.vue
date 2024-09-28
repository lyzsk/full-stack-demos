<template>
    <svg :style="{ width, height }">
        <use :xlink:href="prefix + name" :fill="color"></use>
    </svg>
</template>

<script setup lang="ts">
// 接收父组件传递来的参数
defineProps({
    prefix: {
        type: String,
        default: "#icon-",
    },
    name: String,
    color: {
        type: String,
        default: "",
    },
    width: {
        type: String,
        default: "16px",
    },
    height: {
        type: String,
        default: "16px",
    },
});
</script>

<style scoped></style>
