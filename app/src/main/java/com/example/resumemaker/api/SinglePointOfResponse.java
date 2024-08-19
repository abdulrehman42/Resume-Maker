package com.example.resumemaker.api;

import static com.pentabit.pentabitessentials.utils.EConstantsKt.ERROR_MESSAGE;

import androidx.annotation.NonNull;

import com.example.resumemaker.json.JSONKeys;
import com.example.resumemaker.json.JSONManager;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.pentabit.pentabitessentials.api.exceptions.ClientException;
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogManager;
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinglePointOfResponse implements Callback<JsonElement> {
    private final Callback<JsonElement> originalCallback;

    public SinglePointOfResponse(Callback<JsonElement> originalCallback) {
        this.originalCallback = originalCallback;
    }

    @Override
    public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
        try {
            if (response.isSuccessful())
                originalCallback.onResponse(call, response);
            else if (response.errorBody() != null) {
                String errorMessage = (String) JSONManager.getInstance().getFormattedResponse(JSONKeys.MESSAGE, response.errorBody(), new TypeToken<String>() {
                }.getType());
                originalCallback.onFailure(call, new ClientException(errorMessage));
            }
        } catch (Exception e) {
            AppsKitSDKLogManager.getInstance().log(AppsKitSDKLogType.ERROR, "Exception in onResponse: " + e.getMessage());
            originalCallback.onFailure(call, new ClientException(ERROR_MESSAGE));
        }
    }

    @Override
    public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
        originalCallback.onFailure(call, t);
    }
}
