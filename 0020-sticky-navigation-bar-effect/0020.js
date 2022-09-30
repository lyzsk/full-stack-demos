const navbar = document.querySelector("nav");

window.addEventListener("scroll", (e) => {
    var navHeight = document.querySelector("nav").offsetHeight;
    // 两种写法 better cross-browser compatibility for pageYOffset than scrollY
    // if (window.scrollY >= navHeight) {
    if (window.pageYOffset >= navHeight) {
        navbar.classList.add("sticky");
    } else {
        navbar.classList.remove("sticky");
    }
});
