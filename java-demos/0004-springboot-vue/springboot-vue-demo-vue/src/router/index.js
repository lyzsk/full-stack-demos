import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import Home from "../views/Home.vue";
import Index from "../views/Index.vue";
import User from "../views/sys/User.vue";
import Role from "../views/sys/Role.vue";
import Menu from "../views/sys/Menu.vue";
// 为动态绑定路由导入axios
import axios from "axios";
import store from "../store";

Vue.use(VueRouter);

const routes = [
    {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/index",
                name: "Index",
                meta: {
                    title: "首页",
                },
                component: Index,
            },
            {
                path: "/userCenter",
                name: "UserCenter",
                meta: {
                    title: "个人中心",
                },
                component: () => import("../views/UserCenter.vue"),
            },
            // 隐藏这一部分, 这部分要用动态路由来获取
            // {
            //     path: "/sys/users",
            //     name: "SysUser",
            //     component: User,
            // },
            // {
            //     path: "/sys/menus",
            //     name: "SysMenu",
            //     component: Menu,
            // },
            // {
            //     path: "/sys/roles",
            //     name: "SysRole",
            //     component: Role,
            // },
        ],
    },
    {
        path: "/login",
        name: "Login",
        component: () => import("../views/Login.vue"),
    },
];

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes,
});

router.beforeEach((to, from, next) => {
    let hasRoute = store.state.menus.hasRoute;
    let token = localStorage.getItem("token");
    if (to.path == "/login") {
        next();
    } else if (!token) {
        next({ path: "/login" });
    } else if (token && !hasRoute) {
        // 这里的axios不是$axios, 不是之前全局的, 所以要获取一次header
        axios
            .get("/sys/menu/nav", {
                headers: {
                    Authorization: localStorage.getItem("token"),
                },
            })
            .then((res) => {
                console.log(res.data.data);
                // 获取 menuList
                store.commit("setMenuList", res.data.data.nav);
                // 获取 用户权限
                store.commit("setPermList", res.data.data.authorities);
                console.log(store.state.menus.menuList);
                // 动态绑定路由
                let newRoutes = router.options.routes;
                res.data.data.nav.forEach((menu) => {
                    if (menu.children) {
                        menu.children.forEach((e) => {
                            // 转化成路由
                            let route = menuToRoute(e);
                            // 把路由添加到路由管理中
                            if (route) {
                                newRoutes[0].children.push(route);
                            }
                        });
                    }
                });
                console.log("newRoutes");
                console.log(newRoutes);
                // router.addRoutes(newRoutes);
                newRoutes.forEach((route) => {
                    router.addRoute(route);
                });

                hasRoute = true;
                store.commit("changeRouteStatus", hasRoute);
            });
    }

    next();
});

// router里面的menu是path, name, component, 和 mock里定义的不一样, 需要转化
const menuToRoute = (menu) => {
    // mock里面有的component是空的, 判断是否跳转路由
    if (!menu.component) {
        return null;
    }
    let route = {
        name: menu.name,
        path: menu.path,
        meta: {
            icon: menu.icon,
            title: menu.title,
        },
    };
    route.component = () => import("../views/" + menu.component + ".vue");
    return route;
};

export default router;
