function toggleMenu() {
    let menu = document.getElementById("sideMenu");
    let button = document.getElementById("menu-button");

    menu.classList.toggle("active");
    button.classList.toggle("open");
}