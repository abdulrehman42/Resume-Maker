package com.pentabit.cvmaker.resumebuilder.json;

import com.google.errorprone.annotations.Keep;

@Keep
public enum JSONKeys {
    DATA("data"),
    FORMAT_DATA("askRecommendationCategories"),
    ID("id"),
    MESSAGE("msg"),
    PAGE_NUMBER("page"),
    PROFILE_COMPLETION("profileCompletion"),
    DATA_SOURCE("source"),
    TOKEN("token");

    private final String key;

    JSONKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
