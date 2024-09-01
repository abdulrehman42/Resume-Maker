package com.pentabit.cvmaker.resumebuilder.utils;

public enum Genders {

    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String gender;

    Genders(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public static Genders findGender(String value) {
        for (Genders enumValue : Genders.values()) {
            if (enumValue.getGender().equalsIgnoreCase(value))
                return enumValue;
        }
        return OTHER;
    }
}
