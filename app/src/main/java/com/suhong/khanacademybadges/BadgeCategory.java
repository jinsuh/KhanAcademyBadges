package com.suhong.khanacademybadges;

public class BadgeCategory {

    private int id;
    private String name;
    private String description;
    private String iconUrl;

    public BadgeCategory(int id, String name, String description, String iconUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
