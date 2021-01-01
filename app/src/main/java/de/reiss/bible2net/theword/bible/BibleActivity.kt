package de.reiss.bible2net.theword.bible

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.databinding.BibleActivityBinding
import de.reiss.bible2net.theword.main.MainActivity
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn

class BibleActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, BibleActivity::class.java)
    }

    private lateinit var binding: BibleActivityBinding

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private val prefChangedListener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            redirectIfBibleChosen()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BibleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (redirectIfBibleChosen()) {
            return
        }

        appPreferences.registerListener(prefChangedListener)

        initFragment()
    }

    private fun initFragment() {
        if (findFragmentIn(R.id.bible_fragment_container) == null) {
            replaceFragmentIn(
                container = R.id.bible_fragment_container,
                fragment = BibleFragment.createInstance()
            )
        }
    }

    private fun redirectIfBibleChosen(): Boolean {
        if (appPreferences.chosenBible != null) {
            startActivity(MainActivity.createIntent(this))
            supportFinishAfterTransition()
            return true
        }
        return false
    }
}
