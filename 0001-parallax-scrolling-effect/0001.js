gsap.registerPlugin(ScrollTrigger);

let container = document.querySelector(".main-container"),
    slides = gsap.utils.toArray(".slide"),
    getRatio = (el) =>
        window.innerHeight / (window.innerHeight + el.offsetHeight);

slides.forEach((slide, i) => {
    let bg = slide.querySelector(".background"),
        textBox = slide.querySelector(".text-box"),
        tl = gsap.timeline({
            scrollTrigger: {
                trigger: slide,
                start: () => (i ? "top bottom" : "top top"),
                end: "bottom top",
                scrub: true,
                invalidateOnRefresh: true,
                // markers: true,
            },
        });

    tl.fromTo(
        bg,
        {
            y: () => (i ? -window.innerHeight * getRatio(slide) : 0),
        },
        {
            y: () => window.innerHeight * (1 - getRatio(slide)),
            ease: "none",
        }
    );
    tl.fromTo(
        textBox,
        {
            y: () => (i ? window.innerHeight * -getRatio(slide) * 2 : 0),
        },
        {
            y: () => window.innerHeight * getRatio(slide) * 2,
            ease: "none",
        },
        0
    );
});
