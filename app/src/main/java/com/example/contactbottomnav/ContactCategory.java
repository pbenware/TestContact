package com.example.contactbottomnav;

public class ContactCategory {

    String category_id;
    String name;
    String icon;

    public ContactCategory(String catId, String catName, String catIcon) {
        category_id = catId;
        name = catName;
        icon = catIcon;
    }
    public String getCategoryId() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
