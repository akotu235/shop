package io.github.akotu235.shop.controller;


import io.github.akotu235.shop.service.theme.ThemeService;
import io.github.akotu235.shop.service.theme.model.Theme;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/theme")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/{themeName}")
    public String setDayTheme(@PathVariable String themeName,
                              HttpServletResponse response) {
        Theme theme = themeService.getTheme(themeName);
        Cookie cookie = new Cookie("theme", theme.getName());
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
