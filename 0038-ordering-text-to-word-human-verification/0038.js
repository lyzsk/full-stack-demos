var box = document.querySelector(".box");
var btmTextBox = document.querySelector(".btm-text-box");
var fls = document.querySelectorAll(".fl");
var radios = document.querySelectorAll(".radio");

var arr = [
    "水煮肉片",
    "酸菜鱼",
    "地三鲜",
    "盖浇饭",
    "鱼香茄子",
    "兰州拉面",
    "蛋炒饭",
    "咖喱鸡",
    "干炒牛河",
    "荔枝肉",
    "麻婆豆腐",
    "蒜泥白肉",
    "干锅肥肠",
];

var dic =
    "泥雷销诗咖指便豆团往干晶厚盟衡字牛触映封材排供河态蒜麻腐荔末宪庆编探滑镇饭浓航怀赶附盖获茶鲜粮肠另施减鸡孙延危胶还斤孩脱硫肥叶几九区强放决西被约支般从感劳鱼波视仅费紧爱左章游久菜味旧模湖货损预炒浇露杆锅转更单风切打白教速花酸层枝片婆始却专状育厂京识肉践尼衣宽扬棉希伤操由煮其些然前外天政四日去把性好应开它合还因两体制机当使点史雨婷的一喱是在不了有和人这中二理起小物现实加量都大为上个国我以要他时来用们生到作地于出就分对成会可主发年动同工也能下过子说产种面而方后多定度家电力里如水化高自行学法所民得经十三之进着等部";

// 重置元素
function clear() {
    fls.forEach((fl) => {
        fl.style.display = "none";
    });
    radios.forEach((radio) => {
        radio.style.opacity = 0;
    });
}

function start() {
    box.innerHTML = '<div class="img-box"></div>';
    btmTextBox.innerHTML = "";
    btmTextBox.style.color = "rgb(0, 0, 0)";

    // 需要点击的词语 (ES6模板字符替换字符用的是`!而不是引号)
    let words = arr[Math.floor(Math.random() * arr.length)];
    btmTextBox.innerHTML = `请依次点击: <span>${words}</span>`;

    let timer;
    let position = [
        { left: "120px", top: "0px" },
        { left: "160px", top: "20px" },
        { left: "80px", top: "200px" },
        { left: "240px", top: "260px" },
        { left: "40px", top: "260px" },
        { left: "240px", top: "50px" },
        { left: "40px", top: "150px" },
        { left: "40px", top: "110px" },
        { left: "160px", top: "120px" },
    ];
    // 随机位置
    position.sort(() => {
        return Math.random() - 0.5;
    });

    let verify = [];
    let extra_str = "";
    for (let i = 0; i < 5; i++) {
        extra_str += dic[Math.floor(Math.random() * dic.length)];
    }
    extra_str = words + extra_str;
    for (i in extra_str) {
        verify.push(i);
        let left = Math.floor(Math.random() * (i * 10));
        let top = Math.floor(Math.random() * (i * 10));

        // 每个字一个div
        let divs = document.createElement("div");
        divs.classList.add("fl");
        divs.style.left = position[i].left;
        divs.style.top = position[i].top;

        // 设置每个字的div里设置 span 绝对定位
        let span = document.createElement("span");
        span.innerText = extra_str[i];
        span.style.left = left + "px";
        span.style.top = top + "px";

        span.setAttribute("data-index", i);
        span.setAttribute("data-judge", true);
        divs.appendChild(span);
        box.appendChild(divs);
    }

    let cspan = [];
    let fspans = box.querySelectorAll(".fl span");
    fspans.forEach((fspan) => {
        // 每个字添加点击事件
        fspan.addEventListener("click", function () {
            if (fspan.getAttribute("data-judge")) {
                cspan.push(fspan.getAttribute("data-index"));
                fspan.setAttribute("data-judge", false);
            } else {
                console.log("重复点击");
            }
        });
    });

    let cnt = 0;
    box.addEventListener("click", function (event) {
        cnt++;
        let radios = document.createElement("div");
        radios.classList.add("radio");
        radios.innerText = cnt;
        radios.style.left = event.pageX - box.offsetLeft - 20 + "px";
        radios.style.top = event.pageY - box.offsetTop - 20 + "px";
        box.appendChild(radios);
        if (cnt == words.length) {
            if (cspan.join() == verify.slice(0, words.length).join()) {
                btmTextBox.innerHTML = "验证成功!";
                btmTextBox.style.color = "var(--c-success)";
                clear();
            } else {
                btmTextBox.innerHTML = "验证失败!";
                btmTextBox.style.color = "var(--c-fail)";
                clear();
            }
            // 清除定时器
            clearTimeout(timer);
            // 完成后设置定时任务 1s 后执行start()
            timer = setTimeout(() => {
                start();
            }, 1000);
            // 重置选择计数
            cnt = 0;
        }
    });
}

start();
