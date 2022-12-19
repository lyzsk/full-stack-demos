# quick install for dev

1. install vue

```
npm uninstall vue-cli -g

npm install -g @vue/cli

vue --version

vue create springboot-vue-admin-vue
```

需要勾选 babel, router, vuex

当前使用 Vue 2.x

2. install element-ui

```
npm i element-ui -S
```

@see https://element.eleme.cn/#/zh-CN/component/installation?fileGuid=HXqVy6jx8qkWKPJq&accessToken=eyJhbGciOiJIUzI1NiIsImtpZCI6ImRlZmF1bHQiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE2NzA4MDIxMTIsImZpbGVHVUlEIjoicHh3eUpIZ3FjV2pXa1RLWCIsImlhdCI6MTY3MDgwMTgxMiwiaXNzIjoidXBsb2FkZXJfYWNjZXNzX3Jlc291cmNlIiwidXNlcklkIjotNzQ3NDc5MDAxMX0.BVV5tNQTb0iFkyr2h3lX1bX_Cg1TUyKJJHU14pw9ySA

3. import element-ui in `main.js`

```js
import Element from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(Element);
```

4. install axios, qs, mockjs

-   axios: 一个基于 promise 的 HTTP 库，类 ajax

-   qs：查询参数序列化和解析库

-   mockjs：为我们生成随机数据的工具库

安装 axios 库

```
npm install axios
npm install qs
npm install mockjs
```

@see http://www.axios-js.com/?fileGuid=HXqVy6jx8qkWKPJq&accessToken=eyJhbGciOiJIUzI1NiIsImtpZCI6ImRlZmF1bHQiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE2NzA4MDIxMTIsImZpbGVHVUlEIjoicHh3eUpIZ3FjV2pXa1RLWCIsImlhdCI6MTY3MDgwMTgxMiwiaXNzIjoidXBsb2FkZXJfYWNjZXNzX3Jlc291cmNlIiwidXNlcklkIjotNzQ3NDc5MDAxMX0.BVV5tNQTb0iFkyr2h3lX1bX_Cg1TUyKJJHU14pw9ySA

@see http://mockjs.com/?fileGuid=HXqVy6jx8qkWKPJq&accessToken=eyJhbGciOiJIUzI1NiIsImtpZCI6ImRlZmF1bHQiLCJ0eXAiOiJKV1QifQ.eyJleHAiOjE2NzA4MDIxMTIsImZpbGVHVUlEIjoicHh3eUpIZ3FjV2pXa1RLWCIsImlhdCI6MTY3MDgwMTgxMiwiaXNzIjoidXBsb2FkZXJfYWNjZXNzX3Jlc291cmNlIiwidXNlcklkIjotNzQ3NDc5MDAxMX0.BVV5tNQTb0iFkyr2h3lX1bX_Cg1TUyKJJHU14pw9ySA

5. router

在开发页面之前我们需要先定义路由。传统项目开发，我们都是通过链接到达控制器然后再到页面渲染的。而类似于 Vue 这样的前后端分离性质的框架，我们是先访问页面，然后再异步加载数据渲染。而在 Vue 中，路由的管理是有个专门的组件叫 Router 管理的。

Router：WebApp 的链接路径管理系统，简单就是建立起 url 和页面之间的映射关系

`src\router\index.js` 就是用来配置路由的

创建 `views/Login.vue`, `views/Index.vue`, 然后在路由中心配置配置 url 与 vue 页面的映射关系

更改 `index.js`:

```js
import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login";

Vue.use(VueRouter);

const routes = [
    {
        path: "/index",
        name: "Index",
        component: () => import("../views/Index.vue"),
    },
    {
        path: "/login",
        name: "Login",
        component: Login,
    },
];

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes,
});

export default router;
```

新建的 Vue 项目，如果没其他配置，那么默认的就是一个单页面应用，也就是说这个应用是由一个外壳页面和多个页面片段组成的，页面跳转的时候其实始终都没有离开外壳页面，替换的只是加载的页面片段而已。

比如这个项目中, 外壳页面就是 App.vue，片段页面就是 Login.vue，所以我们刚刚通过/login 链接看到的页面效果就是 App.vue + Login.vue 的结果

修改 `App.vue`:

```js
<template>
    <div id="app">
        <router-view />
    </div>
</template>

<style>
html,
body,
#app {
    font-family: "Helvetica Neue", "Hiragino Sans GB", "WenQuanYi Micro Hei",
        "Microsoft Yahei", sans-serif;
    height: 100%;
    padding: 0;
    margin: 0;
    font-size: 15px;
}
</style>
```
