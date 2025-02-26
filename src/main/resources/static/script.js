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
const langArrow = document.getElementById('lang-arrow');

language.addEventListener('click', () => {
    if (languageContainer.style.display === 'flex') {
        languageContainer.style.display = 'none';
        langArrow.textContent = '▾';
    } else {
        languageContainer.style.display = 'flex';
        langArrow.textContent = '▴';
    }
});


//search-bar
const searchInput = document.getElementById('search-input');
window.onload = function () {
    searchInput.focus();
};