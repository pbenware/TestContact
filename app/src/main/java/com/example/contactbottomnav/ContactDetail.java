package com.example.contactbottomnav;

import java.util.List;

public class ContactDetail {
    String contact_id;
    String first_Name;
    String last_name;
    String company;
    String avatar;
    int unread_messages;
    String company_image;
    List<Phone> phones;
    List<Email> emails;
    List<SocialMedia> social_medias;

    public String getContactId() {
        return contact_id;
    }

    public String getFirstName() {
        return first_Name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getCompany() {
        return company;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getUnreadMessages() {
        return unread_messages;
    }

    public String getCompanyImage() {
        return company_image;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public List<SocialMedia> getSocialMedias() {
        return social_medias;
    }
}
