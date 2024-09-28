import { createApp } from "vue";
import App from "@/App.vue";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import "virtual:svg-icons-register";
import globalComponent from "@/components/index";
import "@/styles/index.scss";
import router from "./router";
import pinia from "./store";

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}
app.use(ElementPlus, {
    locale: zhCn,
});
app.use(globalComponent);
app.use(pinia);
app.use(router);
app.mount("#app");
