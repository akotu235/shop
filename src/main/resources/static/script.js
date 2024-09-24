function toggleMenu() {
    let menu = document.getElementById("sideMenu");
    let button = document.getElementById("menu-button");

    menu.classList.toggle("active");
    button.classList.toggle("open");
}


const language = document.getElementById('language');
const languageContainer = document.getElementById('language-container');
const body = document.getElementsByTagName('body')[0];

language.addEventListener('mouseenter', () => {
    language.style.display = 'none';
    languageContainer.style.display = 'flex';
});

languageContainer.addEventListener('mouseleave', () => {
    language.style.display = 'flex';
    languageContainer.style.display = 'none';
});