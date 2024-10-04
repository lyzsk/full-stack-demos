import { defineStore } from "pinia";
import { reqLogin, reqUserInfo } from "@/api/user";
import type { loginForm, loginResponseData } from "@/api/user/type";
import type { UserState } from "@/store/modules/types/types";
import { SET_TOKEN, GET_TOKEN, REMOVE_TOKEN } from "@/utils/token";
import { constantRoutes } from "@/router/routes";

const useUserStore = defineStore("User", {
    state: (): UserState => {
        return {
            token: GET_TOKEN(),
            menuRoutes: constantRoutes,
            username: "",
            avatar: "",
        };
    },
    actions: {
        async userLogin(data: loginForm) {
            let result: loginResponseData = await reqLogin(data);
            if (result.code === 200) {
                this.token = result.data.token as string;
                SET_TOKEN(result.data.token as string);
                return "ok";
            } else {
                return Promise.reject(new Error(result.data.message));
            }
        },
        async userInfo() {
            let result = await reqUserInfo();
            if (result.code === 200) {
                this.username = result.data.checkUser.username;
                this.avatar = result.data.checkUser.avatar;
                return "ok";
            } else {
                return Promise.reject("获取用户信息失败");
            }
        },
        userLogout() {
            this.token = "";
            this.username = "";
            this.avatar = "";
            REMOVE_TOKEN();
        },
    },
    getters: {},
});

export default useUserStore;
