package com.suhong.khanacademybadges;

public class Badge {

    private String name;
    private int category;
    private String description;
    private int points;
    private String iconCompactUrl;
    private String iconLargeUrl;

    public Badge(String name, int category, String description, int points, String iconCompactUrl, String iconLargeUrl) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.points = points;
        this.iconCompactUrl = iconCompactUrl;
        this.iconLargeUrl = iconLargeUrl;
    }

    public int getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public String getIconCompactUrl() {
        return iconCompactUrl;
    }

    public String getIconLargeUrl() {
        return iconLargeUrl;
    }
}
