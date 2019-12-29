package de.reiss.bible2net.theword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.reiss.bible2net.theword.bible.BibleActivity
import de.reiss.bible2net.theword.main.MainActivity


class SplashScreenActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, SplashScreenActivity::class.java)

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToRightStart()
        supportFinishAfterTransition()
    }

    private fun goToRightStart() {
        startActivity(
                when {
                    appPreferences.chosenBible == null -> {
                        BibleActivity.createIntent(this)
                    }
                    else -> {
                        MainActivity.createIntent(this)
                    }
                })

    }

}
