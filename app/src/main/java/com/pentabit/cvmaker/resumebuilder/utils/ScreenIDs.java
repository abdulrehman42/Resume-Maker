package com.pentabit.cvmaker.resumebuilder.utils;

public enum ScreenIDs {
    DEFAULT(-1),
    SPLASH(0),
    Onboarding(1),
    HOME(2), // Subscription
    CHOOSE_RESUME_TEMPLATES(3), // Wallpaper Slider
    LOGIN(4), // favourite screen
    ADD_BASIC_INFO(5),// downloads screen
    ADD_OBJECTIVE(6), // Intro screen
    ADD_EDUCATION(7),// languages screen
    ADD_SKILLS(8), // Preview Parallax screen ( not is use now )
    ADD_EXPERIENCE(9), // Live Preview Screen
    ADD_REFERENCE(10), // Exit Dialog
    ADD_INTERESTS(11), // Main Home drawer screen of wallpapers
    ADD_LANGUAGE(12), // NOT IN USE
    ADD_PROJECT(13), // REMOVE BG Screen in create wallpaper
    ADD_ACHIEVEMENT(14),// Add Sticker Screen in create wallpaper
    PREVIEW_RESUME(15), // Text Sticker Screen in create wallpaper
    CHOOSE_COVER_LETTER_TEMPELATES(16), // REMOVE WaterMark Screen in create wallpaper
    CREATE_COVER_LETTER(17), // Wallpaper Preview Screen in create wallpaper
    COVER_LETTER_SAMPLES(18), // Main Home Screen
    SELECT_COVER_LETTER(19), // Created Wallpapers Gallery
    PREVIEW_COVER_LETTER(20), // Use on all Dialogs
    DOWNLOADS(21), // Use on all Dialogs
    PROFILE_LISTING(22), // Use on all Dialogs
    PROFILE_PREVIEW(23),
    REMOVE_ADS_SCREEN(24),
    SUBSCRIPTION(25);

    public static ScreenIDs getPair(String value) {
        for (ScreenIDs enumValue : ScreenIDs.values()) {
            if (enumValue.name().equalsIgnoreCase(value))
                return enumValue;
        }
        return DEFAULT;
    }

    private final int id;

    ScreenIDs(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
