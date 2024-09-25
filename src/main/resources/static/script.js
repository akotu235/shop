//mobile-menu-button
function toggleMenu() {
    let menu = document.getElementById("sideMenu");
    let button = document.getElementById("menu-button");

    menu.classList.toggle("active");
    button.classList.toggle("open");
}

//lang-button
const language = document.getElementById('language');
const languageContainer = document.getElementById('language-container');

language.addEventListener('mouseenter', () => {
    language.style.display = 'none';
    languageContainer.style.display = 'flex';
});

languageContainer.addEventListener('mouseleave', () => {
    language.style.display = 'flex';
    languageContainer.style.display = 'none';
});


//search-bar
const searchInput = document.getElementById('search-input');
window.onload = function () {
    searchInput.focus();
};