// 路由鉴权
import router from "@/router";
import setting from "@/setting";
import nprogress from "nprogress";
import "nprogress/nprogress.css";
import pinia from "./store";
import useUserStore from "@/store/modules/user";
import nProgress from "nprogress";

nProgress.configure({ showSpinner: false });

let userStore = useUserStore(pinia);

// 全局前置守卫
router.beforeEach(async (to: any, from: any, next: any) => {
    document.title = `${setting.title} - ${to.meta.title}`;
    nprogress.start();
    let token = userStore.token;
    let username = userStore.username;
    if (token) {
        if (to.path === "/login") {
            next({ path: "/" });
        } else {
            if (username) {
                next();
            } else {
                try {
                    await userStore.userInfo();
                    next();
                } catch (error) {
                    // token 过期 || 用户手动修改 localStorage
                    userStore.userLogout();
                    next({ path: "/login", query: { redirect: to.path } });
                }
            }
        }
    } else {
        if (to.path === "/login") {
            next();
        } else {
            next({ path: "/login", query: { redirect: to.path } });
        }
    }
});
// 全局后置守卫
router.afterEach((to: any, from: any) => {
    nprogress.done();
});
