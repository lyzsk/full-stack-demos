const square = new mojs.Shape({
    radius: 70,
    radiusY: 70,
    shape: "rect",
    stroke: "#2ECC40",
    strokeWidth: { 10: 50 },
    fill: "none",
    scale: { 0.45: 0.55 },
    opacity: { 1: 0 },
    duration: 350,
    easing: "sin.out",
    isShowEnd: false,
});

const lines = new mojs.Burst({
    left: 0,
    top: 0,
    radius: { 35: 75 },
    angle: 0,
    count: 8,
    children: {
        shape: "line",
        radius: 10,
        scale: 1,
        stroke: "#2Ecc40",
        strokeDasharray: "100%",
        strokeDashoffset: { "-100%": "100%" },
        duration: 700,
        easing: "quad.out",
    },
});

const checkLabel = document.querySelector("label");
let checked = checkLabel.checked;

checkLabel.addEventListener("click", function () {
    if (!checked) {
        const pos = this.getBoundingClientRect();

        const timeline = new mojs.Timeline({ speed: 1.5 });

        timeline.add(square, lines);

        square.tune({
            left: pos.left + 35,
            top: pos.top + 35,
        });
        lines.tune({
            x: pos.left + 35,
            y: pos.top + 35,
        });

        if ("vibrate" in navigator) {
            navigator.vibrate =
                navigator.vibrate ||
                navigator.webkitVibrate ||
                navigator.mozVibrate ||
                navigator.msVibrate;

            navigator.vibrate([100, 200, 400]);
        }

        timeline.play();
    }
    checked = !checked;
});

const checkBox = document.getElementById("checkbox");
checkBox.addEventListener("click", function () {
    if (checkBox.checked) {
        document.querySelector("img").src = "./images/98805198_p0.jpg";
    } else {
        document.querySelector("img").src = "./images/98805198_p1.jpg";
    }
});
