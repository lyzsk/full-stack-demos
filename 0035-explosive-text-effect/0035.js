var canvas = document.getElementById("canvas");
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
var ctx = canvas.getContext("2d");
var strs = [
    "しゃけ",
    "おかか",
    "こんぶ",
    "ツナ",
    "ツナマヨ",
    "明太子",
    "高菜",
    "いくら",
    "すじこ",
    "爆ぜろ",
    "潰れろ",
    "動くな",
    "眠れ",
];
var colors = [
    "rgb(64, 66, 88)",
    "rgb(71, 78, 104)",
    "rgb(80, 87, 122)",
    "rgb(107, 114, 142)",
    "rgb(63, 59, 108)",
    "rgb(98, 79, 130)",
    "rgb(159, 115, 171)",
    "rgb(108, 74, 182)",
    "rgb(141, 114, 225)",
    "rgb(141, 158, 255)",
    "rgb(233, 119, 119)",
    "rgb(252, 221, 176)",
    "rgb(255, 250, 215)",
    "rgb(253, 132, 31)",
    "rgb(242, 222, 186)",
    "rgb(255, 143, 177)",
    "rgb(247, 164, 164)",
    "rgb(239, 245, 245)",
    "rgb(214, 228, 229)",
    "rgb(73, 113, 116)",
    "rgb(235, 100, 64)",
    "rgb(0, 173, 181)",
    "rgb(249, 237, 105)",
    "rgb(184, 59, 94)",
    "rgb(240, 138, 93)",
    "rgb(106, 44, 112)",
    "rgb(234, 234, 234)",
    "rgb(255, 46, 99)",
    "rgb(135, 133, 162)",
    "rgb(255, 226, 226)",
    "rgb(220, 214, 247)",
    "rgb(108, 91, 123)",
    "rgb(246, 114, 128)",
    "rgb(211, 224, 220)",
];
var pixs = [];
var index = 0;
var textcanvas = document.createElement("canvas");
var tctx = textcanvas.getContext("2d");
// 粒子数组对象
var particleArray = [];

// 建立一个span, 绑定check样式
// 获取w, h 后删掉元素, 返回尺寸数据
function check(str) {
    var span = document.createElement("span");
    span.className = "check";
    span.textContent = str;
    document.body.appendChild(span);
    var w = span.clientWidth;
    var h = span.clientHeight;
    document.body.removeChild(span);
    return [w, h];
}
// 文字转为图片
// 根据check函数获取的尺寸, 将文字写入画布
function textdata(i) {
    var textsize = check(strs[i]);
    textcanvas.width = textsize[0];
    textcanvas.height = textsize[1];
    tctx.clearRect(0, 0, textcanvas.width, textcanvas.height);
    // 将文字属性设置为与span一致
    tctx.font = "40px Noto Sans Japanese";
    tctx.fillStyle = "white";
    tctx.fillText(strs[i], 0, 40);
    return tctx.getImageData(0, 0, textcanvas.width, textcanvas.height);
}

class Particle {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.vx = 0.5 - Math.random();
        // y轴向量值先向上运动
        this.vy = Math.random() - 1.5;
        // 重力值
        this.g = 0.03;
        this.color = colors[(Math.random() * colors.length) | 0];
        // 延迟参数, 用于散开前固定一小段时间
        this.wait = 60;
    }
    update() {
        // 当wait值归零后, 开始变更坐标值
        if (this.wait < 0) {
            this.x += this.vx * 2;
            this.y += this.vy * 2;
            this.vy += this.g;
        }
        this.wait--;
    }
    draw() {
        ctx.beginPath();
        ctx.arc(this.x, this.y, 2, 0, Math.PI * 2);
        ctx.fillStyle = this.color;
        ctx.fill();
    }
}

function init() {
    pixs = textdata(index % strs.length);
    for (var i = 0; i < pixs.data.length / 4; i++) {
        // 如果透明度 != 0 根据顺序生成粒子坐标
        // 原尺寸太小了, *5放大
        // 根据生成的x, y， 放入粒子类的constructor生成画布
        if (pixs.data[i * 4 + 3] != 0) {
            x =
                (i % textcanvas.width) * 5 +
                (canvas.width - textcanvas.width * 5) / 2;
            y =
                ((i / textcanvas.width) | 0) * 5 +
                (canvas.height - textcanvas.height * 5) / 2;
            particleArray.push(new Particle(x, y));
        }
    }
    index++;
}
// 根据粒子数组的内容挨个绘制
function draw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    for (var i = 0; i < particleArray.length; i++) {
        var particle = particleArray[i];
        particle.update();
        particle.draw();
        if (particle.y > canvas.height) {
            particleArray.splice(i, 1);
        }
        // 当粒子全部消失后, 重新init()
        if (particleArray.length == 0) {
            init();
        }
    }
}
init();
setInterval(draw, 10);
