// 创建一个和页面尺寸一样的画布
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
// 创建img对象
var img = new Image();
img.src = "./images/102198528_p0.jpg";
// 粒子数组
var particleArray = [];
// 给img对象添加监听事件
img.addEventListener("load", function () {
    var ic = document.createElement("canvas");
    var ictx = ic.getContext("2d");
    ic.width = img.width;
    ic.height = img.height;
    ictx.drawImage(img, 0, 0, img.width, img.height);
    var imgdata = ictx.getImageData(0, 0, img.width, img.height);
    ic.style.zIndex = "-1";
    var imgArray = [];
    for (var i = 0; i < imgdata.data.length / 4; i++) {
        imgArray.push(i);
    }
    // 每次调用 随机选取像素数组中的元素
    function newparticle() {
        for (var c = 0; c < 100; c++) {
            var ind = (Math.random() * imgArray.length) | 0;
            var ci = imgArray[ind];
            // 根据元素值计算图片具体位置
            var cx = ci % img.width;
            var cy = (ci / img.width) | 0;
            // 通过imgdata的信息获取对应坐标点的颜色值
            var r = imgdata.data[ci * 4];
            var g = imgdata.data[ci * 4 + 1];
            var b = imgdata.data[ci * 4 + 2];
            var color = "rgb(" + r + "," + g + "," + b + ")";
            particleArray.push(
                new Particle(
                    cx + (canvas.width - img.width) / 2,
                    cy + (canvas.height - img.height) / 2,
                    color
                )
            );
            // 要在画布中移除对应的像素
            ictx.clearRect(cx, cy, 1, 1);
            // 同时再数组中删除这个元素, 以保证每个像素只可能被选中一次
            imgArray.splice(ind, 1);
            for (var i = 0; i < 10; i++) {
                var ind = (Math.random() * imgArray.length) | 0;
                var ci = imgArray[ind];
                var cx = ci % img.width;
                var cy = (ci / img.width) | 0;
                ictx.clearRect(cx, cy, 1, 1);
                imgArray.splice(ind, 1);
            }
        }
    }
    function draw() {
        ctx.fillStyle = "rgba(0,0,0,0.5)";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(
            ic,
            (canvas.width - ic.width) / 2,
            (canvas.height - ic.height) / 2,
            ic.width,
            ic.height
        );
        newparticle();
        for (var i = 0; i < particleArray.length; i++) {
            var particle = particleArray[i];
            particle.update();
            particle.draw();
            // 防止生成过多粒子
            if (particle.age > 50) {
                particleArray.splice(i, 1);
            }
        }
        requestAnimationFrame(draw);
    }
    draw();
});
// 一次性把所有pixel像素转成particle粒子性能消耗巨大
// 所以采用分步方式, 每次选取若干像素生成粒子直到所有像素转成粒子
class Particle {
    // 让右上方向飘, 所以构造器里面vy是减, vx是加
    constructor(x, y, c) {
        this.x = x;
        this.y = y;
        this.color = c;
        this.vy = -Math.random();
        this.vx = Math.random();
        this.age = 0;
    }
    // 每次调用除了移动粒子, 通过 *= 1.05 让移动速度也增加5%
    update() {
        this.x += this.vx;
        this.y += this.vy;
        this.vx *= 1.05;
        this.vy *= 1.05;
        this.age++;
    }
    draw() {
        ctx.beginPath();
        ctx.fillStyle = this.color;
        ctx.arc(this.x, this.y, this.vx / 3, 0, Math.PI * 2, 0);
        ctx.fill();
    }
}
