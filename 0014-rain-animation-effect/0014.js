const rainBox = document.querySelector(".rain-box");
var rainBoxHeight = rainBox.clientHeight;
var rainBoxWidth = rainBox.clientWidth;

// 页面大小变化时, 改变box的h, w与之对应
window.onresize = function () {
    rainBoxHeight = rainBox.clientHeight;
    rainBoxWidth = rainBox.clientWidth;
};

// 每隔一段时间, 添加rain
setInterval(() => {
    const rain = document.createElement("div");
    rain.classList.add("rain");
    rain.style.top = 0;
    // 随机生成x轴位置
    rain.style.left = Math.random() * rainBoxWidth + "px";
    // 随机生成透明度
    rain.style.opacity = Math.random();
    rainBox.appendChild(rain);

    // 设置rain下落间隔时间
    var race = 1;
    const timer = setInterval(() => {
        if (parseInt(rain.style.top) > rainBoxHeight) {
            clearInterval(timer);
            rainBox.removeChild(rain);
        }
        ++race;
        rain.style.top = parseInt(rain.style.top) + race + "px";
    }, 20);
}, 50);
