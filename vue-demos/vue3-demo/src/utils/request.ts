import axios from "axios";
import { ElMessage } from "element-plus";

let request = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    timeout: 5000,
});

request.interceptors.request.use((config) => {
    return config;
});

request.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        let message = "";
        let status = error.response.status;
        switch (status) {
            case 401:
                message = "Token 过期";
                break;
            case 403:
                message = "Forbidden";
                break;
            case 404:
                message = "Not Found";
                break;
            case 500:
                message = "Internal Server Error";
                break;
            default:
                message = "Unknown Error";
                break;
        }
        ElMessage({
            type: "error",
            message: message,
        });
        return Promise.reject(error);
    }
);

export default request;
