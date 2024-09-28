function createUserList() {
    return [
        {
            userId: 1,
            avatar: "https://cdn.cloudflare.steamstatic.com/steamcommunity/public/images/avatars/73/7331e51f61063068cd672a6e7041c387818c1257.jpg",
            username: "admin",
            password: "123456",
            desc: "This is the administrator account",
            roles: ["admin"],
            buttons: ["curser.detail"],
            routes: ["home"],
            token: "Admin Token",
        },
        {
            userId: 2,
            avatar: "https://cdn.cloudflare.steamstatic.com/steamcommunity/public/images/avatars/73/7331e51f61063068cd672a6e7041c387818c1257.jpg",
            username: "test",
            password: "123456",
            desc: "This is the test account",
            roles: ["test"],
            buttons: ["curser.detail"],
            routes: ["home"],
            token: "Test Token",
        },
    ];
}

export default [
    {
        url: "/api/user/login",
        method: "post",
        response: ({ body }) => {
            const { username, password } = body;
            const checkUser = createUserList().find(
                (item) =>
                    item.username === username && item.password === password
            );
            if (!checkUser) {
                return { code: 201, data: { message: "用户名或密码错误" } };
            }
            const { token } = checkUser;
            return { code: 200, data: { token } };
        },
    },
    {
        url: "/api/user/info",
        method: "get",
        response: (request) => {
            const token = request.headers.token;
            const checkUser = createUserList().find(
                (item) => item.token === token
            );
            if (!checkUser) {
                return { code: 201, data: { message: "用户不存在" } };
            }
            return { code: 200, data: { checkUser } };
        },
    },
];
