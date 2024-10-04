import { Chicken } from "@element-plus/icons-vue";
import component from "element-plus/es/components/tree-select/src/tree-select-option.mjs";

export const constantRoutes = [
    {
        path: "/login",
        component: () => import("@/views/login/index.vue"),
        name: "login",
        meta: {
            title: "登录",
            hidden: true,
            icon: "Promotion",
        },
    },
    {
        path: "/",
        component: () => import("@/layout/index.vue"),
        name: "layout",
        meta: {
            title: "",
            hidden: false,
            icon: "",
        },
        redirect: "/home",
        children: [
            {
                path: "/home",
                component: () => import("@/views/home/index.vue"),
                meta: {
                    title: "首页",
                    hidden: false,
                    icon: "HomeFilled",
                },
            },
        ],
    },
    {
        path: "/404",
        component: () => import("@/views/404/index.vue"),
        name: "404",
        meta: {
            title: "404",
            hidden: true,
            icon: "DocumentDelete",
        },
    },
    {
        path: "/:pathMatch(.*)*",
        redirect: "/404",
        name: "Any",
        meta: {
            title: "任意路由",
            hidden: true,
            icon: "DataLine",
        },
    },
    {
        path: "/screen",
        component: () => import("@/views/screen/index.vue"),
        name: "Screen",
        meta: {
            title: "数据大屏",
            hidden: false,
            icon: "Platform",
        },
    },
    {
        path: "/acl",
        component: () => import("@/layout/index.vue"),
        name: "Acl",
        meta: {
            title: "权限管理",
            hidden: false,
            icon: "Lock",
        },
        redirect: "/acl/user",
        children: [
            {
                path: "/acl/user",
                component: () => import("@/views/acl/user/index.vue"),
                name: "User",
                meta: {
                    title: "用户管理",
                    hidden: false,
                    icon: "User",
                },
            },
            {
                path: "/acl/role",
                component: () => import("@/views/acl/role/index.vue"),
                name: "Role",
                meta: {
                    title: "角色管理",
                    hidden: false,
                    icon: "UserFilled",
                },
            },
            {
                path: "/acl/permission",
                component: () => import("@/views/acl/permission/index.vue"),
                name: "Permission",
                meta: {
                    title: "菜单管理",
                    hidden: false,
                    icon: "Monitor",
                },
            },
        ],
    },
    {
        path: "/product",
        component: () => import("@/layout/index.vue"),
        name: "Product",
        meta: {
            title: "商品管理",
            hidden: false,
            icon: "Goods",
        },
        redirect: "/product/trademark",
        children: [
            {
                path: "/product/trademark",
                component: () => import("@/views/product/trademark/index.vue"),
                name: "Trademark",
                meta: {
                    title: "品牌管理",
                    hidden: false,
                    icon: "ShoppingCartFull",
                },
            },
            {
                path: "/product/attr",
                component: () => import("@/views/product/attr/index.vue"),
                name: "Attr",
                meta: {
                    title: "属性管理",
                    hidden: false,
                    icon: "ChromeFilled",
                },
            },
            {
                path: "/product/spu",
                component: () => import("@/views/product/spu/index.vue"),
                name: "Spu",
                meta: {
                    title: "SPU管理",
                    hidden: false,
                    icon: "Calendar",
                },
            },
            {
                path: "/product/sku",
                component: () => import("@/views/product/sku/index.vue"),
                name: "Sku",
                meta: {
                    title: "SKU管理",
                    hidden: false,
                    icon: "Orange",
                },
            },
        ],
    },
];
