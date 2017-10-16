package de.reiss.bible2net.theword.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import kotlinx.android.synthetic.main.about_activity.*

class AboutActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, AboutActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        setSupportActionBar(about_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        about_app_version.text = appVersion()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.about_share -> {
                    val text = getString(R.string.share_app, getString(R.string.play_store_url))
                    startActivity(Intent.createChooser(Intent()
                            .setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_TEXT, text)
                            .setType("text/plain"),
                            getString(R.string.share_app_title)))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun appVersion(): String {
        val version = packageManager.getPackageInfo(packageName, 0).versionName
        return getString(R.string.app_version, version)
    }

}