$(function () {
    var Page = (function () {
        var $navArrows = $("#nav-arrows").hide(),
            $navDots = $("#nav-dots").hide(),
            $nav = $navDots.children("span"),
            $shadow = $("#shadow").hide(),
            slicebox = $("#sb-slider").slicebox({
                onReady: function () {
                    $navArrows.show();
                    $navDots.show();
                    $shadow.show();
                },
                onBeforeChange: function (pos) {
                    $nav.removeClass("nav-dot-current");
                    $nav.eq(pos).addClass("nav-dot-current");
                },
            }),
            init = function () {
                initEvents();
            },
            initEvents = function () {
                // add navigation events
                $navArrows.children(":first").on("click", function () {
                    slicebox.next();
                    return false;
                });

                $navArrows.children(":last").on("click", function () {
                    slicebox.previous();
                    return false;
                });

                $nav.each(function (i) {
                    $(this).on("click", function (event) {
                        var $dot = $(this);

                        if (!slicebox.isActive()) {
                            $nav.removeClass("nav-dot-current");
                            $dot.addClass("nav-dot-current");
                        }

                        slicebox.jump(i + 1);
                        return false;
                    });
                });
            };

        return { init: init };
    })();

    Page.init();
});
