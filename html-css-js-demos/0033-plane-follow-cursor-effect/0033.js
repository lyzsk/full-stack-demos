var plane = document.getElementById("plane");
var deg = 0,
    ex = 0,
    ey = 0,
    vx = 0,
    vy = 0,
    count = 0;
window.addEventListener("mousemove", (e) => {
    ex = e.x - plane.offsetLeft - plane.clientWidth / 2;
    ey = e.y - plane.offsetTop - plane.clientHeight / 2;
    deg = (360 * Math.atan(ey / ex)) / (2 * Math.PI) + 45;
    if (ex < 0) {
        deg += 180;
    }
    count = 0;
});
function draw() {
    plane.style.transform = "rotate(" + deg + "deg)";
    if (count < 100) {
        vx += ex / 100;
        vy += ey / 100;
    }
    plane.style.left = vx + "px";
    plane.style.top = vy + "px";
    count++;
}
setInterval(draw, 1);
