package io.github.akotu235.shop.service.theme.configuration;


import io.github.akotu235.shop.service.theme.model.Theme;
import io.github.akotu235.shop.service.theme.ThemeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ThemeInterceptor implements HandlerInterceptor {
    private final ThemeService themeService;

    public ThemeInterceptor(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("theme", themeService.getThemeFromCookie(request).toString());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            Theme theme = themeService.getTheme(request.getAttribute("theme"));
            modelAndView.addObject("theme", theme);
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
