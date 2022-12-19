var active = false;
var middleScroller = document.querySelector(".middle-scroller");
var topScroller = document.querySelector(".top-scroller");

// 添加 mousedown, mouseup, mouseleave 监听事件, 触发时添加或移除 sliding
middleScroller.addEventListener("mousedown", function () {
    active = "middle";
    middleScroller.classList.add("sliding");
});
document.body.addEventListener("mouseup", function () {
    active = false;
    middleScroller.classList.remove("sliding");
});
document.body.addEventListener("mouseleave", function () {
    active = false;
    middleScroller.classList.remove("sliding");
});
topScroller.addEventListener("mousedown", function () {
    active = "top";
    topScroller.classList.add("sliding");
});
document.body.addEventListener("mouseup", function () {
    active = false;
    topScroller.classList.remove("sliding");
});
document.body.addEventListener("mouseleave", function () {
    active = false;
    topScroller.classList.remove("sliding");
});

// get x coordinate value when mousemove
document.body.addEventListener("mousemove", function (e) {
    if (!active) {
        return;
    }
    let x = e.pageX;
    x -= document.querySelector(".img-box").getBoundingClientRect().left;
    slideStart(x);
});

function slideStart(x) {
    let transform = Math.max(
        0,
        Math.min(x, document.querySelector(".img-box").offsetWidth)
    );
    if (active === "middle") {
        document.querySelector(".middle").style.width = transform + "px";
        middleScroller.style.left = transform - 25 + "px";
        // 如果中间层滑块和最顶层滑块相交，根据中间层的滑动距离设置最顶层的滑动距离（一起滑动）
        if (
            topScroller.getBoundingClientRect().left >
            middleScroller.getBoundingClientRect().left - 5
        ) {
            document.querySelector(".top").style.width = transform - 5 + "px";
            topScroller.style.left = transform - 30 + "px";
        }
    }
    if (active === "top") {
        document.querySelector(".top").style.width = transform + "px";
        topScroller.style.left = transform - 25 + "px";
        // 如果最顶层滑块和中间层滑块相交，根据最顶层的滑动距离设置中间层的滑动距离（一起滑动）
        if (
            topScroller.getBoundingClientRect().left >
            middleScroller.getBoundingClientRect().left - 5
        ) {
            document.querySelector(".middle").style.width =
                transform + 5 + "px";
            middleScroller.style.left = transform - 20 + "px";
        }
    }
}

// init
active = "middle";
slideStart(363);
active = "top";
slideStart(1);
active = false;
