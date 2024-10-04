// layout组件配置仓库

import { defineStore } from "pinia";

let useLayoutSettingStore = defineStore("SettingStore", {
    state: () => {
        return {
            fold: false,
            refresh: false,
        };
    },
});

export default useLayoutSettingStore;
