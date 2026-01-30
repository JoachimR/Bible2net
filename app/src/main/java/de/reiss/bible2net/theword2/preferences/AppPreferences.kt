package de.reiss.bible2net.theword2.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import de.reiss.bible2net.theword2.R
import de.reiss.bible2net.theword2.events.FontSizeChanged
import de.reiss.bible2net.theword2.events.postMessageEvent
import de.reiss.bible2net.theword2.util.extensions.change
import de.reiss.bible2net.theword2.widget.triggerWidgetRefresh

open class AppPreferences(val context: Context) : OnSharedPreferenceChangeListener {

    companion object {

        private const val KEY_LAST_TIME_BIBLES_UPDATED = "LAST_TIME_BIBLES_UPDATED"

        private const val MAX_AGE_BIBLES_LAST_UPDATED = 86400000 // 1 day
    }

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        registerListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == preferences && key != null) {
            if (isWidgetPref(key)) {
                triggerWidgetRefresh()
            } else {
                if (key == str(R.string.pref_fontsize_key)) {
                    postMessageEvent(FontSizeChanged())
                }
            }
        }
    }

    private fun isWidgetPref(key: String): Boolean = (
        key == str(R.string.pref_language_key) ||
            key == str(R.string.pref_widget_fontsize_key) ||
            key == str(R.string.pref_widget_fontcolor_key) ||
            key == str(R.string.pref_widget_backgroundcolor_key) ||
            key == str(R.string.pref_widget_showdate_key) ||
            key == str(R.string.pref_widget_centered_text_key)
        )

    fun registerListener(listener: OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    open var chosenBible: String?
        get() = prefString(R.string.pref_language_key)
        set(chosenBible) = preferences.change {
            putString(str(R.string.pref_language_key), chosenBible)
        }

    fun currentTheme(): AppTheme {
        val chosenTheme = prefString(R.string.pref_theme_key, R.string.pref_theme_default)
        return AppTheme.find(context, chosenTheme!!) ?: AppTheme.default()
    }

    fun currentDesign(): AppDesign {
        val chosenDesign = prefString(R.string.pref_design_key, R.string.pref_design_option_default)
        return AppDesign.find(context, chosenDesign!!) ?: AppDesign.default()
    }

    var lastTimeBiblesUpdated: Long
        get() = preferences.getLong(KEY_LAST_TIME_BIBLES_UPDATED, -1)
        set(time) = preferences.change {
            putLong(KEY_LAST_TIME_BIBLES_UPDATED, time)
        }

    fun biblesNeedUpdate() =
        lastTimeBiblesUpdated < System.currentTimeMillis() - MAX_AGE_BIBLES_LAST_UPDATED

    fun showNotes() = prefBoolean(R.string.pref_shownotes_key, true)

    fun shouldShowDailyNotification() =
        prefBoolean(R.string.pref_show_daily_notification_key, false)

    fun fontSize() = prefInt(
        stringRes = R.string.pref_fontsize_key,
        default = Integer.parseInt(str(R.string.pref_fontsize_max))
    )

    fun widgetShowDate() = prefBoolean(R.string.pref_widget_showdate_key, true)

    fun widgetFontColor() = prefInt(
        R.string.pref_widget_fontcolor_key,
        ContextCompat.getColor(context, R.color.font_black)
    )

    fun widgetFontSize() = prefInt(
        stringRes = R.string.pref_widget_fontsize_key,
        default = Integer.parseInt(str(R.string.pref_widget_fontsize_default))
    ).toFloat()

    fun widgetCentered() = prefBoolean(R.string.pref_widget_centered_text_key, true)

    fun widgetBackground(): String = prefString(
        R.string.pref_widget_backgroundcolor_key,
        R.string.pref_widget_backgroundcolor_default
    )!!

    fun changeFontSize(newFontSize: Int) {
        val min = Integer.parseInt(str(R.string.pref_fontsize_min))
        val max = Integer.parseInt(str(R.string.pref_fontsize_max))
        val changeValue = when {
            newFontSize < min -> min
            newFontSize > max -> max
            else -> newFontSize
        }
        preferences.change {
            putInt(str(R.string.pref_fontsize_key), changeValue)
        }
    }

    private fun prefString(@StringRes stringRes: Int, @StringRes defaultStringRes: Int? = null) =
        preferences.getString(
            str(stringRes),
            if (defaultStringRes != null) str(defaultStringRes) else null
        )

    private fun prefBoolean(@StringRes stringRes: Int, default: Boolean) =
        preferences.getBoolean(str(stringRes), default)

    private fun prefInt(@StringRes stringRes: Int, default: Int) =
        preferences.getInt(str(stringRes), default)

    private fun str(@StringRes stringRes: Int) = context.getString(stringRes)
}
