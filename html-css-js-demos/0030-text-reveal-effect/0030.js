var active = false;
const textContent = document.querySelector("p");
const text = baffle(".baffle");
text.set({
    characters: '~#+-=!"?<>.,;:£$%^&*/()[]|lyzsk',
    speed: 100,
});

// https://camwiegert.github.io/baffle/
textContent.addEventListener("mousedown", function () {
    if (active === false) {
        active === true;
        text.start();
        text.reveal(2500);
    } else {
        active === false;
        text.once();
        text.end();
    }
});
