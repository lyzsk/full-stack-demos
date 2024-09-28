export const SET_TOKEN = (token: string) => {
    localStorage.setItem("TOKEN", token);
};

export const GET_TOKEN = () => {
    return localStorage.getItem("TOKEN");
};
