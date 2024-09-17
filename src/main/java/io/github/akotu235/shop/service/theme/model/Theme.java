package io.github.akotu235.shop.service.theme.model;

import java.util.Properties;

public class Theme {
    private final String name;
    private final String primary;
    private final String secondary;
    private final String background;
    private final String font;
    private final String highlighted;
    private final String buttonFont;
    private final String headerFont;
    private final String headerHighlightedFont;

    public Theme(String name, Properties properties) {
        this.name = name;
        this.primary = getColor(properties, name, "primary");
        this.secondary = getColor(properties, name, "secondary");
        this.background = getColor(properties, name, "background");
        this.font = getColor(properties, name, "font");
        this.highlighted = getColor(properties, name, "highlighted");
        this.buttonFont = getColor(properties, name, "button-font");
        this.headerFont = getColor(properties, name, "header-font");
        this.headerHighlightedFont = getColor(properties, name, "header-highlighted-font");
    }

    private static String getColor(Properties properties, String theme, String key) {
        return properties.getProperty(theme + "." + key);
    }

    public String getName() {
        return name;
    }

    public String getPrimary() {
        return primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public String getBackground() {
        return background;
    }

    public String getFont() {
        return font;
    }

    public String getHighlighted() {
        return highlighted;
    }

    public String getButtonFont() {
        return buttonFont;
    }

    public String getHeaderFont() {
        return headerFont;
    }

    public String getHeaderHighlightedFont() {
        return headerHighlightedFont;
    }

    @Override
    public String toString() {
        return name;
    }
}