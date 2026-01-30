package de.reiss.bible2net.theword2.preferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword2.App
import de.reiss.bible2net.theword2.R
import de.reiss.bible2net.theword2.SplashScreenActivity
import de.reiss.bible2net.theword2.architecture.AppActivity
import de.reiss.bible2net.theword2.databinding.PreferenceActivityBinding
import de.reiss.bible2net.theword2.util.extensions.replaceFragmentIn

class AppPreferencesActivity : AppActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, AppPreferencesActivity::class.java)
    }

    private lateinit var binding: PreferenceActivityBinding

    lateinit var viewModel: AppPreferencesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PreferenceActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.preferencesToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(
            this,
            AppPreferencesViewModel.Factory(
                App.component.appPreferencesRepository
            )
        )[AppPreferencesViewModel::class.java]

        viewModel.biblesLiveData.observe(this) { updateUi() }

        App.component.appPreferences.registerListener(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadBibles()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.pref_theme_key) -> {
                restartApp()
            }
            getString(R.string.pref_design_key) -> {
                val nightMode = App.component.appPreferences.currentDesign().nightMode
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }
        }
    }

    private fun updateUi() {
        if (viewModel.isLoadingBibles()) {
            binding.preferencesLoading.setLoading(true)
            binding.preferencesFragmentContainer.visibility = GONE
        } else {
            binding.preferencesLoading.setLoading(false)
            binding.preferencesFragmentContainer.visibility = VISIBLE

            if (supportFragmentManager
                .findFragmentById(R.id.preferences_fragment_container) == null
            ) {
                replaceFragmentIn(
                    R.id.preferences_fragment_container,
                    AppPreferencesFragment.newInstance(viewModel.bibles())
                )
            }
        }
    }

    private fun restartApp() {
        startActivity(
            SplashScreenActivity.createIntent(this)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        supportFinishAfterTransition()
    }
}
