package io.github.akotu235.shop.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.password")
public class PasswordConfigurationProperties {
    private int minLength;
    private boolean requireUppercase;
    private boolean requireNumeric;


    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public boolean isRequireUppercase() {
        return requireUppercase;
    }

    public void setRequireUppercase(boolean requireUppercase) {
        this.requireUppercase = requireUppercase;
    }

    public boolean isRequireNumeric() {
        return requireNumeric;
    }

    public void setRequireNumeric(boolean requireNumeric) {
        this.requireNumeric = requireNumeric;
    }
}