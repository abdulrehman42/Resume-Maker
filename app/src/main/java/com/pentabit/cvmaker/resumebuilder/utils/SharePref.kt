package com.pentabit.cvmaker.resumebuilder.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Constants.PREFS_TOKEN_FILE
import com.google.gson.Gson

class SharePref constructor(ctx: Context){
    val gson = Gson()
    var prefs: SharedPreferences? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        @SuppressLint("StaticFieldLeak")
        var mInstence: SharePref? = null

        fun init(context: Context?) {
            mContext = context
        }

        fun getInstance(): SharePref? {
            if (mInstence != null) {
                return mInstence
            } else {
                mInstence = SharePref(mContext!!.applicationContext)
            }
            return mInstence
        }
    }
    var isThisSessionFromLink = false

    init {
        prefs = ctx.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)
        isThisSessionFromLink = false
    }

    fun writeBoolean(key: String?, value: Boolean) {
        prefs!!.edit().putBoolean(key, value).apply()
    }

    fun readBoolean(key: String?,
                    defValue: Boolean
    ): Boolean {
        return prefs!!.getBoolean(key, defValue)
    }

    fun writeInteger(key: String?, value: Int) {
        prefs!!.edit().putInt(key, value).apply()
    }

    fun readInteger(key: String?, defValue: Int): Int {
        return prefs!!.getInt(key, defValue)
    }

    fun writeString(key: String?, value: String?) {
        prefs!!.edit().putString(key, value).apply()
    }

    fun readString(key: String?, defValue: String): String? {
        return prefs!!.getString(key, defValue)
    }
    fun readString(key: String?): String? {
        return prefs!!.getString(key, null)
    }
    fun deleteAllSharedPrefs() {
        prefs!!.edit().clear().apply()
    }
    @SuppressLint("CommitPrefEdits")
    fun deleteAllSharedPrefsExcept(exceptKey: String) {
        val keys = prefs!!.all.keys

        for (key in keys) {
            if (key != exceptKey) {
                prefs!!.edit().remove(key)
            }
        }
    }
    @SuppressLint("CommitPrefEdits")
    fun deleteAllSharedPrefsExcept(vararg keysToKeep: String) {
        val keysToKeepSet = keysToKeep.toSet()

        // Get all the keys in SharedPreferences
        val allKeys = prefs!!.all.keys

        // Iterate through the keys and remove those that are not in the keysToKeepSet
        for (key in allKeys) {
            if (key !in keysToKeepSet) {
                prefs!!.edit().remove(key)
            }
        }

        // Apply the changes
        prefs!!.edit().apply()
    }

    fun deleteItemSharePref(value:String){
        prefs!!.edit().remove(value).apply()
    }

    fun readDataEducation(): ProfileModelAddDetailResponse.UserQualification? {
        val json = prefs?.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, null)
        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, ProfileModelAddDetailResponse.UserQualification::class.java)
        } else {
            // Return a default instance if the JSON is null or empty
            return null
        }
    }
    fun writeDataEdu(user: ProfileModelAddDetailResponse.UserQualification) {
        try {
            val data=gson.toJson(user)
            prefs!!.edit().putString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, data).apply()
        }catch (e:Exception)
        {
            Log.e("TAGException", e.message.toString())
        }

    }
    fun writeDataExperience(user: ProfileModelAddDetailResponse.UserExperience) {
        try {
            val data=gson.toJson(user)
            prefs!!.edit().putString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, data).apply()
        }catch (e:Exception)
        {
            Log.e("TAGException", e.message.toString())
        }

    }
    fun readProfileReference(): ProfileModelAddDetailResponse.UserReference? {
        val json = prefs!!.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, null)
        val gson = Gson()
        return gson.fromJson(json, ProfileModelAddDetailResponse.UserReference::class.java)
    }
    fun writeDataReference(user: ProfileModelAddDetailResponse.UserReference) {
        try {
            val data=gson.toJson(user)
            prefs!!.edit().putString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, data).apply()
        }catch (e:Exception)
        {
            Log.e("TAGException", e.message.toString())
        }

    }
    fun readProfileProject(): ProfileModelAddDetailResponse.UserProject? {
        val json = prefs!!.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, null)
        val gson = Gson()
        return gson.fromJson(json, ProfileModelAddDetailResponse.UserProject::class.java)
    }
    fun writeDataProjects(user: ProfileModelAddDetailResponse.UserProject) {
        try {
            val data=gson.toJson(user)
            prefs!!.edit().putString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, data).apply()
        }catch (e:Exception)
        {
            Log.e("TAGException", e.message.toString())
        }

    }
    fun readProfileExperience(): ProfileModelAddDetailResponse.UserExperience? {
        val json = prefs!!.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, null)
        val gson = Gson()
        return gson.fromJson(json, ProfileModelAddDetailResponse.UserExperience::class.java)
    }
    fun writeDataAchievement(user: ProfileModelAddDetailResponse.UserAchievement) {
        try {
            val data=gson.toJson(user)
            prefs!!.edit().putString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, data).apply()
        }catch (e:Exception)
        {
            Log.e("TAGException", e.message.toString())
        }

    }

    fun readProfileAchievement(): ProfileModelAddDetailResponse.UserAchievement? {
        val json = prefs!!.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, null)
        val gson = Gson()
        return gson.fromJson(json, ProfileModelAddDetailResponse.UserAchievement::class.java)
    }
    fun writeDataProfile(user: ProfileModelAddDetailResponse) {
        try {
            val data=gson.toJson(user)
            prefs!!.edit().putString(Constants.DATA_PROFILE, data).apply()
        }catch (e:Exception)
        {
            Log.e("TAGException", e.message.toString())
        }

    }

    fun readProfileData(): ProfileModelAddDetailResponse? {
        val json = prefs?.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA_PROFILE, null)
        return if (json != null) {
            Gson().fromJson(json, ProfileModelAddDetailResponse::class.java)
        } else {
            null // or ProfileModelAddDetailResponse() if you prefer a non-null return
        }
    }


}