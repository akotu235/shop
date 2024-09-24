package io.github.akotu235.shop.configuration;

import io.github.akotu235.shop.configuration.resolver.CustomLocaleResolver;
import io.github.akotu235.shop.properties.AppConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.*;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    private final Environment environment;
    private final AppConfigurationProperties config;

    public MessageConfig(Environment environment, AppConfigurationProperties config) {
        this.environment = environment;
        this.config = config;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/lang");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.ENGLISH);
        String urlMessage = environment.getProperty("app.url");
        Properties properties = new Properties();
        properties.put("url.home", urlMessage);
        messageSource.setCommonMessages(properties);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    @Bean
    public List<Locale> supportedLocales() {
        List<Locale> locales = new ArrayList<>();
        for (String lang : config.getSupportedLanguages()) {
            locales.add(new Locale(lang));
        }
        return locales;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
}