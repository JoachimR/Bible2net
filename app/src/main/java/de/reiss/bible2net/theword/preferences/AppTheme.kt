package de.reiss.bible2net.theword.preferences

import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import de.reiss.bible2net.theword.R

enum class AppTheme(@StringRes val prefKey: Int, @StyleRes val theme: Int) {

    RED_TEAL(R.string.pref_theme_value_red_teal, R.style.AppTheme),
    ORANGE_BLUE(R.string.pref_theme_value_blue_orange, R.style.AppThemeBlue),
    GREY_CYAN(R.string.pref_theme_value_grey_cyan, R.style.AppThemeGrey),
    GREEN_BROWN(R.string.pref_theme_value_green_brown, R.style.AppThemeGreen);

    companion object {

        fun find(context: Context, key: String): AppTheme? =
            values().firstOrNull {
                context.getString(it.prefKey) == key
            }
    }
}
