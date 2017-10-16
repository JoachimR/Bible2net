package de.reiss.bible2net.theword.preferences

import android.content.Context
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import de.reiss.bible2net.theword.R

enum class AppTheme(@StringRes val prefKey: Int, @StyleRes val theme: Int) {

    RED_TEAL(R.string.pref_theme_value_red_teal, R.style.AppTheme_NoActionBar),
    ORANGE_BLUE(R.string.pref_theme_value_blue_orange, R.style.AppThemeBlue_NoActionBar),
    GREY_CYAN(R.string.pref_theme_value_grey_cyan, R.style.AppThemeGrey_NoActionBar),
    GREEN_BROWN(R.string.pref_theme_value_green_brown, R.style.AppThemeGreen_NoActionBar);

    companion object {

        fun find(context: Context, key: String) =
                values().firstOrNull {
                    context.getString(it.prefKey) == key
                }

    }

}