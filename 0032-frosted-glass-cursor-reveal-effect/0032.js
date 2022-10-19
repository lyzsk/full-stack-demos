const mainContainer = document.querySelector(".main-container");
const cursor = document.querySelector(".cursor");

mainContainer.addEventListener("mousemove", function (e) {
    // 获取 mainContainer 和 cursor 的大小及其相对于视口的位置
    let contRect = mainContainer.getBoundingClientRect();
    let cursRect = cursor.getBoundingClientRect();
    // 计算cursor的位置
    let cursX = e.clientX - contRect.left - cursRect.width / 2;
    let cursY = e.clientY - contRect.top - cursRect.height / 2;
    cursor.style.left = cursX + "px";
    cursor.style.top = cursY + "px";
});
