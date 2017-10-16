package de.reiss.bible2net.theword.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.about.AboutActivity
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.bible.BibleActivity
import de.reiss.bible2net.theword.main.viewpager.ViewPagerFragment
import de.reiss.bible2net.theword.note.list.NoteListActivity
import de.reiss.bible2net.theword.preferences.AppPreferencesActivity
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


class MainActivity : AppActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(main_toolbar)

        if (redirectIfChosenBibleMissing()) {
            return
        }

        initNav()
        refreshFragment()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.main_drawer)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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
        main_drawer.closeDrawer(GravityCompat.START)
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
        val toggle = ActionBarDrawerToggle(this,
                main_drawer,
                main_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        main_drawer.addDrawerListener(toggle)
        toggle.syncState()

        main_nav.setNavigationItemSelectedListener(this)
    }

    private fun goToSettings() {
        startActivity(AppPreferencesActivity.createIntent(this))
    }

}
