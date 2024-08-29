package com.pentabit.cvmaker.resumebuilder.models;

import java.util.List;

public class User {

    String id;
    String name;
    String email;
    String oauthId;
    String phoneLanguage;
    String oauthProvider;
    boolean isDeleted;
    List<Integer> profiles;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOauthId() {
        return oauthId;
    }

    public String getPhoneLanguage() {
        return phoneLanguage;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public List<Integer> getProfiles() {
        return profiles;
    }
}
