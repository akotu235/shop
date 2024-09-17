package io.github.akotu235.shop.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;
import java.util.Properties;

@Configuration
public class MessageConfig implements WebMvcConfigurer {

    private final Environment environment;

    public MessageConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("lang");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.ENGLISH);
        messageSource.getMessage("greeting", null, Locale.getDefault());
        String urlMessage = environment.getProperty("app.url");
        Properties properties = new Properties();
        properties.put("url.home", urlMessage);
        messageSource.setCommonMessages(properties);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
    }
}