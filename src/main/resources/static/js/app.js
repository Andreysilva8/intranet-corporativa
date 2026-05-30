(function () {
    var toggle = document.querySelector("[data-sidebar-toggle]");
    var sidebar = document.querySelector("[data-sidebar]");

    if (toggle && sidebar) {
        toggle.addEventListener("click", function () {
            sidebar.classList.toggle("is-open");
        });
    }

    document.addEventListener("click", function (event) {
        if (!sidebar || !sidebar.classList.contains("is-open")) {
            return;
        }

        var clickedInsideSidebar = sidebar.contains(event.target);
        var clickedToggle = toggle && toggle.contains(event.target);

        if (!clickedInsideSidebar && !clickedToggle) {
            sidebar.classList.remove("is-open");
        }
    });
}());
