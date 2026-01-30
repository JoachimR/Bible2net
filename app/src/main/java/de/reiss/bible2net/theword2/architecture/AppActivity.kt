package de.reiss.bible2net.theword2.architecture

import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.R as MaterialR
import de.reiss.bible2net.theword2.App

abstract class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(App.component.appPreferences.currentTheme().theme)
        val nightMode = App.component.appPreferences.currentDesign().nightMode
        AppCompatDelegate.setDefaultNightMode(nightMode)

        val typedValue = TypedValue()
        theme.resolveAttribute(MaterialR.attr.colorPrimaryVariant, typedValue, true)
        val statusBarColor = typedValue.data
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(statusBarColor)
        )

        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
