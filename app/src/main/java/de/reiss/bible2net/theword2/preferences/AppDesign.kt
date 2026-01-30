package de.reiss.bible2net.theword2.preferences

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import de.reiss.bible2net.theword2.R

enum class AppDesign(@StringRes val prefKey: Int, @NightMode val nightMode: Int) {

    BRIGHT(R.string.pref_design_option_bright, AppCompatDelegate.MODE_NIGHT_NO),
    DARK(R.string.pref_design_option_dark, AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM(R.string.pref_design_option_system, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    companion object {
        fun default(): AppDesign = SYSTEM

        fun find(context: Context, key: String): AppDesign? =
            values().firstOrNull {
                context.getString(it.prefKey) == key
            }
    }
}
