package me.ndkshr.melon.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtils {

    private var sharedPreferences: SharedPreferences? = null
    fun getSharedPrefs(ctx: Context): SharedPreferences {
        if (sharedPreferences == null) {
            sharedPreferences = ctx.getSharedPreferences("com.nandakishor.core", Context.MODE_PRIVATE)
        }
        return sharedPreferences!!
    }

    fun getStringPref(ctx: Context, key: String): String {
        return getSharedPrefs(ctx).getString(key, "") ?: ""
    }

    fun setStringPref(ctx: Context, key: String, value: String) {
        getSharedPrefs(ctx).edit().putString(key, value).apply()
    }

}