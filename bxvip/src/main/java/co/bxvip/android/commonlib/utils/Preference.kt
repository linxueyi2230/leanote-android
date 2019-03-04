package co.bxvip.android.commonlib.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import co.bxvip.android.commonlib.utils.CommonInit.ctx

/**
 * desc:kotlin委托属性+SharedPreference实例
 */

object Preference {

    private inline val sp: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(ctx)

    fun spSetInt(key: String, value: Int) = sp.edit().putInt(key, value).apply()

    fun spGetInt(key: String, defaultValue: Int = 0) = sp.getInt(key, defaultValue)

    fun spSetBoolean(key: String, value: Boolean) = sp.edit().putBoolean(key, value).apply()

    fun spGetBoolean(key: String, defaultValue: Boolean = false) = sp.getBoolean(key, defaultValue)

    fun spSetString(key: String, value: String) = sp.edit().putString(key, value).apply()

    fun spGetString(key: String, defaultValue: String = "") = sp.getString(key, defaultValue)!!

    fun spRemove(key: String) = sp.edit().remove(key).apply()

    fun spClearAll() = sp.edit().clear().apply()
}