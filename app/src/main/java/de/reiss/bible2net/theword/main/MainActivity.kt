package de.reiss.bible2net.theword.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.about.AboutActivity
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.bible.BibleActivity
import de.reiss.bible2net.theword.databinding.MainActivityBinding
import de.reiss.bible2net.theword.main.viewpager.ViewPagerFragment
import de.reiss.bible2net.theword.note.list.NoteListActivity
import de.reiss.bible2net.theword.preferences.AppPreferencesActivity
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn
import java.util.Calendar

class MainActivity : AppActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    private lateinit var binding: MainActivityBinding

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private val drawerBackCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            binding.mainDrawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        if (redirectIfChosenBibleMissing()) {
            return
        }

        onBackPressedDispatcher.addCallback(this, drawerBackCallback)
        initNav()
        refreshFragment()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_notes -> {
                startActivity(NoteListActivity.createIntent(this))
            }
            R.id.nav_settings -> {
                goToSettings()
            }
            R.id.nav_info -> {
                startActivity(AboutActivity.createIntent(this))
            }
        }
        binding.mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun refreshFragment() {
        if (findFragmentIn(R.id.main_fragment_container) == null) {
            replaceFragmentIn(
                container = R.id.main_fragment_container,
                fragment = ViewPagerFragment.createInstance(
                    DaysPositionUtil.positionFor(Calendar.getInstance())
                )
            )
        }
    }

    private fun redirectIfChosenBibleMissing(): Boolean {
        val chosenBible = appPreferences.chosenBible
        if (chosenBible == null) {
            startActivity(BibleActivity.createIntent(this))
            supportFinishAfterTransition()
            return true
        }
        return false
    }

    private fun initNav() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.mainDrawer,
            binding.mainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.mainDrawer.addDrawerListener(toggle)
        binding.mainDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                drawerBackCallback.isEnabled = true
            }
            override fun onDrawerClosed(drawerView: View) {
                drawerBackCallback.isEnabled = false
            }
            override fun onDrawerStateChanged(newState: Int) {}
        })
        toggle.syncState()

        binding.mainNav.setNavigationItemSelectedListener(this)
    }

    private fun goToSettings() {
        startActivity(AppPreferencesActivity.createIntent(this))
    }
}
