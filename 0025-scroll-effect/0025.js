gsap.registerPlugin(ScrollTrigger);
const topLayer = gsap.utils.toArray(".top-layer");

topLayer.forEach((layer, i) => {
    ScrollTrigger.create({
        trigger: layer,
        toggleClass: "active",
        start: "top 100%",
        end: "top -50%",
        // markers: true,
    });
});
