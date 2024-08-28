package com.pentabit.cvmaker.resumebuilder.utils;

import android.util.Base64;

import com.google.errorprone.annotations.Keep;

import java.io.UnsupportedEncodingException;

@Keep
public class JWTUtils {

    private JWTUtils() {
    }

    public static String decoded(String jwtToken) {
        try {
            String[] split = jwtToken.split("\\.");
            return getJson(split[1]);
        } catch (Exception e) {
            return "";
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
