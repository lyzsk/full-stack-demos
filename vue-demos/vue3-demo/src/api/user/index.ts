import request from "@/utils/request";
import type { loginForm, loginResponseData, userResponseData } from "./type";

enum API {
    LOGIN_URL = "/user/login",
    USERINFO_URL = "/user/info",
}

export const reqLogin = (data: loginForm) =>
    request.post<any, loginResponseData>(API.LOGIN_URL, data);
export const getUserInfo = () =>
    request.get<any, userResponseData>(API.USERINFO_URL);
