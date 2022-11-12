var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var img = new Image();
img.src = "./images/85093033_p0.jpg";
var particleArray = [];
img.addEventListener("load", function () {
    canvas.width = img.width;
    canvas.height = img.height;
    ctx.drawImage(img, 0, 0, img.width, img.height);
    var imgdata = ctx.getImageData(0, 0, img.width, img.height);
    ctx.clearRect(0, 0, img.width, img.height);
    class Particle {
        constructor() {
            this.x = (Math.random() * canvas.width) | 0;
            this.y = 0;
            this.vy = 1;
            this.size = Math.random() * 3;
            this.color = "white";
        }
        update() {
            this.y += this.vy;
            if (this.y >= canvas.height) {
                this.y = 0;
                this.x = (Math.random() * canvas.width) | 0;
            }
            var data = check(this.x, this.y);
            this.vy = data[0];
            this.color = data[1];
        }
        draw() {
            ctx.beginPath();
            ctx.fillStyle = this.color;
            ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2, 0);
            ctx.fill();
        }
    }

    function check(x, y) {
        var ind = y * canvas.width + x;
        var r = imgdata.data[ind * 4];
        var g = imgdata.data[ind * 4 + 1];
        var b = imgdata.data[ind * 4 + 2];
        var light = (((0.3 * r + 0.59 * g + 0.11 * b) / 255) * 5 + 1) | 0;
        var color = "rgb(" + r + "," + g + "," + b + ")";
        return [light, color];
    }

    function draw() {
        ctx.fillStyle = "rgba(0,0,0,0.1)";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        for (var i = 0; i < particleArray.length; i++) {
            var particle = particleArray[i];
            particle.update();
            particle.draw();
        }
    }
    for (var i = 0; i < 8000; i++) {
        particleArray.push(new Particle());
    }
    setInterval(draw, 1);
});
