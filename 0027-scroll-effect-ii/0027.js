gsap.utils.toArray(".main-container").forEach((imgBox) => {
    let tl = gsap.timeline({
        scrollTrigger: {
            trigger: imgBox,
            start: "center center",
            // makes the height of the scrolling (while pinning) match the width, thus the speed remains constant (vertical/horizontal)
            end: () => "+=" + imgBox.offsetWidth,
            scrub: true,
            pin: true,
            anticipatePin: 1,
            // markers: true,
        },
        defaults: { ease: "none" },
    });
    // animate the container
    tl.fromTo(
        imgBox.querySelector(".after-img"),
        { xPercent: 100, x: 0 },
        { xPercent: 0 }
    ).fromTo(
        imgBox.querySelector(".after-img img"),
        { xPercent: -100, x: 0 },
        { xPercent: 0 },
        0
    );
});
