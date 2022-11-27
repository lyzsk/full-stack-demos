// const list = [
//     {
//         id: 1,
//         name: "谢谢惠顾",
//         is: true,
//         url: "url('./images/102770412_p5.jpg')",
//     },
//     {
//         id: 2,
//         name: "1元优惠券",
//         is: true,
//         url: "url('./images/102770412_p6.jpg')",
//     },
//     {
//         id: 3,
//         name: "10元优惠券",
//         is: true,
//         url: "url('./images/102770412_p7.jpg')",
//     },
//     {
//         id: 4,
//         name: "汤臣一品房子一套",
//         url: "url('./images/102790057_p0.png')",
//     },
//     { id: 5, name: "法拉利430", url: "url('./images/102790057_p1.png')" },
//     { id: 6, name: "100万代金券", url: "url('./images/102790057_p2.png')" },
//     {
//         id: 7,
//         name: "外星人游戏笔记本",
//         url: "url('./images/102790057_p3.png')",
//     },
//     { id: 8, name: "iphone14", url: "url('./images/102790057_p4.png')" },
//     { id: 9, name: "欧洲十日游", url: "url('./images/102790057_p5.png')" },
// ].sort((v) => Math.random() - 0.5);
// const winPrize = list.filter((v) => v.is);
// const noPrize = list.filter((v) => !v.is);
// let count = 3;
// const box = document.querySelector(".box");
// const countEL = document.querySelector("#count");
// countEL.innerHTML = count;
// box.innerHTML = list
//     .map((v) => {
//         return `<div class="item" onclick="fp(this)">
//     <div class="style1">抽奖</div>
//     <div class="style2"></div>
//     </div>`;
//     })
//     .join("");
// // 翻牌抽奖
// const fp = (me) => {
//     if (count === 0) {
//         return;
//     }
//     me.querySelector(".style1").classList.add(".hide");
//     // 抽奖动画
//     setTimeout(() => {
//         // 从非中奖项中取最后一个，并从数组中移除该项
//         const item = winPrize.pop();
//         const style2 = me.querySelector(".style2");
//         style2.innerHTML = item.name;
//         style2.style.backgroundImage = item.url;
//         style2.classList.add(".show");
//     }, 400);
//     count--;
//     countEL.innerHTML = count;
//     // 当抽奖次数为0的时候，就把剩余的奖项全部展示出来
//     if (count === 0) {
//         setTimeout(() => {
//             box.querySelector(".style1").forEach((v) => {
//                 v.classList.add("hide");
//                 if (v.nextElementSibling.className.indexOf("show") === -1) {
//                     const item = noArr.pop();
//                     v.nextElementSibling.innerHTML = item.name;
//                 }
//                 setTimeout(() => {
//                     box.querySelectorAll(".style2").forEach((v) => {
//                         if (v.className.indexOf("show") === -1) {
//                             v.classList.add("show-result");
//                         }
//                     });
//                 }, 400);
//             }, 1000);
//         });
//     }
// };
// 奖品列表
const list = [
    { id: 1, name: "1元优惠券", is: true },
    { id: 2, name: "10元优惠券", is: true },
    { id: 3, name: "谢谢惠顾", is: true },
    { id: 4, name: "豪华电动车" },
    { id: 5, name: "1w购物券" },
    { id: 6, name: "5w购物券" },
    { id: 7, name: "豪华轿车" },
    { id: 8, name: "房子一套" },
    { id: 9, name: "顶配笔记本" },
].sort((v) => Math.random() - 0.5);
// 中奖项
const isArr = list.filter((v) => v.is);
// 非中奖项
const noArr = list.filter((v) => !v.is);
// 抽奖次数
let count = 3;
const box = document.querySelector(".box");
const countEL = document.querySelector("#count");
countEL.innerHTML = count;
box.innerHTML = list
    .map((v) => {
        return `<div class="item" onclick="fp(this)">
            <div class="style1">抽奖</div>
            <div class="style2"></div>
        </div>`;
    })
    .join("");
// 翻牌抽奖
const fp = (me) => {
    if (count === 0) {
        return;
    }
    me.querySelector(".style1").classList.add("hide");
    // 抽奖动画
    setTimeout(() => {
        // 从非中奖项中取最后一个，并从数组中移除该项
        const item = isArr.pop();
        const style2 = me.querySelector(".style2");
        style2.innerHTML = item.name;
        style2.classList.add("show");
    }, 400);
    count--;
    countEL.innerHTML = count;
    // 当抽奖次数为0的时候，就把剩余的奖项全部展示出来
    if (count === 0) {
        setTimeout(() => {
            box.querySelectorAll(".style1").forEach((v) => {
                v.classList.add("hide");
                if (v.nextElementSibling.className.indexOf("show") === -1) {
                    const item = noArr.pop();
                    v.nextElementSibling.innerHTML = item.name;
                }
            });
            setTimeout(() => {
                box.querySelectorAll(".style2").forEach((v) => {
                    if (v.className.indexOf("show") === -1) {
                        v.classList.add("show-result");
                    }
                });
            }, 400);
        }, 1000);
    }
};
