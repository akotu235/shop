package io.github.akotu235.shop.configuration;


import io.github.akotu235.shop.service.theme.ThemeService;
import io.github.akotu235.shop.service.theme.configuration.ThemeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ThemeConfig implements WebMvcConfigurer {
    private final ThemeService themeService;

    public ThemeConfig(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ThemeInterceptor themeInterceptor = new ThemeInterceptor(themeService);
        registry.addInterceptor(themeInterceptor);
    }
}