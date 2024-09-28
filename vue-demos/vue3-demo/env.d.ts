/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly NODE_ENV: string;
    readonly VITE_APP_TITLE: string;
    readonly VITE_APP_BASE_API: string;
    readonly VITE_SERVE: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}
