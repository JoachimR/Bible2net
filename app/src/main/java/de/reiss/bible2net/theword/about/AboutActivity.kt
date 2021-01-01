package de.reiss.bible2net.theword.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.databinding.AboutActivityBinding
import de.reiss.bible2net.theword.util.appVersion
import de.reiss.bible2net.theword.util.extensions.onClick

class AboutActivity : AppActivity() {

    companion object {
        fun createIntent(context: Context): Intent =
                Intent(context, AboutActivity::class.java)
    }

    private lateinit var binding: AboutActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AboutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.appVersion.text = appVersion(this)

        binding.privacyPolicyButton.onClick {
            startActivity(PrivacyPolicyActivity.createIntent(this))
        }
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
}