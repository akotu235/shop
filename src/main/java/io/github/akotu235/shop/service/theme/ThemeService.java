package io.github.akotu235.shop.service.theme;

import io.github.akotu235.shop.service.theme.model.Theme;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ThemeService {
    private final Map<String, Theme> themes;

    public ThemeService() {
        themes = load();
    }

    public Theme getTheme(Object value) {
        if (value != null && themes.get(value.toString()) != null) {
            return themes.get(value.toString());
        }
        return getDefaultTheme();
    }

    public Theme getThemeFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("theme".equals(cookie.getName())) {
                    return getTheme(cookie.getValue());
                }
            }
        }
        return getDefaultTheme();
    }

    public boolean isValidName(String name) {
        return themes.containsKey(name);
    }

    public Theme getDefaultTheme() {
        return themes.get("classic");
    }

    private static Map<String, Theme> load() {
        Properties properties = loadYamlProperties();
        Set<String> themeNames = getThemeNames(properties);
        Map<String, Theme> themesMap = new HashMap<>();
        for (String name : themeNames) {
            themesMap.put(name, new Theme(name, properties));
        }
        return themesMap;
    }

    private static Set<String> getThemeNames(Properties properties) {
        Set<String> themes = new HashSet<>();
        for (Object key : properties.keySet()) {
            String klucz = (String) key;
            if (klucz.contains(".")) {
                String themeName = klucz.split("\\.")[0];
                themes.add(themeName);
            }
        }
        return themes;
    }

    private static Properties loadYamlProperties() {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        Resource resource = new ClassPathResource("theme.yml");
        yamlFactory.setResources(resource);
        return yamlFactory.getObject();
    }
}
