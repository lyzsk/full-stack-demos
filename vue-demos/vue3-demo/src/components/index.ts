import SvgIcon from "./SvgIcon/index.vue";
import Pagination from "./Pagination/index.vue";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const allGlobalComponents = { SvgIcon, Pagination };
type GlobalComponents = typeof allGlobalComponents;

// 对外暴露插件对象
export default {
    // 必须叫 install
    install(app: any) {
        (
            Object.keys(allGlobalComponents) as Array<keyof GlobalComponents>
        ).forEach((key) => {
            app.component(key, allGlobalComponents[key]);
        });
        for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
            app.component(key, component);
        }
    },
};
