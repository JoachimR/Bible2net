package de.reiss.bible2net.theword.architecture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import de.reiss.bible2net.theword.App

abstract class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(App.component.appPreferences.currentTheme().theme)
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
